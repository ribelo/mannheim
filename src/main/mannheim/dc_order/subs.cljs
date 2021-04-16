(ns mannheim.dc-order.subs
  (:require
   [re-frame.core :as rf]
   [reagent.ratom :as ra]
   [datascript.core :as d]
   [cuerdas.core :as str]
   [taoensso.encore :as e]))

;;
;; * ui
;;

#_(rp/reg-query-sub
 ::ui.show-settings?
 '[:find ?v .
   :where [?e :dc.order.ui/show-settings? ?v]])

#_(rp/reg-query-sub
 ::ui.groups-settings.selected-group
 '[:find ?v .
   :where [?e :dc.order.ui.settings/selected-group ?v]])

#_(rp/reg-query-sub
 ::ui.show-make-order-dialog?
 '[:find ?v .
   :where [?e :dc.order.ui/show-make-order-dialog? ?v]])

#_(rp/reg-query-sub
 ::ui.make-order-dialog.selected-group
 '[:find ?v .
   :where [?e :dc.order.ui.make-order-dialog/selected-group ?v]])

#_(rp/reg-query-sub
 ::make-order-dialog.group-enabled?
 '[:find ?e .
   :in $ ?market-id ?gid
   :where
   [?e :dc.order.settings/market-id ?market-id]
   [?e :dc.order.settings/group-id ?gid]])

#_(rp/reg-query-sub
 ::ui.name-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/name-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.name-query
 '[:find ?v .
   :where [?e :dc.order.ui/name-query ?v]])

#_(rp/reg-query-sub
 ::ui.ean-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/ean-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.ean-query
 '[:find ?v .
   :where [?e :dc.order.ui/ean-query ?v]])

#_(rp/reg-query-sub
 ::ui.stock-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/stock-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.stock-query
 '[:find ?v .
   :where [?e :dc.order.ui/stock-query ?v]])

#_(rp/reg-query-sub
 ::ui.demand-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/demand-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.demand-query
 '[:find ?v .
   :where [?e :dc.order.ui/demand-query ?v]])

#_(rp/reg-query-sub
 ::ui.ec-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/ec-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.ec-query
 '[:find ?v .
   :where [?e :dc.order.ui/ec-query ?v]])

#_(rp/reg-query-sub
 ::ui.cg-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/cg-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.cg-query
 '[:find ?v .
   :where [?e :dc.order.ui/cg-query ?v]])

#_(rp/reg-query-sub
 ::ui.cg-stock-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/cg-stock-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.cg-stock-query
 '[:find ?v .
   :where [?e :dc.order.ui/cg-stock-query ?v]])


#_(rp/reg-query-sub
 ::ui.vendor-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/vendor-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.vendor-query
 '[:find ?v .
   :where [?e :dc.order.ui/vendor-query ?v]])

#_(rp/reg-query-sub
 ::ui.supply-days-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/supply-days-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.supply-days-query
 '[:find ?v .
   :where [?e :dc.order.ui/supply-days-query ?v]])

#_(rp/reg-query-sub
 ::ui.order-qty-query-tmp
 '[:find ?v .
   :where [?e :dc.order.ui/order-qty-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.order-qty-query
 '[:find ?v .
   :where [?e :dc.order.ui/order-qty-query ?v]])

#_(rp/reg-query-sub
 ::ui.sort-order
 '[:find ?v .
   :where [?e :dc.order.ui/sort-order ?v]])

#_(rp/reg-query-sub
 ::ui.make-order.selected-group
 '[:find ?v .
   :where [?e :dc.order.ui.make-order/selected-group ?v]])

#_(rp/reg-query-sub
 ::selected-products-ids
 '[:find [?v ...]
   :where [?e :dc.order.ui/selected-products ?v]])

#_(rp/reg-query-sub
 ::product-selected?
 '[:find ?id .
   :in $ ?id
   :where
   [?e :dc.order.ui/selected-products ?id]])

;;
;; * groups
;;


#_(rp/reg-query-sub
 ::groups-eids-by-level
 '[:find [?e ...]
   :in $ ?level
   :where
   [(inc ?level) ?level]
   [(* 2 ?level) ?level]
   [?e :dc.warehouse.groups/group-id ?gid]
   [(count ?gid) ?count]
   [(= ?count ?level)]])

#_(rp/reg-query-sub
 ::group-name
 '[:find ?v .
   :in $ ?gid
   :where
   [?e :dc.warehouse.groups/group-id ?gid]
   [?e :dc.warehouse.groups/group-name ?v]])

#_(rp/reg-query-sub
 ::group-children
 '[:find ?gid ?name
   :in $ ?parent
   :where
   [(count ?parent) ?parent-count]
   [(+ 2 ?parent-count) ?children-count]
   [?e :dc.warehouse.groups/group-id ?gid]
   [?e :dc.warehouse.groups/group-name ?name]
   [(count ?gid) ?group-count]
   [(= ?group-count ?children-count)]
   [(clojure.string/starts-with? ?gid ?parent)]])

#_(rp/reg-query-sub
 ::group-optimal-order-days
 '[:find ?v .
   :in $ ?market-id ?gid
   :where
   [?e :dc.warehouse.groups/market-id ?market-id]
   [?e :dc.warehouse.groups/group-id ?gid]
   [?e :dc.warehouse.groups/group-optimal-order-days ?v]])

;;
;; * order
;;

#_(rp/reg-query-sub
 ::market-id
 '[:find ?v .
   :where [?e :dc-order/market-id ?v]])

#_(rp/reg-sub
 ::order-products-eids-all
 :<- [::market-id]
 (fn [market-id]
   {:type      :query
    :variables [market-id]
    :query     '[:find [?e ...]
                 :in $ ?market-id
                 :where
                 [?e :dc.order/market-id ?market-id]
                 [?e :dc.order/show? true]]}))

#_(def ^:private fn-map
  {">"  >
   "<"  <
   "="  =
   ">=" >=
   "<=" <=})

#_(defn- number->query [s]
  (if (not-empty s)
    (as-> s s
      (str/split s " ")
      (partition-all 2 s)
      (mapcat (fn [coll]
                (if (= (count coll) 2)
                  [(get fn-map (first coll) =) (or (e/as-?float (second coll)) 0.00)]
                  [= (or (e/as-?float (first coll)) 0.00)])) s))
    [(constantly true) js/NaN]))

#_(rp/reg-sub
 ::product-pull
 (fn [_ [_ eid]]
   {:type    :pull
    :pattern '[*]
    :id      eid}))

#_(rf/reg-sub-raw
 ::order-products-sorted-pull
 (fn [_ _]
   (ra/reaction
    (let [eids     @(rf/subscribe [::order-products-eids-all])
          products @(p/pull-many @re-posh.db/store '[*] eids)
          [dir k]  @(rf/subscribe [::ui.sort-order])]
      (case dir
        :asc  (sort-by (or k :dc.order/product-name) products)
        :desc (sort-by (or k :dc.order/product-name) e/rcompare products))))))

#_(comment
  (rf/dispatch [:mannheim.dc-order.events/ui.set-sort-order :dc.order/product-name])
  @(rf/subscribe [::order-products-sorted-pull])

  (p/q '[:find [?e ...]
         :where [?e :dc.order/market-id ?v]]
       @re-posh.db/store)

  (rf/subscribe [::ui.sort-order])
  (rf/clear-subscription-cache!)
  (rf/dispatch [:mannheim.dc-order.events/ui.set-cg-stock-query "> 0"])
  (rf/dispatch [:mannheim.dc-order.events/ui.set-stock-query ""])
  (count @(rf/subscribe [::products-filtered-pull]))

  (->
    (d/q '[:find [(pull ?e [*]) ...]
           :in $ ?market-id
           :where
           [?e :dc.order/market-id ?market-id]
           ;; [?e :dc.order/show? true]
           ]
         @@re-posh.db/store :f01451)
    (first))
  (+ 1 1)
  )
