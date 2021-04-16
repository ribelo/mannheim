(ns mannheim.specs
  (:require
   [cuerdas.core :as str]
   [malli.core :as mc]
   [ribelo.doxa :as dx]))

(def Date
  [:re #"^\d{4}-\d{2}-\d{2}$"])

(def TaxId
  [:schema {:decode/string (fn [s _] (str/lower s))}
   [:re #"^([A-z]{2})?\d{10}$"]])

(def EntityKey
  [:or keyword? string?])

(def EntityId
  [:schema {:registry {::simple-eid   [:or keyword? nat-int? string?]
                       ::compound-eid [:vector {:min 2} ::simple-eid]}}
   [:or ::simple-eid ::compound-eid]])

(def Ident
  [:cat EntityKey EntityId])
