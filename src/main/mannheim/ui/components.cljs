(ns mannheim.ui.components
  (:require
   ["date-fns" :as dt]
   ["react-feather" :rename {X x-icon}]
   ["react-transition-group" :refer [Transition CSSTransition TransitionGroup]]
   ["react-virtualized/dist/commonjs/AutoSizer" :default auto-sizer]
   ["react-virtualized/dist/commonjs/List" :default virtual-list]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->js]]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [mannheim.init :as init]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.ratom :as ra]
   [taoensso.encore :as enc]))

(defn button
  ([text]
   (button {} text))
  ([{:keys [class] :as params} text]
   [:div (merge {:class ["flex px-4 py-1 bg-nord-0 text-nord-4 font-medium"
                         "outline-none focus:ring ring-nord-7 hover:bg-nord-7"
                         "rounded shadow cursor-pointer border border-gray-900"
                         "transition duration-150" class]}
                (dissoc params :class))
    [:div {:class "m-auto"} text]]))

(defn modal [{:keys [show?_ close title bg class] :as params} content]
  [:> CSSTransition {:in              (enc/some? @show?_)
                     :timeout         200
                     :unmount-on-exit true
                     :class-names     "modal"}
   (fn [_]
     (r/as-element
      ^{:key :modal}
      [:div {:class "fixed flex inset-0 w-full h-full"}
       [:div {:class "absolute inset-0 w-full h-full bg-nord-0 opacity-25"
              :on-click close}]
       [:div {:class ["min-h-24 m-auto z-40 shadow" (if bg bg "bg-nord-3")]}
        [:div {:class "border-b border-nord-0 bg-nord-3"}
         (when title
           [:div {:class "px-4 py-2 flex items-center"}
            [:div {:class "flex-1 text-xl font-medium tracking-wider text-nord-4"}
             title]
            [:> x-icon {:class    "text-nord-4 cursor-pointer hover:text-nord-11
                                   transition duration-150 easy-in-out"
                        :on-click close}]])]
        [:div {:class "px-4 py-2"}
         content]]]))])
