(ns mannheim.ui.events
  (:require
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre]))

(rf/reg-event-fx
 ::add-notification
 (fn [_ [k notification]]
   {:transact          [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/notifications notification]]
    :transact-debounce [{:id      [k notification]
                         :ms      3000
                         :tx-data [[:db/retract [:mannheim/type :mannheim/ui] :mannheim.ui/notifications notification]]}]}))

(rf/reg-event-fx
 ::expand-sidebar
 (fn [_ _]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/sidebar-expanded? true]]}))

(rf/reg-event-fx
 ::shrink-sidebar
 (fn [_ _]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/sidebar-expanded? false]]}))

(rf/reg-event-fx
 ::set-view
 (fn [_ [_ view]]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/view view]]}))

(rf/reg-event-fx
 ::data-loading
 (fn [_ _]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/show-spinner? true]]}))

(rf/reg-event-fx
 ::data-loading-ms
 (fn [_ [k ms]]
   (println k)
   {:transact          [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/show-spinner? true]]
    :transact-debounce [{:id      [k ms]
                         :ms      ms
                         :tx-data [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/show-spinner? false]]}]}))

(rf/reg-event-fx
 ::data-loaded
 (fn [_ _]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :mannheim.ui/show-spinner? false]]}))

(rf/reg-event-fx
 :fireposh/data-loading
 (fn [_ _]
   {:dispatch [::data-loading]}))

(rf/reg-event-fx
 :fireposh/data-loaded
 (fn [_ _]
   {:dispatch [::data-loaded]}))
