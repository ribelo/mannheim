(ns mannheim.file-storage.events
  (:require
   [re-frame.core :as rf]
   [mannheim.file-storage.fx :as fx]))

(rf/reg-event-fx
 ::write
 (fn [_ [_ store]]
   {:fx [[::fx/write store]]}))

(rf/reg-event-fx
 ::run-autosave
 (fn [{db :db} [_ store ms subsequent?]]
   (when (or (not subsequent?) (get-in db [:app.settings/id ::autosave store]))
     {:fx [(when-not subsequent? [:commit [:app [:dx/put [:app.settings/id ::autosave] store ms]]])
           [:dispatch-later {:ms ms :dispatch [::write store]}]
           [:dispatch-later {:ms ms :dispatch [::run-autosave store ms true]}]]})))

(rf/reg-event-fx
 ::stop-autosave
 (fn [_ [_ store]]
   {:fx [[:commit [:app [:dx/delete [:app.settings/id ::autosave] store]]]]}))

(comment
  (rf/dispatch [::write :app])
  (rf/dispatch [::run-autosave :app 5000])
  (rf/dispatch [::stop-autosave :app]))
