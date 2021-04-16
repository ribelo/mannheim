(ns mannheim.subs
  (:require
   [reagent.ratom :as ra]
   [re-frame.core :as rf]
   [ribelo.doxa :as dx]))

(comment
  (dx/pull @(:ui @dx/dxs_) [:name] [:db/id :ivan])

  (rf/reg-sub-raw
   ::tmp1
   (fn [_ _]
     (ra/reaction
      (dx/with-dx [db :ui]
        (dx/pull @db [:name] [:db/id :ivan])))))

  (rf/reg-event-fx
   ::tmp1
   (fn [_ [_ name]]
     {:fx [[:transact [:ui [[:dx/put [:db/id :ivan] :name name]]]]]}))

  (rf/dispatch [::tmp1 "Ivan"])

  (rf/clear-subscription-cache!)
  (rf/subscribe [::tmp1])
  (rf/subscribe [::tmp2])
  (dx/with-dx [db :ui]
    (dx/transact! db [[:dx/put [:db/id :ivan] :name "IVAN"]]))
  )
