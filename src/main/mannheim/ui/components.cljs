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
   [mannheim.payments.events :as p.evt]
   [mannheim.payments.subs :as p.sub]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.ratom :as ra]
   [taoensso.encore :as e]))

(defn button
  ([text]
   (button {} text))
  ([params text]
   [:div.px-2.py-1.mx-2.bg-nord-0.text-nord-4.hover:text-nord-7.font-medium.text-center.rounded.shadow.cursor-pointer.border.border-gray-900.transition.duration-200
    params
    text]))

(defn modal [{:keys [show? close title bg class] :as params} content]
  [:> CSSTransition {:in              @show?
                     :timeout         200
                     :unmount-on-exit true
                     :class-names     "modal"}
   (fn [_]
     (r/as-element
      ^{:key :modal}
      [:div.fixed.flex.inset-0.w-full.h-full
       [:div.absolute.inset-0.w-full.h-full.bg-nord-0.opacity-25
        {:on-click close}]
       [:div.min-h-24.min-w-1|6.m-auto.z-40.shadow
        {:class [ (if bg bg :bg-nord-3)]}
        [:div.border-b.border-nord-0
         {:class :bg-nord-3}
         (when title
           [:div.px-4.py-2.flex.items-center
            [:div.flex-1.text-xl.font-medium.tracking-wider.text-nord-4
             title]
            [:> x-icon {:class    [:text-nord-4 :cursor-pointer :hover:text-nord-11
                                   :transition :duration-200 :easy-in-out]
                        :on-click close}]])]
        [:div.px-4.py-2
         content]]]))])
