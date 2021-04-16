(ns mannheim.market.payments.utils
  (:require
   [taoensso.encore :as e]
   [cuerdas.core :as str]))

(def fn-map
  {">"  >
   "<"  <
   "="  =
   ">=" >=
   "<=" <=})

(defn parse-document-value [s]
  (if (not-empty s)
    (as-> s s
      (str/split s " ")
      (partition-all 2 s)
      (mapv (fn [coll]
              (if (= (count coll) 2)
                [(get fn-map (first coll) =) (or (e/as-?float (second coll)) 0.00)]
                [= (or (e/as-?float (second coll)) 0.00)])) s))
    [[not= 0.0]]))

(defn transfer-date->pred [s filter]
  (if (empty? s)
    (condp = filter
      :all      (constantly true)
      :paid     not-empty
      :not-paid empty?
      (constantly true))
    (partial re-find (re-pattern s))))
