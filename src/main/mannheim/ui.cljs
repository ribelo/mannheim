(ns mannheim.ui
  (:require
   ["react-transition-group" :refer [Transition CSSTransition TransitionGroup]]
   ["react-feather" :refer [ChevronRight DollarSign ShoppingCart]]
   [applied-science.js-interop :as j]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [taoensso.encore :as enc]
   [mannheim.init :as init]
   [mannheim.market.payments.ui :as payments.ui]
   [mannheim.ui.components :refer [button]]
   [mannheim.ui.subs :as ui.sub]
   [mannheim.ui.events :as ui.evt]
   [mannheim.auth.subs :as auth.subs]
   [mannheim.auth.events :as auth.events]
   [mannheim.auth.ui :as auth.ui]
   [re-frame.core :as rf]
   [reagent.core :as r]))


(defn notification []
  (let [msgs @(rf/subscribe [::ui.sub/notifications])]
    [:div {:class "fixed flex flex-row items-center top-0 right-0 mr-4 mt-12"}
     [:div
      [:> TransitionGroup
       (for [{:keys [title content type] :as msg*}  msgs]
         ^{:key (or title (enc/uuid-str))}
         [:> CSSTransition {:in              true
                            :timeout         500
                            :unmount-on-exit true
                            :class-names     "notification"}
          (fn [_]
            (r/as-element
             [:div {:class ["min-h-24 w-64 bg-nord-3 z-50 shadow my-4"
                            (case type
                              :error   ["border" "border-nord-11"]
                              :success ["border" "border-nord-14"]
                              nil)]}
              (when title
                [:div {:class "px-4 py-2 flex-1 font-medium tracking-wider text-nord-4"}
                 title])
              (when content
                [:div {:class "px-4 py-2 text-nord-4 text-xs break-words"}
                 content])]))])]]]))

(defn logo []
  (fn []
    [:div {:class "text-xl text-nord-4 mx-4 w-48 hover:text-nord-7 transition duration-150 easy-in-out"}
     "mannheim"]))

(defn account []
  (fn []
    [:div {:class "text-xs text-nord-4 mx-4 text-right w-48 hover:text-nord-7 transition duration-150 easy-in-out"}
     "r.krzywaznia@teas.com.pl"]))

(defn navbar [content]
  [:div {:class "flex h-12 min-h-32 justify-between items-center bg-nord-1 shadow"}
   [logo]
   content
   [account]])

(defn sidebar []
  (let [expanded? @(rf/subscribe [::ui.sub/sidebar-expanded?])]
    [:div {:class ["flex flex-none flex-col p-2 bg-nord-2 overflow-y-auto shadow-xl transition-all duration-150 ease-in-out"
                   (if expanded? "w-32" "w-12")]}
     [:div {:class "flex-1 text-nord-4"}
      [:div {:class "hover:text-nord-7 transition duration-150 easy-in-out"
             :on-click #(rf/dispatch [::ui.evt/set-view :payments])}
       (if expanded?
         [:div {:class "py-1 px-2 truncate"}
          "płatności"]
         [:div {:class "py-1 px-1 mx-auto"}
          [:> DollarSign]])]
      [:div {:class "hover:text-nord-7 transition duration-150 easy-in-out"
             :on-click #(rf/dispatch [::ui.evt/set-view :dc-order])}
       (if expanded?
         [:div {:class "py-1 px-2 truncate"}
          "zamówienia"]
         [:div {:class "py-1 px-1 mx-auto"}
          [:> ShoppingCart]])]]
     [:div {:class "ml-auto text-nord-4"}
      [:> ChevronRight
       {:class    "cursor-pointer transition-transform duration-150 ease-in-out"
        :style    {:transform (if expanded? "rotate(-180deg)")}
        :on-click #(if expanded?
                     (rf/dispatch [:mannheim.ui.events/shrink-sidebar])
                     (rf/dispatch [:mannheim.ui.events/expand-sidebar]))}]]]))

(defn loader []
  [:div {:class "fixed flex inset-0 w-full h-full"}
   [:div {:class "absolute inset-0 w-full h-full bg-nord-3 opacity-50"}]
   [:div {:class "rounded-full h-64 w-64 border-8 border-t-8 m-auto z-50 shadow spinner"
          :style {:border-top-color "#5E81AC"}}]])

(defn main-view []
  (let [current-view  @(rf/subscribe [::ui.sub/view])
        show-spinner? @(rf/subscribe [::ui.sub/show-spinner?])]
    [:div {:class "flex flex-col h-screen min-h-screen bg-nord-4 text-nord-1"}
     (case current-view
       :payments [navbar [payments.ui/header]]
       ;; :dc-order [navbar [dc-order.ui/header]]
       [navbar [payments.ui/header]])
     [:div {:class "flex flex-1 overflow-y-hidden overflow-x-hidden"}
      [sidebar]
      [payments.ui/content]
      ]
     [notification]
     (when show-spinner? [loader])]))

(defn view []
  (let [boot-successful? @(rf/subscribe [::init/boot-successful?])
        user             @(rf/subscribe [::auth.subs/user])]
    (enc/cond
      (and boot-successful? (some? user))
      [main-view]
      :else
      [:div {:class "flex flex-col justify-center items-center w-screen h-screen inset-0 bg-nord-3"}
       [:div {:class "flex py-4 text-nord-4 text-6xl hover:text-nord-7 transition duration-150 easy-in-out"}
        "mannheim"]
       [auth.ui/login-form]])

    ))
