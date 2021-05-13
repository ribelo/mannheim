(ns mannheim.db.fx
  (:require
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [mannheim.db.core :refer [default-db]]
   [ribelo.doxa :as dx]))

(rf/reg-fx
 :commit
 (fn [data]
   (if (even? (count data))
     (let [it (iter data)]
       (while (.hasNext it)
         (let [store (.next it)
               txs   (.next it)]
           (dx/with-dx [db store]
             (enc/cond
               :let [tx (nth txs 0)]
               (vector?  tx) (dx/commit! db txs)
               (keyword? tx) (dx/commit! db [txs]))))))
     (timbre/error "mannheim: \":commit\" number of elements should be even"))))

(let [timeouts (atom {})]
  (rf/reg-fx
   :commit-later
   (fn [data]
     (if (even? (count data))
       (let [it (iter data)]
         (while (.hasNext it)
           (let [ms    (.next it)
                 id    (.next it)
                 store (.next it)
                 txs   (.next it)]
             (dx/with-dx [db store]
               (let [t (enc/cond!
                         :do           (some-> (@timeouts id) enc/tf-cancel!)
                         :let          [tx (nth txs 0)]
                         (vector?  tx) (enc/after-timeout ms (dx/commit! db txs))
                         (keyword? tx) (enc/after-timeout ms (dx/commit! db [txs])))]
                 (swap! timeouts assoc id t))))))
       (timbre/error "mannheim: \":commit\" number of elements should be even")))))
