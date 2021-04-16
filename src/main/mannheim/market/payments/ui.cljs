(ns mannheim.market.payments.ui
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
   [mannheim.ui.components :as ui.c]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.ratom :as ra]
   [taoensso.encore :as e]))

(defn header []
  [:div.flex
   [ui.c/button
    {:on-click #(rf/dispatch [::p.evt/ui.show-market-chooser])}
    "wczytaj dane"]])

(defn market-chooser-dialog []
  [ui.c/modal {:show? (rf/subscribe [::p.sub/ui.show-market-chooser?])
               :close #(rf/dispatch [::p.evt/ui.hide-market-chooser])
               :title "wybierz sklep"}
   [:div.flex.justify-between
    (for [market-id ["f01450" "f01451" "f01752"]]
      ^{:key market-id}
      [:div.text-nord-4.text-lg.cursor-pointer.hover:text-nord-7.transition.duration-200
       {:on-click #(do (rf/dispatch [::p.evt/ui.hide-market-chooser])
                       (rf/dispatch [::p.evt/read-payment-file market-id]))}
       market-id])]])

(defn container [& children]
  (into
   [:div.flex-1.overflow-y-scroll.p-4]
   children))

(defn transfer-date-component [id date]
  (let [input?_ (r/atom false)]
    (fn [id date]
      (if @input?_
        [:input.w-full.bg-nord-4 {:id        (str id "-transfer-date")
                                  :type      :date
                                  :value     date
                                  :on-change #(do
                                                (rf/dispatch [::p.evt/set-transfer-date id (-> % .-target .-value)])
                                                (-> (j/call js/document :getElementById (str id "-transfer-date"))
                                                    (j/call :blur)))
                                  :on-blur   #(reset! input?_ false)}]
        [:div.w-full.flex.cursor-pointer
         (if (not-empty date)
           [:div
            {:on-click #(do (reset! input?_ true)
                            (js/setTimeout (fn []
                                             (-> (j/call js/document :getElementById (str id "-transfer-date"))
                                                 (j/call :focus)))
                                           100))}
            (str date)]
           [:div.flex-1
            {:on-click #(rf/dispatch [::p.evt/set-transfer-date id (dt/format (js/Date.) "yyyy-MM-dd")])}
            "nie opłacone"])]))))

(defn header-filter [{:keys [evt sub class]}]
  (let [value @(rf/subscribe sub)]
    [:div.flex
     [:input.flex-1.w-full.bg-nord-4.rounded.focus:outline-none.focus:shadow-outline.truncate
      {:class       class
       :type        :text
       :placeholder "filtruj"
       :value       value
       :on-change   #(rf/dispatch (conj evt (-> % .-target .-value)))}]
     (when (seq value)
       [:div.text-nord-3.hover:text-nord-11.cursor-pointer.transition-all.duration-200.ease-in-out
        [:> x-icon
         {:class    [:w-4 :-ml-5]
          :on-click #(rf/dispatch (conj evt ""))}]])]))

(defn table-row [id]
  (let [doc                    @(rf/subscribe [::p.sub/document-pull [:document/id id]])
        document-selected?   @(rf/subscribe [::p.sub/document-selected? id])
        after-deadline?      (and (empty? (:document/transfer-date doc))
                                  (dt/isAfter
                                   (js/Date.)
                                   (js/Date. (:document/payment-date doc))))
        paid-after-deadline? (and (not-empty (:document/transfer-date doc))
                                  (dt/isAfter
                                   (js/Date. (:document/transfer-date doc))
                                   (js/Date. (:document/payment-date doc))))]
    [:div.flex.h-full.w-full.transition.duration-200.ease-in-out
     {:class (if document-selected?
               [:bg-nord-9 :hover:bg-nord-8]
               [:hover:bg-nord-5])}
     (let [market-id (:document/market.id doc)]
       [:div.flex-1.self-center.px-4.py-2.text-left.text-sm.cursor-pointer
        {:on-click #(rf/dispatch [::p.evt/ui.set-market-query market-id])}
        market-id])
     (let [name (:document/contractor.name doc)]
       [:div.flex-1.self-center.px-4.py-2.text-left.text-sm.truncate.cursor-pointer
        {:on-click #(rf/dispatch [::p.evt/ui.set-contractor-query name])}
        name])
     (let [nip (:document/contractor.nip doc)]
       [:div.flex-2.self-center.px-4.py-2.text-left.text-sm.truncate.cursor-pointer
        {:on-click #(rf/dispatch [::p.evt/ui.set-nip-query nip])}
        nip])
     [:div.flex-2.self-center.px-4.py-2.text-left.text-sm.truncate.cursor-pointer
      {:on-click #(rf/dispatch [::p.evt/toggle-document id])}
      (:document/id doc)]
     [:div.flex-1.self-center.px-4.py-2.text-right.text-sm
      (some-> (:document/net-value doc)
              identity (j/call :toFixed 2))]
     [:div.flex-1.self-center.px-4.py-2.text-right.text-sm
      (some-> (:document/gross-value doc)
              identity (j/call :toFixed 2))]
     [:div.flex-2.self-center.px-4.py-2.text-left.text-sm
      (:document/issue-date doc)]
     [:div.flex-2.self-center.px-4.py-2.text-left.text-sm
      {:class (if after-deadline? :text-nord-11)}
      (:document/payment-date doc)]
     [:div.flex-2.self-center.px-2.py-2.text-sm
      {:class (cond
                paid-after-deadline?                        :text-nord-11
                (not (empty? (:document/transfer-date doc))) :text-nord-14)}
      [transfer-date-component (:document/id doc) (:document/transfer-date doc)]]]))

(defn table-header []
  [:div
   [:div.flex.w-full
   [:div.flex-1.px-4.py-2.font-normal.text-left
     "market"]
    [:div.flex-1.px-4.py-2.font-normal.text-left
     "firma"]
    [:div.flex-2.px-4.py-2.font-normal.text-left
     "nip"]
    [:div.flex-2.px-4.py-2.font-normal.text-left
     "id"]
    [:div.flex-1.px-4.py-2.font-normal.text-right
     "netto"]
    [:div.flex-1.px-4.py-2.font-normal.text-right
     "brutto"]
    [:div.flex-2.px-4.py-2.font-normal.text-left
     "data"]
    [:div.flex-2.px-4.py-2.font-normal.text-left
     "termin"]
    [:div.flex-2.px-2.py-2.font-normal.text-left.cursor-pointer
     {:on-click #(rf/dispatch [::p.evt/ui.change-transfer-filter])}
     (condp = @(rf/subscribe [::p.sub/ui.transfer-filter])
       :all      "wszystkie"
       :paid     "opłacone"
       :not-paid "nie opłacone"
       "wszystkie")]]
   [:div.flex.border-b.border-nord-3
    [:div.flex-1.px-4.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.market-query-tmp]
       :evt [::p.evt/ui.set-market-query]}]]
    [:div.flex-1.px-4.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.contractor-query-tmp]
       :evt [::p.evt/ui.set-contractor-query]}]]
    [:div.flex-2.px-4.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.nip-query-tmp]
       :evt [::p.evt/ui.set-nip-query]}]]
    [:div.flex-2.px-4.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.document-query-tmp]
       :evt [::p.evt/ui.set-document-query]}]]
    [:div.flex-1.px-4.py-2.font-normal
     [header-filter
      {:class :text-right
       :sub   [::p.sub/ui.net-value-query-tmp]
       :evt   [::p.evt/ui.set-net-value-query]}]]
    [:div.flex-1.px-4.py-2.font-normal
     [header-filter
      {:class :text-right
       :sub   [::p.sub/ui.gross-value-query-tmp]
       :evt   [::p.evt/ui.set-gross-value-query]}]]
    [:div.flex-2.px-4.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.accounting-date-query-tmp]
       :evt [::p.evt/ui.set-accounting-date-query]}]]
    [:div.flex-2.px-4.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.payment-date-query-tmp]
       :evt [::p.evt/ui.set-payment-date-query]}]]
    [:div.flex-2.px-2.py-2.font-normal
     [header-filter
      {:sub [::p.sub/ui.transfer-date-query-tmp]
       :evt [::p.evt/ui.set-transfer-date-query]}]]]])

(defn table []
  (let [show-modal?        (r/atom false)
        document-info      (r/atom nil)
        scrollbar-visible? (r/atom false)
        documents          (rf/subscribe [::p.sub/documents-filtered])]
    (fn []
      (let [data (sort-by :document/payment-date @documents)]
        [:div.flex.flex-col.flex-1.px-4.pt-4.pb-1
         [:div
          (when @scrollbar-visible? {:style {:margin-right "15px"}}) ;; scrollbar fix
          [table-header]]
         [:div.flex-1
          [:> auto-sizer
           (fn [sizer]
             (reset! scrollbar-visible? (< (j/get sizer :height) (* 48 (count data))))
             (r/as-element
              [:> virtual-list
               {:class       [:focus:outline-none]
                :height      (j/get sizer :height)
                :width       (j/get sizer :width)
                :rowHeight   48
                :rowCount    (count data)
                :rowRenderer (fn [m]
                               (r/as-element
                                ^{:key (j/get m :key)}
                                [:div {:style (j/get m :style)}
                                 [table-row (:document/id (nth data (j/get m :index)))]]))}]))]]]))))

(defn selected-document-summary []
  (let [selected-documents @(rf/subscribe [::p.sub/selected-documents-ids])
        gross-value        @(rf/subscribe [::p.sub/selected-documents-gross-value])]
    [:div.transition-all.duration-200.ease-linear
     {:class (if (pos? (count selected-documents))
               [:mx-8 :py-4 :border-t :border-nord-3]
               [:mx-8 :h-0])}
     [:div.flex.flex-row.items-center
      [:div.flex.flex-col
       [:div.flex.text-sm
        [:div.w-32 "tytuł przelewu: "]
        [:div.select-all
         [:> TransitionGroup {:class :flex}
          (for [doc selected-documents]
            ^{:key doc}
            [:> CSSTransition {:in              true
                               :timeout         200
                               :unmount-on-exit true
                               :class-names     "transition-opacity"}
             (fn [_]
               (r/as-element
                [:div.mr-2 doc]))])]]]
       [:div.flex.text-sm
        [:div.w-32 "wartość brutto: "]
        [:div.flex
         [:div.select-all (when gross-value (.toFixed gross-value 2))]
         [:div.ml-1 "zł"]]]]
      [:div.flex.ml-auto.mr-4
       [:> x-icon
        {:class    [:text-nord-3 :hover:text-nord-11 :cursor-pointer
                    :transition-all :duration-200 :ease-in-out]
         :on-click #(rf/dispatch [::p.evt/reset-document-selection])}]]]]))

(defn content []
  [:div.flex.flex-1.flex-col
   [table]
   [selected-document-summary]
   [market-chooser-dialog]])
