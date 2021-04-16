(ns mannheim.db.events
  (:require
   [re-frame.core :as rf]
   [re-frame.db]
   [ribelo.doxa :as dx]
   [mannheim.db.core :refer [default-db]]))

(rf/reg-event-db
 ::init-db
 (fn [_]
   default-db))

(rf/reg-event-fx
 :commit
 (fn [_ [_ store txs]]
   {:commit [store txs]}))
