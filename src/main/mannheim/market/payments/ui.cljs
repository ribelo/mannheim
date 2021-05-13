(ns mannheim.market.payments.ui
  (:require
   ["date-fns" :as dt]
   ["react-feather" :refer [Check] :rename {X x-icon}]
   ["react-transition-group" :refer [Transition CSSTransition TransitionGroup SwitchTransition]]
   ["react-virtualized/dist/commonjs/AutoSizer" :default auto-sizer]
   ["react-virtualized/dist/commonjs/List" :default virtual-list]
   [applied-science.js-interop :as j]
   [taoensso.encore :as enc]
   [cljs-bean.core :refer [->js]]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [mannheim.init :as init]
   [mannheim.market.payments.events :as p.evt]
   [mannheim.market.payments.subs :as p.sub]
   [mannheim.ui.components :as ui.c]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.ratom :refer [reaction]]
   [ribelo.doxa :as dx]))

(defn header []
  [:div {:class "flex"}
   [ui.c/button
    {:on-click #(rf/dispatch [::p.evt/read-file])}
    "wczytaj dane"]])

(defn market-chooser-dialog []
  (let [{:keys [event]}  @(rf/subscribe [::p.sub/ui-market-chooser])]
    [ui.c/modal {:show?_ (rf/subscribe [::p.sub/ui-market-chooser])
                 :close  #(rf/dispatch [::p.evt/ui-hide-market-chooser])
                 :title  "wybierz sklep"}
     [:div {:class "flex justify-between"}
      (for [market-id ["f01450" "f01451" "f01752"]]
        ^{:key market-id}
        [:div {:class    "px-2 text-nord-4 text-lg cursor-pointer hover:text-nord-7 transition duration-150"
               :on-click #(do (rf/dispatch [::p.evt/ui-hide-market-chooser])
                              (rf/dispatch (conj event market-id)))}
         market-id])]]))

(defn container [& children]
  (into
   [:div {:class "flex-1 overflow-y-scroll p-4"}]
   children))

(defn transfer-date-component [_id _payment-date _transfer-date]
  (let [input?_ (r/atom false)
        hover?  (r/atom false)
        tmp-date (r/atom _transfer-date)]
    (fn [id payment-date transfer-date]
      (let [paid-after-deadline? (and transfer-date
                                      (dt/isAfter
                                       (js/Date. transfer-date)
                                       (js/Date. payment-date)))]
        (if @input?_
          [:input {:class     "w-full bg-nord-4 focus:!ring-nord-7 focus:!border-nord-7"
                   :id        (str id "-transfer-date")
                   :type      :date
                   :value     transfer-date
                   :on-click  (fn [e] (.stopPropagation e))
                   :on-change #(do
                                 (rf/dispatch [::p.evt/set-transfer-date id (-> % .-target .-value)])
                                 (-> (j/call js/document :getElementById (str id "-transfer-date"))
                                     (j/call :blur)))
                   :on-blur   #(reset! input?_ false)}]
          [:div {:class          "w-full flex cursor-pointer"
                 :on-mouse-enter #(reset! hover? true)
                 :on-mouse-leave #(reset! hover? false)}
           [:> CSSTransition {:in              (some? transfer-date)
                              :timeout         300
                              :unmount-on-exit true
                              :class-names     {:enter        "hidden opacity-0"
                                                :enter-active "transition-opacity delay-150 opacity-100"
                                                :exit         "opacity-100"
                                                :exit-active  "transition-opacity opacity-5"}}
            [:div {:class    ["relative flex flex-row items-center transition-colors" (when paid-after-deadline? "text-nord-11")]
                   :on-click (fn [^js e]
                               (.stopPropagation e)
                               (reset! input?_ true)
                               (js/setTimeout (fn []
                                                (-> (j/call js/document :getElementById (str id "-transfer-date"))
                                                    (j/call :focus)))
                                              100))}
             [:div
              (or (str transfer-date) "0000-00-00")]
             [:> x-icon
              {:class    ["absolute w-4 -right-5 transition-opacity"
                          "hover:text-nord-11"
                          (if @hover? "opacity-100" "opacity-0")]
               :on-click (fn [^js e]
                           (.stopPropagation e)
                           (rf/dispatch [::p.evt/set-transfer-date id nil]))}]]]
           [:> CSSTransition {:in              (nil? transfer-date)
                              :timeout         150
                              :unmount-on-exit true
                              :class-names     {:enter        "hidden opacity-0"
                                                :enter-active "transition-opacity opacity-100"
                                                :exit         "opacity-100"
                                                :exit-active  "transition-opacity opacity-5"}}
            [:div {:class    ["flex-1 transition-all text-nord-1"]
                   :on-click (fn [^js e]
                               (.stopPropagation e)
                               (rf/dispatch [::p.evt/set-transfer-date id :now]))}
             "nie opłacone"]]])))))

(defn header-filter [{:keys [_header _evt sub]}]
  (let [value  (rf/subscribe sub)
        focus? (r/atom false)
        mouse-inside? (r/atom false)
        ref (atom nil)]
    (fn [{:keys [header evt _sub]}]
      [:div {:class "relative flex mx-4 mt-4 mb-2 flex-1 flex-col"}
       [:div {:class          ["absolute px-1 min-h-[16px] transition-all"
                               (if (or @mouse-inside? (seq @value) @focus?) "-top-5 left-1 text-xs" "top-1 left-2.5")]
              :on-mouse-enter #(reset! mouse-inside? true)
              :on-mouse-leave #(reset! mouse-inside? false)}
        header]
       [:div {:class "flex flex-row w-full items-center"}
        [:input {:class          ["flex-2 py-1 pr-4 w-full bg-nord-4 rounded focus:outline-none focus:shadow-outline focus:!ring-nord-7 focus:!border-nord-7 truncate"]
                 :type           :text
                 :on-focus       #(reset! focus?        true)
                 :on-blur        #(reset! focus?        false)
                 :on-mouse-enter #(reset! mouse-inside? true)
                 :on-mouse-leave #(reset! mouse-inside? false)
                 :value          @value
                 :on-key-down    (fn [^js e] (when (= (.-key e) "Escape") (.blur ^js @ref)))
                 :on-change      #(rf/dispatch (conj evt (-> % .-target .-value)))
                 :ref            (fn [e] (reset! ref e))}]
        (when (seq @value)
          [:> x-icon
           {:class    "absolute w-4 right-2 text-nord-3 hover:text-nord-11 cursor-pointer transition-all duration-150 ease-in-out"
            :on-click #(rf/dispatch (conj evt ""))}])]])))

(defn table-row [{:keys [document/id] :as doc}]
  (let [document-selected?   @(rf/subscribe [::p.sub/document-selected? id])
        after-deadline?      (and (empty? (:document/transfer-date doc))
                             (dt/isAfter
                              (js/Date.)
                              (js/Date. (:document/payment-date doc))))]
    [:div {:class ["flex h-full w-full transition duration-150 ease-in-out cursor-pointer"
                   (if document-selected? "bg-nord-9 hover:bg-nord-8" "hover:bg-nord-5")]
           :on-click #(rf/dispatch [::p.evt/toggle-document id])}
     (let [market-id (get-in doc [:document/market :market/id])]
       [:div {:class "flex-2 self-center truncate"}
        [:div {:class    "mx-5 px-2 text-left lowercase cursor-pointer"
               :on-click (fn [^js e]
                           (.stopPropagation e)
                           (rf/dispatch [::p.evt/ui-set-market-query (name market-id) :ms 200]))}
         market-id]])
     (let [name (get-in doc [:document/contractor :contractor/name])]
       [:div {:class "flex-2 self-center truncate"}
        [:div {:class    "mx-5 px-2 text-left lowercase cursor-pointer"
               :on-click (fn [^js e]
                           (.stopPropagation e)
                           (rf/dispatch [::p.evt/ui-set-contractor-query name :ms 200]))}
         name]])
     (let [nip (get-in doc [:document/contractor :contractor/id])]
       [:div {:class "flex-3 self-center truncate"}
        [:div {:class    "mx-5 px-2 text-left lowercase cursor-pointer"
               :on-click (fn [^js e]
                           (.stopPropagation e)
                           (rf/dispatch [::p.evt/ui-set-nip-query nip :ms 200]))}
         nip]])
     [:div {:class "flex-3 self-center truncate"}
      [:div {:class "mx-5 px-2 text-left lowercase cursor-pointer"}
       (first id)]]
     [:div {:class "flex-2 self-center truncate"}
      [:div {:class "mx-5 px-2 text-right lowercase cursor-pointer"}
       (some-> (:document/net-value doc) (j/call :toFixed 2))]]
     [:div {:class "flex-2 self-center truncate"}
      [:div {:class "mx-5 px-2 text-right lowercase cursor-pointer"}
       (some-> (:document/gross-value doc) (j/call :toFixed 2))]]
     (let [date (:document/accounting-date doc)]
       [:div {:class "flex-2 self-center truncate"}
        [:div {:class "mx-5 px-2 text-left lowercase cursor-pointer"
               :on-click (fn [^js e]
                           (.stopPropagation e)
                           (rf/dispatch [::p.evt/ui-set-accounting-date-query date :ms 200]))}
         date]])
     (let [date (:document/payment-date doc)]
       [:div {:class ["flex-2 self-center truncate transition-colors"
                      (when after-deadline? "text-nord-11")]}
        [:div {:class "mx-5 px-2 text-left lowercase cursor-pointer"
               :on-click (fn [^js e]
                           (.stopPropagation e)
                           (rf/dispatch [::p.evt/ui-set-payment-date-query date :ms 200]))}
         date]])
     [:div {:class ["flex-2 self-center"]}
      [:div {:class "mx-5 px-2"}
       [transfer-date-component (:document/id doc) (:document/payment-date doc) (:document/transfer-date doc)]]]]))

(defn table-header []
  [:div
   [:div {:class "flex border-b border-nord-3"}
    [:div {:class "flex-2"}
     [header-filter
      {:header "market"
       :sub    [::p.sub/ui-market-query-tmp]
       :evt    [::p.evt/ui-set-market-query]}]]
    [:div {:class "flex-2"}
     [header-filter
      {:header "firma"
       :sub    [::p.sub/ui-contractor-query-tmp]
       :evt    [::p.evt/ui-set-contractor-query]}]]
    [:div {:class "flex-3"}
     [header-filter
      {:header "nip"
       :sub    [::p.sub/ui-nip-query-tmp]
       :evt    [::p.evt/ui-set-nip-query]}]]
    [:div {:class "flex-3"}
     [header-filter
      {:header "id"
       :sub    [::p.sub/ui-document-query-tmp]
       :evt    [::p.evt/ui-set-document-query]}]]
    [:div {:class "flex-2"}
     [header-filter
      {:header "netto"
       :align  :text-right
       :sub    [::p.sub/ui-net-value-query-tmp]
       :evt    [::p.evt/ui-set-net-value-query]}]]
    [:div {:class "flex-2"}
     [header-filter
      {:header "brutto"
       :class  :text-right
       :sub    [::p.sub/ui-gross-value-query-tmp]
       :evt    [::p.evt/ui-set-gross-value-query]}]]
    [:div {:class "flex-2"}
     [header-filter
      {:header "data"
       :sub    [::p.sub/ui-accounting-date-query-tmp]
       :evt    [::p.evt/ui-set-accounting-date-query]}]]
    [:div {:class "flex-2"}
     [header-filter
      {:header "termin"
       :sub    [::p.sub/ui-payment-date-query-tmp]
       :evt    [::p.evt/ui-set-payment-date-query]}]]
    [:div {:class "flex-2"}
     [header-filter
      {:header "płatność"
       :sub [::p.sub/ui-transfer-date-query-tmp]
       :evt [::p.evt/ui-set-transfer-date-query]}]]]])

(defn table []
  (let [show-modal?        (r/atom false)
        document-info      (r/atom nil)
        scrollbar-visible? (r/atom false)
        documents          (rf/subscribe [::p.sub/documents-filtered])]
    (fn []
      (let [data (sort-by :document/payment-date @documents)]
        [:div {:class "flex flex-col flex-1 px-4 pt-4 pb-1"}
         [:div {:class "mr-[15px]"} ;; scrollbar fix
          [table-header]]
         [:div {:class ["flex-1 border-b border-nord-3"]}
          [:> auto-sizer
           (fn [sizer]
             (reset! scrollbar-visible? (< (j/get sizer :height) (* 48 (count data))))
             (r/as-element
              [:> virtual-list
               {:class       ["focus:outline-none !overflow-y-scroll"]
                :height      (j/get sizer :height)
                :width       (j/get sizer :width)
                :rowHeight   48
                :rowCount    (count data)
                :rowRenderer (fn [m]
                               (r/as-element
                                ^{:key (j/get m :key)}
                                [:div {:style (j/get m :style)}
                                 [table-row (nth data (j/get m :index))]]))}]))]]]))))

(defn selected-document-summary []
  (let [selected-documents (rf/subscribe [::p.sub/selected-documents-ids])
        gross-value        (rf/subscribe [::p.sub/selected-documents-gross-value])
        number-state       (reaction (if (even? (count @selected-documents)) 0 1))]
    (fn []
      [:div {:class "flex overflow-hidden"}
       [:div {:class "px-8 w-full"}
        [:div {:class "flex flex-row items-center"}
         [:div {:class "flex flex-col overflow-x-hidden"}
          [:div {:class "flex w-full text-sm"}
           [:div {:class    "flex flex-1 self-center truncate select-none cursor-pointer"
                  :on-click (fn [_e]
                              (js/navigator.clipboard.writeText (str/join " " (mapv first @selected-documents)))
                              (rf/dispatch [:mannheim.ui.events/add-notification ::copy-to-clipboard
                                            {:content "skopiowano do schowka"}]))}
            [:div {:class "flex-shrink-0 w-28"}
             "tytuł przelewu:"]
            [:> TransitionGroup {:class "flex"}
             (for [[doc _] @selected-documents]
               ^{:key doc}
               [:> CSSTransition {:in              true
                                  :timeout         150
                                  :unmount-on-exit true
                                  :class-names     {:enter        "max-w-[0px] opacity-0"
                                                    :enter-active "transition-all max-w-[100px] opacity-100"
                                                    :exit         "max-w-[100px] opacity-100"
                                                    :exit-active  "transition-all !max-w-[0px] !opacity-0"}}
                [:div {:class "mr-2 truncate"}
                 doc]])]]]
          [:div {:class "flex text-sm"}
           [:div {:class "flex-shrink-0 w-28"}
            "wartość brutto:"]
           [:div {:class "flex"}
            [:> SwitchTransition
             [:> CSSTransition {:key         (if (zero? @number-state) :a :b)
                                :timeout     75
                                :unmount-on-exit true
                                :class-names {:enter        "max-w-[0px]"
                                              :enter-active "transition-all duration-75 max-w-[100px]"
                                              :exit         "max-w-[100px]"
                                              :exit-active  "transition-all duration-75 !ml-0 !max-w-[0px]"}}
              [:div {:class    "select-none cursor-pointer truncate"
                     :on-click (fn [_e]
                                 (js/navigator.clipboard.writeText gross-value)
                                 (rf/dispatch [:mannheim.ui.events/add-notification ::copy-to-clipboard
                                               {:content "skopiowano do schowka"}]))}
               (enc/format "%.2f zł" @gross-value)]]]]]]
         [:div {:class "flex flex-1 justify-end mx-4 space-x-6"}
          [:> Check
           {:class "text-nord-3 hover:text-nord-14 cursor-pointer transition-all duration-150"}]
          [:> x-icon
           {:class    "text-nord-3 hover:text-nord-11 cursor-pointer transition-all duration-150 ease-in-out"
            :on-click #(rf/dispatch [::p.evt/reset-document-selection])}]]]]])))

(defn footer-bar []
  (let [cnt         @(rf/subscribe [::p.sub/documents-filtered-count])
        net-value   @(rf/subscribe [::p.sub/documents-filtered-net-value])
        gross-value @(rf/subscribe [::p.sub/documents-filtered-gross-value])]
    [:div {:class "px-6 min-w-1 text-sm flex flex-row justify-end"}
     [:div {:class "px-2"}
      (enc/format "ilość: %s" cnt)]
     [:div {:class "px-2"}
      (enc/format "netto: %s" net-value)]
     [:div {:class "px-2"}
      (enc/format "brutto: %s" gross-value)]]))

(defn content []
  (let [selected-documents @(rf/subscribe [::p.sub/selected-documents-ids])]
    [:div {:class "flex flex-col overflow-hidden"}
     [table]
     [:> CSSTransition {:in              (enc/some? selected-documents)
                        :timeout         450
                        :unmount-on-exit true
                        :class-names {:enter        "max-h-[1px] opacity-0"
                                      :enter-active "transition-all delay-150 max-h-[100px] py-2 opacity-100"
                                      :enter-done   "py-2"
                                      :exit         "py-2 max-h-[100px] opacity-100"
                                      :exit-active  "transition-all !max-h-[1px] !py-0 opacity-5"}}
      [selected-document-summary]]
     [:> CSSTransition {:in              (not (enc/some? selected-documents))
                        :timeout         450
                        :unmount-on-exit true
                        :class-names     {:enter        "max-h-[1px] opacity-0"
                                          :enter-active "transition-all delay-150 max-h-[100px] py-2 opacity-100"
                                          :enter-done   "py-2"
                                          :exit         "py-2 max-h-[100px] opacity-100"
                                          :exit-active  "transition-all !max-h-[1px] py-0 opacity-5"}}
      [footer-bar]]
     [market-chooser-dialog]]))
