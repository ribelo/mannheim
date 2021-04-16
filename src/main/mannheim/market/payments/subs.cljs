(ns mannheim.market.payments.subs
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as e]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [mannheim.electron.core :as el]
   [net.cgrand.xforms :as x]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [applied-science.js-interop :as j]
   [mannheim.market.payments.utils :as u]))

;;
;; ui
;;

#_(rp/reg-query-sub
 ::ui.market-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/market-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.market-query
 '[:find ?v .
   :where [?e :payments.ui/market-query ?v]])

#_(rp/reg-query-sub
 ::ui.contractor-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/contractor-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.contractor-query
 '[:find ?v .
   :where [?e :payments.ui/contractor-query ?v]])

#_(rp/reg-query-sub
 ::ui.nip-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/nip-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.nip-query
 '[:find ?v .
   :where [?e :payments.ui/nip-query ?v]])


#_(rp/reg-query-sub
 ::ui.document-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/document-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.document-query
 '[:find ?v .
   :where [?e :payments.ui/document-query ?v]])

#_(rp/reg-query-sub
 ::ui.net-value-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/net-value-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.net-value-query
 '[:find ?v .
   :where [?e :payments.ui/net-value-query ?v]])

#_(rp/reg-query-sub
 ::ui.gross-value-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/gross-value-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.gross-value-query
 '[:find ?v .
   :where [?e :payments.ui/gross-value-query ?v]])

#_(rp/reg-query-sub
 ::ui.accounting-date-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/accounting-date-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.accounting-date-query
 '[:find ?v .
   :where [?e :payments.ui/accounting-date-query ?v]])

#_(rp/reg-query-sub
 ::ui.payment-date-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/payment-date-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.payment-date-query
 '[:find ?v .
   :where [?e :payments.ui/payment-date-query ?v]])

#_(rp/reg-query-sub
 ::ui.transfer-date-query-tmp
 '[:find ?v .
   :where [?e :payments.ui/transfer-date-query-tmp ?v]])

#_(rp/reg-query-sub
 ::ui.transfer-date-query
 '[:find ?v .
   :where [?e :payments.ui/transfer-date-query ?v]])

#_(rp/reg-query-sub
 ::ui.transfer-filter
 '[:find ?v .
   :where
   [?e :payments.ui/transfer-filter ?v]])

#_(rp/reg-query-sub
 ::ui.show-market-chooser?
 '[:find ?v .
   :where [?e :payments.ui/show-market-chooser? ?v]])

;;
;;
;;

#_(rp/reg-query-sub
 ::documents-eids
 '[:find [?e ...]
   :where [?e :document/id]])

#_(rp/reg-sub
 ::documents-all
 :<- [::documents-eids]
 (fn [eids _]
   {:type    :pull-many
    :pattern '[*]
    :ids     eids}))

#_(rp/reg-sub
 ::document-pull
 (fn [_ [_ eid]]
   {:type    :pull
    :pattern '[*]
    :id      eid}))

#_(rp/reg-query-sub
 ::transfer-date
 '[:find ?v .
   :in $ ?e
   :where [?e :document/transfer-date ?v]])

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

#_(rp/reg-sub
 ::documents-filtered
 :<- [::documents-filtered-eids]
 (fn [eids _]
   {:type    :pull-many
    :pattern '[*]
    :ids     eids}))

#_(rp/reg-query-sub
 ::selected-documents-ids
 '[:find [?v ...]
   :where [?e :payments.ui/selected-documents ?v]])

#_(rp/reg-query-sub
 ::document-selected?
 '[:find ?id .
   :in $ ?id
   :where
   [?e :payments.ui/selected-documents ?id]])

#_(rp/reg-query-sub
 ::selected-documents-gross-value
 '[:find (sum ?v) .
   :where
   [_ :payments.ui/selected-documents ?id]
   [?e :document/id ?id]
   [?e :document/gross-value ?v]])


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
