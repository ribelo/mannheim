(ns mannheim.market.specs
  (:require
   [taoensso.encore :as e]
   [meander.epsilon :as m]
   [malli.core :as mc]
   [malli.transform :as mt]
   [malli.transform :as transform]
   [mannheim.specs :refer [Date TaxId Ident EntityId]]))

(def DocumentId
  [:cat string? TaxId Date])

(def ^:private document-types
  {"Faktura VAT"                             :document.type/invoice
   "Faktura korygująca do faktury VAT"       :document.type/invoice-correction
   "Faktura kosztowa"                        :document.type/invoice-cost
   "PK (Faktura)"                            :document.type/invoice-pk
   "Faktura korygująca do faktury kosztowej" :document.type/invoice-cost.correction
   "Faktura VAT (sprzedaż pozakasowa)"       :document.type/invoice-sales})

(def DocumentType
  [:enum {:decode/string (fn [s] (document-types s))}
   :document.type/invoice
   :document.type/invoice-correction
   :document.type/invoice-cost
   :document.type/invoice-pk
   :document.type/invoice-cost.correction
   :document.type/invoice-sales])

(def Contractor
  [:map
   [:contractor/id   TaxId]
   [:contractor/name :string]])

(defn- -decode-document [doc]
  (m/rewrite doc
    {:document/issue-date      ?date
     :document/contractor-name ?name
     :document/contractor-id   ?cid
     :document/gross-value     ?gross
     :document/net-value       ?net
     :document/id              ?did
     :as                       ?m}
    {&                    ?m
     :document/contractor {:contractor/id ?cid :contractor/name ?name}
     :document/vat-value  ~(e/round2 (- ?gross ?net))
     :document/id         [?did ?cid ?date]}))

(def Document
  [:map {:decode/string (fn [doc] (-decode-document doc))}
   [:document/issue-date      {:optional true}
    [:maybe Date]]
   [:document/payment-date    {:optional true}
    [:maybe Date]]
   [:document/accounting-date {:optional true}
    [:maybe Date]]
   [:document/contractor      [:or Ident Contractor]]
   [:document/gross-value     :double]
   [:document/vat-value       :double]
   [:document/net-value       :double]
   [:document/type            DocumentType]
   [:document/id              DocumentId]
   [:document/transfer-date   {:optional true}
    [:maybe :string]]])

(def ->document (mc/decoder Document (mt/transformer mt/string-transformer mt/strip-extra-keys-transformer)))

(def document? (mc/validator Document))

(defn- -decode-pxec [doc]
  (m/rewrite doc
    {:document/id ?did
     :document/accounting-date ?date
     :as ?m}
    {& ?m
     :document/id [?did "7791906082" ?date]}))

(def PXEC
  [:map {:decode/string (fn [doc] (-decode-pxec doc))}
   [:pxec/id :string]
   [:document/id DocumentId]
   [:document/accounting-date Date]
   [:document/transfer-date Date]])

(def ->pxec (mc/decoder PXEC (mt/transformer mt/string-transformer mt/strip-extra-keys-transformer)))

(def pxec? (mc/validator PXEC))
