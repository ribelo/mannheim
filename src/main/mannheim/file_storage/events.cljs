(ns mannheim.file-storage.events
  (:require
   [re-frame.core :as rf]
   [mannheim.file-storage.fx :as fx]))

(rf/reg-event-fx
 ::write
 (fn [_ [_ store]]
   {::fx/write store}))

(rf/reg-event-fx
 ::sync
 (fn [_ [_ store]]
   {::fx/sync store}))

(rf/reg-event-fx
 ::read
 (fn [_ [_ store]]
   {::fx/read store}))
