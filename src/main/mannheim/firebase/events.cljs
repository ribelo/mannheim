(ns mannheim.firebase.events
  (:require
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [re-frame.core :as rf]
   [ribelo.doxa :as dx]
   [mannheim.firebase.fx :as fx]))

(rf/reg-event-fx
 ::download-store
 (fn [_ [id store]]
   (timbre/info id store)
   (let [k (enc/merge-keywords [:firebase.download store])]
     {:fx [[:commit [store [:dx/put [:app.settings/id k] :promises :none]]]
           [::fx/download-store store]]})))

(rf/reg-event-fx
 ::download-store.success
 (fn [_ [id store]]
   (timbre/info id store)
   (let [k (enc/merge-keywords [:firebase.download store])]
     {:fx [[:commit [store [:dx/delete [:app.settings/id k] :promises]]]]})))

(rf/reg-event-fx
 ::download-store.failure
 (fn [_ [id store]]
   (timbre/warn id store)
   (let [k (enc/merge-keywords [:firebase.download store])]
     {:fx [[:commit [store [:dx/delete [:app.settings/id k] :promises]]]]})))

(rf/reg-event-fx
 ::patch-store
 (fn [_ [_ store]]
   ))

(rf/reg-event-fx
 ::sync-store
 (fn [_ [_ store]]
   {:fx [[::fx/sync store]]}))


(comment
  (rf/dispatch [::download-store :market])

  (rf/dispatch [::sync-store :market])

  (dx/with-dx [db_ :market]
    @db_)

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/put    [:test/id 1] {:name "ivan" :aka ["devil"] :age 39}]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/put    [:test/id 1] :age 35]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/delete    [:test/id 1] :age]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/conj    [:test/id 1] :aka "tupen"]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/delete    [:test/id 1] :aka "devil"]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/delete    [:test/id 1] :aka "tupen"]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/put    [:test/id 1] :aka "devil"]]))

  (dx/with-dx [db_ :market]
    (dx/commit! db_ [[:dx/conj    [:test/id 1] :aka "tupen"]]))

  (dx/with-dx [db_ :market]
    @db_))
