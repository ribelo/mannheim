(ns mannheim.ui
  (:require
   ["react-transition-group" :refer [Transition CSSTransition TransitionGroup]]
   ["react-feather" :refer [ChevronRight DollarSign ShoppingCart]]
   [applied-science.js-interop :as j]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [taoensso.encore :as e]
   [mannheim.init :as init]
   [mannheim.payments.ui :as payments.ui]
   [mannheim.dc-order.ui :as dc-order.ui]
   [mannheim.ui.subs :as ui.sub]
   [mannheim.ui.events :as ui.evt]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn notification []
  (let [msgs @(rf/subscribe [::ui.sub/notifications])]
    [:div.fixed.flex.flex-row.items-center.top-0.right-0.mr-4.mt-12
     [:div
      [:> TransitionGroup
       (for [msg  msgs
             :let [{:keys [title content type] :as msg*} (apply array-map msg)]]
         ^{:key (or title (e/uuid-str))}
         [:> CSSTransition {:in              true
                            :timeout         500
                            :unmount-on-exit true
                            :class-names     "notification"}
          (fn [_]
            (r/as-element
             [:div.min-h-24.w-64.bg-nord-3.z-50.shadow.my-4
              {:class (case type
                        :error   [:border :border-nord-11]
                        :success [:border :border-nord-14]
                        nil)}
              (when title
                [:div.px-4.py-2.flex-1.font-medium.tracking-wider.text-nord-4
                 title])
              (when content
                [:div.px-4.py-2.text-nord-4.text-xs.break-words
                 content])]))])]]]))

(defn logo []
  (fn []
    [:div.text-xl.text-nord-4.mx-4.w-48.hover:text-nord-7.transition.duration-200.easy-in-out
     "mannheim"]))

(defn account []
  (fn []
    [:div.text-xs.text-nord-4.mx-4.text-right.w-48.hover:text-nord-7.transition.duration-200.easy-in-out
     "r.krzywaznia@teas.com.pl"]))

(defn navbar [content]
  [:div.flex.h-12.min-h-32.justify-between.items-center.bg-nord-1.shadow
   [logo]
   content
   [account]])

(defn sidebar []
  (let [expanded? @(rf/subscribe [::ui.sub/sidebar-expanded?])]
    [:div.p-2.flex.flex-col.bg-nord-2.overflow-y-auto.shadow-xl.transition-all.duration-200.ease-in-out
     {:class (if expanded? :w-32 :w-12)}
     [:div.flex-1.text-nord-4
      [:div.hover:text-nord-7.transition.duration-200.easy-in-out
       {:on-click #(rf/dispatch [::ui.evt/set-view :payments])}
       (if expanded?
         [:div.py-1.px-2.truncate "płatności"]
         [:div.py-1.px-1.mx-auto [:> DollarSign]])]
      [:div.hover:text-nord-7.transition.duration-200.easy-in-out
       {:on-click #(rf/dispatch [::ui.evt/set-view :dc-order])}
       (if expanded?
         [:div.py-1.px-2.truncate "zamówienia"]
         [:div.py-1.px-1.mx-auto [:> ShoppingCart]])]]
     [:div.ml-auto.text-nord-4
      [:> ChevronRight
       {:class    [:cursor-pointer :transition-transform :duration-200 :ease-in-out]
        :style    {:transform (if expanded? "rotate(-180deg)")}
        :on-click #(if expanded?
                     (rf/dispatch [:mannheim.ui.events/shrink-sidebar])
                     (rf/dispatch [:mannheim.ui.events/expand-sidebar]))}]]]))

(defn loader []
  [:div.fixed.flex.inset-0.w-full.h-full
   [:div.absolute.inset-0.w-full.h-full.bg-nord-3.opacity-50]
   [:div.rounded-full.h-64.w-64.border-8.border-t-8.m-auto.z-50.shadow.spinner
    {:style {:border-top-color "#5E81AC"}}]])

(defn view []
  (if @(rf/subscribe [::init/boot-successful?])
    (let [current-view  @(rf/subscribe [::ui.sub/current-view])
          show-spinner? @(rf/subscribe [::ui.sub/show-spinner?])]
      [:div.flex.flex-col.h-screen.min-h-screen.bg-nord-4
       (case current-view
         :payments [navbar [payments.ui/header]]
         :dc-order [navbar [dc-order.ui/header]]
         [navbar [payments.ui/header]])
       [:div.flex.flex-1.overflow-y-hidden.overflow-x-hidden
        [sidebar]
        (case current-view
          :payments [payments.ui/content]
          :dc-order [dc-order.ui/content]
          [payments.ui/content])]
       [notification]
       (when show-spinner? [loader])])
    [:div.fixed.flex.w-full.h-full.inset-0.bg-nord-3
     [:div.m-auto.text-nord-4.text-6xl.hover:text-nord-7.transition.duration-200.easy-in-out
      "mannheim"]]))
