(ns mannheim.dc-order.ui
  (:require
   ["react-virtualized/dist/commonjs/AutoSizer" :default auto-sizer]
   ["date-fns" :as dt]
   ["react-feather" :refer [ChevronLeft ArrowUp Trash2] :rename {X x-icon}]
   ["react-transition-group" :refer [Transition CSSTransition TransitionGroup]]
   ["react-virtualized/dist/commonjs/AutoSizer" :default auto-sizer]
   ["react-virtualized/dist/commonjs/List" :default virtual-list]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->js]]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [mannheim.init :as init]
   [mannheim.ui.components :as ui.c]
   [mannheim.dc-order.events :as dc-order.evt]
   [mannheim.dc-order.subs :as dc-order.sub]
   [mannheim.cg.events :as cg.evt]
   [meander.epsilon :as m]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.ratom :as ra]
   [taoensso.encore :as e]))

(defn market-select []
  (let [market-id @(rf/subscribe [::dc-order.sub/market-id])]
    [:select.mx-2.px-2.py-1.bg-nord-0.text-nord-4.focus:text-nord-4.hover:text-nord-7.outline-none.font-medium.w-32.text-center.rounded.shadow.cursor-pointer.border.border-gray-900.transition.duration-200
     {:value     (or (some-> market-id name) "")
      :on-change #(rf/dispatch [::dc-order.evt/set-market-id (-> % .-target .-value)])}
     (when-not market-id [:option "sklep"])
     [:option "f01450"]
     [:option "f01451"]
     [:option "f01752"]]))

(defn header []
  (let [market-id @(rf/subscribe [::dc-order.sub/market-id])]
    [:div.flex
     [market-select]
     [ui.c/button
      {:on-click #(rf/dispatch [::dc-order.evt/ui.show-make-order-dialog])}
      "generuj zamówienie"]
     [ui.c/button
      {:on-click #(rf/dispatch [::cg.evt/read-cg-file market-id])}
      "wczytaj cg"]
     ;; [ui.c/button
     ;;  {:on-click #(rf/dispatch [::dc-order.evt/read-re @market-id])}
     ;;  "wczytaj ecd"]
     [ui.c/button
      {:on-click #(rf/dispatch [::dc-order.evt/ui.show-settings])}
      "ustawienia"]]))

;;
;; * groups settings
;;


(defn settings-group-row [market-id gid name]
  (let [order-days @(rf/subscribe [::dc-order.sub/group-optimal-order-days market-id gid])]
    ^{:key gid}
    [:div.flex.pt-2
     [:div.w-3|5.truncate
      [:span.cursor-pointer
       {:on-click #(rf/dispatch [::dc-order.evt/ui.groups-settings.select-group gid])}
       name]]
     [:div.flex.w-1|5.justify-end
      [:input.w-8.bg-nord-4.outline-none.rounded.focus:shadow-outline
       {:default-value (or order-days 0)
        :type          :number
        :on-blur       #(rf/dispatch [::dc-order.evt/set-group-optimal-order-days
                                      market-id gid (-> % .-target .-value)])}]
      [:div "dni"]]
     [:div.w-1|5]]))

(defn settings-dialog []
  (let [market-id       @(rf/subscribe [::dc-order.sub/market-id])
        group-id        @(rf/subscribe [::dc-order.sub/ui.groups-settings.selected-group])
        group-name      @(rf/subscribe [::dc-order.sub/group-name group-id])
        children-groups @(rf/subscribe [::dc-order.sub/group-children (or group-id "")])]
    [ui.c/modal
     {:show? (rf/subscribe [::dc-order.sub/ui.show-settings?])
      :close #(rf/dispatch [::dc-order.evt/ui.hide-settings])
      :title (if (empty? group-id)
               [:div "opcje"]
               [:div.flex.items-center
                [:> ChevronLeft
                 {:class    [:transition :duration-200 :easy-in-out :hover:text-nord-7 :-ml-2]
                  :on-click #(rf/dispatch [::dc-order.evt/ui.groups-settings.select-group
                                           (e/get-substr group-id 0 (- (count group-id) 2))])}]
                [:div.mx-2.truncate
                 {:style {:max-width "300px"}}
                 group-name]])
      :bg    :bg-nord-4}
     [:div.flex.flex-col.h-64
      {:style {:min-width "400px"}}
      [:div.flex-1
       [:> auto-sizer
        (fn [sizer]
          (r/as-element
           [:div.min-w-full.overflow-y-scroll.pr-2
            {:style {:height (j/get sizer :height)
                     :width  (j/get sizer :width)}}
            [:div.flex.my-2.border-b.border-nord-3
             [:div.w-3|5 "nazwa"]
             [:div.w-1|5.text-right "zapas"]
             [:div.w-1|5 ""]]
            (doall
             (for [[gid name] children-groups]
               ^{:key gid}
               [settings-group-row market-id gid name]))]))]]]]))

;;
;; * order dialog
;;


(defn make-order-dialog-group-row [market-id gid name]
  (let [order-days @(rf/subscribe [::dc-order.sub/group-optimal-order-days market-id gid])
        enabled?   @(rf/subscribe [::dc-order.sub/make-order-dialog.group-enabled? market-id gid])]
    ^{:key gid}
    [:div.flex.pt-2.items-center
     [:div.w-3|5.truncate
      [:span.cursor-pointer
       {:on-click #(rf/dispatch [::dc-order.evt/make-order-dialog.toggle-group gid])}
       name]]
     [:div.flex.w-1|5.justify-end
      [:input.w-8.bg-nord-4.outline-none.rounded.focus:shadow-outline
       {:default-value (or order-days 0)
        :type          :number
        :on-blur       #(rf/dispatch [::dc-order.evt/set-group-optimal-order-days
                                      market-id gid (-> % .-target .-value)])}]
      [:div "dni"]]
     [:div.flex.w-1|5
      [:input.w-1|2.bg-nord-4.outline-none.rounded
       {:checked   (some? enabled?)
        :type      :checkbox
        :on-change #(rf/dispatch [::dc-order.evt/make-order-dialog.toggle-group market-id gid])}]]]))

(comment
  (rf/subscribe [::dc-order.sub/make-order-dialog.group-enabled? :f01752 "09"])
  @(rf/subscribe [::dc-order.sub/group-optimal-order-days :f01752 "09"])
  (some? nil)
  )

(defn make-order-dialog []
  (let [market-id       @(rf/subscribe [::dc-order.sub/market-id])
        group-id        @(rf/subscribe [::dc-order.sub/ui.make-order.selected-group])
        group-name      @(rf/subscribe [::dc-order.sub/group-name group-id])
        children-groups @(rf/subscribe [::dc-order.sub/group-children (or group-id "")])]
    [ui.c/modal
     {:show? (rf/subscribe [::dc-order.sub/ui.show-make-order-dialog?])
      :close #(rf/dispatch [::dc-order.evt/ui.hide-make-order-dialog])
      :title (if (empty? group-id)
               [:div "zamówienie"]
               [:div.flex.items-center
                [:> ChevronLeft
                 {:class    [:transition :duration-200 :easy-in-out :hover:text-nord-7 :-ml-2]
                  :on-click #(rf/dispatch [::dc-order.evt/ui.make-order.select-group
                                           (e/get-substr group-id 0 (- (count group-id) 2))])}]
                [:div.mx-2.truncate
                 {:style {:max-width "300px"}}
                 group-name]])
      :bg    :bg-nord-4}
     [:div.flex.flex-col.h-64
      {:style {:min-width "400px"}}
      [:div.flex-1
       [:> auto-sizer
        (fn [sizer]
          (r/as-element
           [:div.min-w-full.overflow-y-scroll.pr-2
            {:style {:height (j/get sizer :height)
                     :width  (j/get sizer :width)}}
            [:div.flex.my-2.border-b.border-nord-3
             [:div.w-3|5 "nazwa"]
             [:div.w-1|5.text-right "zapas"]
             [:div.w-1|5 ""]]
            (doall
             (for [[gid name] children-groups]
               ^{:key gid}
               [make-order-dialog-group-row market-id gid name]))]))]]
      [:div.border-t.border-nord-3.
       [:div.flex.mt-2.justify-end
        [ui.c/button
         {:class    [:bg-nord-3]
          :on-click #(rf/dispatch [::dc-order.evt/read-report-file market-id])}
         "generuj"]]]]]))

;;
;; table
;;


(defn table-row [product]
  (let [eid          (:db/id product)
        product-name (:dc.order/product-name product)
        product-ean  (:dc.order/product-ean product)
        stock        (:dc.order/stock product)
        demand       (:dc.order/demand product)
        supply-days  (:dc.order/supply product)
        order-qty    (:dc.order/order product)
        ec-price     (:dc.order/ec-price product)
        cg-price     (:dc.order/cg-price product)
        vendor       (:dc.order/vendor product)
        cg-stock     (:dc.order/cg-stock product)]
    [:div.flex.h-full.w-full.transition.duration-200.ease-in-out
     {:class [:hover:bg-nord-5]}
     [:div.flex-2.self-center.px-4.py-2.text-left.text-sm.truncate
      product-name]
     [:div.flex-1.self-center.px-4.py-2.text-left.text-sm.truncate.select-all
      product-ean]
     [:div.w-24.self-center.px-4.py-2.text-left.text-sm.truncate.text-right
      {:class (cond
                (neg? stock)     [:text-nord-11]
                (> demand stock) [:text-nord-3 :font-light])}
      stock]
     [:div.w-24.self-center.px-4.py-2.text-left.text-sm.truncate.text-right
      {:class (when (< demand stock) [:text-nord-3 :font-light])}
      demand]
     [:div.w-24.self-center.px-4.py-4.text-left.text-sm.text-right
      {:class (cond
                (> supply-days 100) :text-nord-11
                (> supply-days 30)  :text-nord-12
                (> supply-days 15)  :text-nord-15)}
      (str supply-days " dni")]
     [:div.w-24.self-center.px-4.py-4.text-sm.text-right
      [:input.h-full.w-full.text-right.bg-transparent.outline-none.focus:shadow-outline.rounded.font-bold
       {:value     order-qty
        :type      :number
        :on-change #(rf/dispatch [::dc-order.evt/set-order-qty eid (-> % .-target .-value js/parseInt)])}]]
     [:div.w-24.self-center.px-4.py-2.text-right.text-sm.text-right
      {:class (when (not= :ec vendor)
                [:text-nord-3 :font-light])}
      (m/match ec-price
        (m/or nil :missing) "brak"
        (m/and ?x (m/pred number? ?x)) (j/call ?x :toFixed 2))]
     [:div.w-24.self-center.px-4.py-2.text-right.text-sm.text-right
      {:class (when (not= :cg vendor)
                [:text-nord-3 :font-light])}
      (m/match cg-price
        (m/or nil :missing) "brak"
        (m/and ?x (m/pred number? ?x)) (j/call ?x :toFixed 2))]
     [:div.w-24.self-center.px-4.py-2.text-right.text-sm
      {:class (when (neg? cg-stock) :text-nord-11)}
      cg-stock]
     [:div.w-24.self-center.px-4.py-2.text-right.text-sm
      vendor]
     [:div.w-12.self-center.px-4.py-2.text-sm
      [:> Trash2
       {:class    [:w-4 :h-4 :hover:text-nord-11 :transition :duration-200 :ease-in-out :cursor-pointer]
        :on-click #(rf/dispatch [::dc-order.evt/remove-product eid])}]]]))

(m/find :text-right
  (m/or :text-right (m/scan :text-right)) true)

(defn header-filter [{:keys [evt sub class type]}]
  (let [value        @(rf/subscribe sub)
        align-right? (m/find class (m/or :text-right (m/scan :text-right)) true)
        input        [:input.flex-1.w-full.bg-nord-4.rounded.focus:outline-none.focus:shadow-outline.truncate
                      {:class       (flatten [class (when (not-empty value) :pr-6)
                                              (when align-right? (when (not-empty value) :-mr-4))])
                       :type        type
                       :placeholder "filtruj"
                       :value       value
                       :on-change   #(rf/dispatch-sync (conj evt (-> % .-target .-value)))}]
        icon         (when (seq value)
                       [:div.text-nord-3.hover:text-nord-11.cursor-pointer.transition-all.duration-200.ease-in-out
                        (when-not type
                          [:> x-icon
                           {:class    [:absolute :w-4 (if-not align-right? :-ml-5 :-ml-1)]
                            :on-click #(rf/dispatch (conj evt ""))}])])]
    [:div.flex
     [:<> input icon]]))

(defn table-header [{:keys [key text class]}]
  (let [[sort-dir sort-key] (or @(rf/subscribe [::dc-order.sub/ui.sort-order])
                                [:desc :dc.warehouse.report/product-name])]
    [:div.flex.items-center
     {:class    class
      :on-click (when key #(rf/dispatch [::dc-order.evt/ui.set-sort-order key]))}
     [:div.min-w-full text]
     (when (and key (= sort-key key))
       [:div
        [:> ArrowUp
         {:class [:w-3 :h-3 :ml-2 :mt-1 :transition-transform :duration-200 :ease-in-out]
          :style {:transform (if (= :desc sort-dir) "rotate(-180deg)")}}]])]))

(defn table-headers []
  (let [[sort-dir sort-key] (or @(rf/subscribe [::dc-order.sub/ui.sort-order])
                                [:asc :dc.order/product-name])]
    [:div
     [:div.flex.w-full
      [:div.flex-2.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key  :dc.order/product-name
                      :text "nazwa"}]]
      [:div.flex-1.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key  :dc.order/product-ean
                      :text "ean"}]]
      [:div.w-24.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key   :dc.order/stock
                      :text  "stan"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key   :dc.order/demand
                      :text  "optymalny"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key   :dc.order/supply
                      :text  "zapas"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key   :dc.order/order
                      :text  "ilość"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal
       [table-header {:text  "ec"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal
       [table-header {:text  "cg"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key   :dc.order/cg-stock
                      :text  "stan cg"
                      :class :text-right}]]
      [:div.w-24.px-4.py-2.font-normal.cursor-pointer
       [table-header {:key   :dc.order/vendor
                      :text  "dostawca"
                      :class :text-right}]]
      [:div.w-12.px-4.py-2.font-normal.cursor-pointer
       ""]]
     [:div.flex.border-b.border-nord-3.text-left
      [:div.flex-2.px-4.py-2.font-normal
       [header-filter
        {:sub [::dc-order.sub/ui.name-query-tmp]
         :evt [::dc-order.evt/ui.set-name-query]}]]
      [:div.flex-1.px-4.py-2.font-normal.text-left
       [header-filter
        {:sub [::dc-order.sub/ui.ean-query-tmp]
         :evt [::dc-order.evt/ui.set-ean-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.stock-query-tmp]
         :evt   [::dc-order.evt/ui.set-stock-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.supply-days-query-tmp]
         :evt   [::dc-order.evt/ui.set-supply-days-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.order-qty-query-tmp]
         :evt   [::dc-order.evt/ui.set-order-qty-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.demand-query-tmp]
         :evt   [::dc-order.evt/ui.set-demand-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.ec-query-tmp]
         :evt   [::dc-order.evt/ui.set-ec-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.cg-query-tmp]
         :evt   [::dc-order.evt/ui.set-cg-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.cg-stock-query-tmp]
         :evt   [::dc-order.evt/ui.set-cg-stock-query]}]]
      [:div.w-24.px-4.py-2.font-normal
       [header-filter
        {:class :text-right
         :sub   [::dc-order.sub/ui.vendor-query-tmp]
         :evt   [::dc-order.evt/ui.set-vendor-query]}]]
      [:div.w-12.px-4.py-2.font-normal
       ""]]]))

(defn table []
  (let [scrollbar-visible? (r/atom false)
        products-all_      (rf/subscribe [::dc-order.sub/order-products-sorted-pull])]
    (fn []
      (let [data @products-all_]
        [:div.flex.flex-col.flex-1.px-4.pt-4.pb-1
         [:div
          (when @scrollbar-visible? {:style {:margin-right "15px"}})
          ;; scrollbar fix
          [table-headers]]
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
                                 [table-row (nth data (j/get m :index))]]))}]))]]]))))

(defn content []
  [:div.flex.flex-1.flex-col
   [table]
   [settings-dialog]
   [make-order-dialog]])
