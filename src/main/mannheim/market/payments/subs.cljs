(ns mannheim.market.payments.subs
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [reagent.ratom :as ra :refer [reaction]]
   [taoensso.encore :as enc]
   [meander.epsilon :as m]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [mannheim.electron.core :as el]
   [net.cgrand.xforms :as x]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [applied-science.js-interop :as j]
   [mannheim.market.payments.utils :as u]
   [ribelo.doxa :as dx]
   [ribelo.danzig :as dz :refer [=>>]]
   [ribelo.kemnath :as math]))

;;
;; * ui
;;
(rf/reg-sub-raw
 ::ui-market-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :market-query-tmp] "")))))

(comment
  (rf/subscribe [::ui-market-query-tmp]))

(rf/reg-sub-raw
 ::ui-market-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :market-query] "")))))

(rf/reg-sub-raw
 ::ui-contractor-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :contractor-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-contractor-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :contractor-query] "")))))

(rf/reg-sub-raw
 ::ui-nip-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :nip-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-nip-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :nip-query] "")))))

(rf/reg-sub-raw
 ::ui-document-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :document-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-document-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :document-query] "")))))

(rf/reg-sub-raw
 ::ui-net-value-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :net-value-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-net-value-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :net-value-query] "")))))

(rf/reg-sub-raw
 ::ui-gross-value-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :gross-value-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-gross-value-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :gross-value-query] "")))))

(rf/reg-sub-raw
 ::ui-accounting-date-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :accounting-date-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-accounting-date-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :accounting-date-query] "")))))

(rf/reg-sub-raw
 ::ui-payment-date-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :payment-date-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-payment-date-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :payment-date-query] "")))))

(rf/reg-sub-raw
 ::ui-transfer-date-query-tmp
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :transfer-date-query-tmp] "")))))

(rf/reg-sub-raw
 ::ui-transfer-date-query
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :transfer-date-query] "")))))

(rf/reg-sub-raw
 ::ui-transfer-filter
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :transfer-filter] "")))))

(rf/reg-sub-raw
 ::ui-market-chooser
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market.payments/market-chooser])))))

(rf/reg-sub-raw
 ::documents-eids
 (fn [_ _]
   (dx/with-dx [dx_ :market]
     (reaction
      (dx/q [:find [?e ...]
             :where
             [?e :document/id ?id]]
        @dx_)))))

(rf/reg-sub-raw
 ::document-pull
 (fn [_ [_ eid]]
   (dx/with-dx [dx_ :market]
     (reaction
      (dx/pull @dx_ [:*
                     {:document/contractor [:contractor/id :contractor/name]}
                     {:document/market [:market/id]}]
               [:document/id eid])))))

(rf/reg-sub-raw
 ::contractor-pull
 (fn [_ [_ ident]]
   (dx/with-dx [dx_ :market]
     (reaction
      (get-in @dx_ ident)))))

(comment
  @(rf/subscribe [::contractor-pull [ :contractor/id "7791906082"]])
  (rf/clear-subscription-cache!)
  )

(rf/reg-sub-raw
 ::transfer-date
 (fn [_ [_ eid]]
   (dx/with-dx [dx_ :market]
     (get-in @dx_ [:document/id eid :document/transfer-date]))))

#_(rp/reg-sub
 ::documents-filtered-eids
 :<- [::ui.market-query]
 :<- [::ui.contractor-query]
 :<- [::ui.nip-query]
 :<- [::ui.document-query]
 :<- [::ui.net-value-query]
 :<- [::ui.gross-value-query]
 :<- [::ui.accounting-date-query]
 :<- [::ui.payment-date-query]
 :<- [::ui.transfer-date-query]
 :<- [::ui.transfer-filter]
 (fn [[market contractor nip document net-value gross-value
      accounting payment transfer transfer-filter] _]
   (let [net-value-parsed   (u/parse-document-value net-value)
         gross-value-parsed (u/parse-document-value gross-value)
         transfer-date-pred (u/transfer-date->pred transfer transfer-filter)]
     {:type      :query
      :variables [[market contractor nip document net-value-parsed gross-value-parsed
                   accounting payment transfer-date-pred]]
      :query
      (if (every? nil? [market contractor document net-value gross-value
                        accounting payment transfer transfer-filter])
        '[:find [?e ...]
          :where [?e :document/id _]]
        '[:find [?e ...]
          :in $ [?q-market-id ?q-contractor ?q-nip ?q-document
                 [[?fn-net-value ?q-net-value]]
                 [[?fn-gross-value ?q-gross-value]]
                 ?q-accounting-date ?q-payment-date ?transfer-pred]
          :where
          ;; filter market
          [(str ?q-market-id) ?q-market-id]
          [?e :document/market.id ?market-id]
          [(name ?market-id) ?market-id]
          [(re-pattern ?q-market-id) ?market-id-pattern]
          [(re-find ?market-id-pattern ?market-id)]
          ;; filter contractor
          [(str ?q-contractor) ?q-contractor]
          [?e :document/contractor.name ?contractor]
          [(re-pattern ?q-contractor) ?contractor-pattern]
          [(re-find ?contractor-pattern ?contractor)]
          ;; filter nip
          [(str ?q-nip) ?q-nip]
          [?e :document/contractor.nip ?nip]
          [(re-pattern ?q-nip) ?nip-pattern]
          [(re-find ?nip-pattern ?nip)]
          ;; filter document
          [(str ?q-document) ?q-document]
          [?e :document/id ?document]
          [(re-pattern ?q-document) ?document-pattern]
          [(re-find ?document-pattern ?document)]
          ;; filter net-value
          [?e :document/net-value ?net-value]
          [(?fn-net-value ?net-value ?q-net-value)]
          ;; filter gross-value
          [?e :document/gross-value ?gross-value]
          [(?fn-gross-value ?gross-value ?q-gross-value)]
          ;; filter accounting-date
          [(str ?q-accounting-date) ?q-accounting-date]
          [?e :document/issue-date ?accounting-date]
          [(re-pattern ?q-accounting-date) ?accounting-pattern]
          [(re-find ?accounting-pattern ?accounting-date)]
          ;; filter payment-date
          [(str ?q-payment-date) ?q-payment-date]
          [?e :document/payment-date ?payment-date]
          [(re-pattern ?q-payment-date) ?payment-pattern]
          [(re-find ?payment-pattern ?payment-date)]
          ;; filter transfer-date
          [(get-else $ ?e :document/transfer-date "") ?transfer-date]
          [(?transfer-pred ?transfer-date)]])})))

(comment
  (def r1 (r/atom 1))
  (def r2 (r/atom 10))
  (def tmp (reaction
            (println (str "sex " @r1 " " @r2))
            (str "sex " @r1 " " @r2)))
  (m/match "2019-12-10"
    (m/re #"20.*") true)
  @tmp
  )

(rf/reg-sub-raw
 ::documents-filtered
 (fn [_ _]
   (dx/with-dx [dx_ :market]
     (reaction
      (let [contractor-name  @(rf/subscribe [::ui-contractor-query])
            contractor-re    (re-pattern (enc/format "(?i).*%s.*" contractor-name))
            market-name      @(rf/subscribe [::ui-market-query])
            market-re        (re-pattern (enc/format "(?i).*%s.*" market-name))
            contractor-id    @(rf/subscribe [::ui-nip-query])
            contractor-id-re (re-pattern (enc/format "(?i).*%s.*" contractor-id))
            net-value        @(rf/subscribe [::ui-net-value-query])
            net-value-re     (re-pattern (enc/format "%s.*" net-value))
            gross-value      @(rf/subscribe [::ui-gross-value-query])
            gross-value-re   (re-pattern (enc/format "%s.*" gross-value))
            acc-date         @(rf/subscribe [::ui-accounting-date-query])
            acc-date-re      (re-pattern (enc/format "%s.*" acc-date))
            payment-date     @(rf/subscribe [::ui-payment-date-query])
            payment-date-re  (re-pattern (enc/format "%s.*" payment-date))
            transfer-date    @(rf/subscribe [::ui-transfer-date-query])
            transfer-date-re (re-pattern (enc/format "%s.*" transfer-date))]
        (m/rewrites @dx_
          (m/and {:document/id {?e {:document/id              ?ident
                                    :document/market          [[?mtable (m/app name (m/re market-re))]]
                                    :document/contractor      [[?ctable (m/re contractor-id-re ?c)]]
                                    :document/net-value       (m/app str (m/re net-value-re ?net))
                                    :document/gross-value     (m/app str (m/re gross-value-re ?gross))
                                    :document/accounting-date (m/re acc-date-re)
                                    :document/payment-date    (m/re payment-date-re)
                                    :document/transfer-date   (m/or (m/some ?tdate) (m/and nil (m/let [?tdate "nie opłacono"])))
                                    :as                       ?m}}
                  ?ctable      {?c {:contractor/name (m/re contractor-re)}}}
                 (m/guard (re-find transfer-date-re ?tdate)))
          ~(dx/denormalize @dx_ ?m)))))))

(comment
  (dx/with-dx [dx_ :market]
    (keys @dx_))
  (rf/clear-subscription-cache!)
  (first @(rf/subscribe [::documents-filtered])))

(rf/reg-sub-raw
 ::documents-filtered-count
 (fn [_ _]
   (reaction (count @(rf/subscribe [::documents-filtered])))))

(rf/reg-sub-raw
 ::documents-filtered-net-value
 (fn [_ _]
   (reaction (math/round2 (transduce (map :document/net-value) + @(rf/subscribe [::documents-filtered]))))))

(rf/reg-sub-raw
 ::documents-filtered-gross-value
 (fn [_ _]
   (reaction (math/round2 (transduce (map :document/gross-value) + @(rf/subscribe [::documents-filtered]))))))

(comment
  (m/rewrite "546.00"
    (m/re (re-pattern (str 4 ".*")) ?x) ?x)

  (rf/clear-subscription-cache!)
  (let [v nil]
    (m/rewrites {:val 726.00}
      {:val (m/pred #(or (= v %) (nil? v)) ?v)} true))

  (dx/with-dx [dx_ :market]
    (first (:document/id @dx_)))
  @dx/dxs_
  (count
   (dx/with-dx [dx_ :market]
     (m/search @dx_
       {:document/id {?e {:document/market-id (m/or nil (m/re "sex" ?market-id))}}
        ?table       {?contractor {:contractor/name ?name}}}
       ?name)))
  (rf/clear-subscription-cache!)
  @(rf/subscribe [::documents-filtered-idents]))


(rf/reg-sub-raw
 ::selected-documents-ids
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui.market/payments :selected-documents])))))

(comment
  (dx/with-dx [dx_ :app]
     @dx_))

(rf/reg-sub-raw
 ::document-selected?
 (fn [_ [_ eid]]
   (dx/with-dx [dx_ :app]
     (reaction
      (m/find @dx_
        {:app/id {:ui.market/payments {:selected-documents (m/scan ~eid)}}}
        true
        _ false)))))


#_(rp/reg-query-sub
   ::selected-documents-gross-value
   '[:find (sum ?v) .
     :where
     [_ :payments.ui/selected-documents ?id]
     [?e :document/id ?id]
     [?e :document/gross-value ?v]])


(rf/reg-sub-raw
 ::selected-documents-gross-value
 (fn [_ _]
   (dx/with-dx [dx_ :market]
     (reaction
      (when-let [ids @(rf/subscribe [::selected-documents-ids])]
        (->> (mapv (fn [eid] [:document/id eid]) ids)
             (dx/pull-value @dx_ [:document/gross-value])
             (reduce +)
             (math/round2)))))))

(comment
  (rf/clear-subscription-cache!))

#_(comment
    (rf/clear-subscription-cache!)
    @(rf/subscribe [::selected-documents-ids])
    @(rf/subscribe [::selected-documents-gross-value])

    (let [docs @(rf/subscribe [::selected-documents-ids])]
      (d/q '[:find (sum ?v) .
             :in $ [?id ...]
             :where
             [?e :document/id ?id]
             [?e :document/gross-value ?v]
             ]
        @@re-posh.db/store
        docs))


    (d/q '[:find (sum ?v) .
           :where
           [_ :payments.ui/selected-documents ?id]
           [?e :document/id ?id]
           [?e :document/gross-value ?v]]
      @@re-posh.db/store)

    )
