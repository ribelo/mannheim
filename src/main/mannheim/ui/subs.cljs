(ns mannheim.ui.subs
  (:require
   [reagent.ratom :as ra :refer [reaction]]
   [re-frame.core :as rf]
   [ribelo.doxa :as dx]))

(rf/reg-sub-raw
 ::notifications
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui/main :notifications])))))

(rf/reg-sub-raw
 ::sidebar-expanded?
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/ui :ui/main :sidebar-expanded?])))))

(comment
  (dx/with-dx [dx_ :app]
    @dx_))

(rf/reg-sub-raw
 ::view
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui/main :view])))))

(rf/reg-sub-raw
 ::show-spinner?
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :ui/main :show-spinner?])))))
