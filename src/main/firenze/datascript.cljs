(ns firenze.datascript
  (:require
   [datascript.core :as d]
   [firenze.realtime-database :as rdb]))

(defn datascript->firebase [conn {:keys [tx-data tx-meta]}]
  (when-not (= :firebase/sync tx-meta)
    (doseq [eid (into [] (comp (map first) (distinct)) tx-data)]
      (let [{:keys [firebase/path firebase/id]
             :as   m} (d/pull @conn '[*] eid)
            id'       (or id (str (d/squuid)))]
        (when path
          (if-not id
            (d/transact! conn [{:db/id eid :firebase/id id'}])
            (rdb/set (conj path id')
                     (-> m
                         (dissoc :db/id)
                         (assoc :firebase/id id')))))))))

(defn firebase-on-child-added [conn {:keys [firebase/id] :as m}]
  (when id
    (let [eid (get (d/entity @conn [:firebase/id id]) :db/id -1)]
      (d/transact! conn [(assoc m :db/id eid)] :firebase/sync))))

(defn firebase-on-child-changed [conn m]
  (firebase-on-child-added conn m))

(defn firebase-on-child-removed [conn {:keys [firebase/id]}]
  (when id
    (d/transact! conn [[:db.fn/retractEntity [:firebase/id id]]] :firebase/sync)))

