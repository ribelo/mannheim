(ns mannheim.db.fx
  (:require
   [taoensso.encore :as e]
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [mannheim.db.core :refer [default-db]]
   [ribelo.doxa :as dx]))

(rf/reg-fx
 :commit
 (fn [data]
   (if (even? (count data))
     (let [it (iter data)]
       (loop []
         (when (.hasNext it)
           (let [store (.next it)
                 txs   (.next it)]
             (dx/with-dx [db store]
               (e/cond
                 :let [tx  (nth txs 0)]
                 (vector?  tx) (dx/commit! db txs)
                 (keyword? tx) (dx/commit! db [txs])))
             (recur)))))
     (timbre/error "mannheim: \":commit\" number of elements should be even"))))
