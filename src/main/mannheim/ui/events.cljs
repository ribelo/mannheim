(ns mannheim.ui.events
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as enc]))

(comment
  (let [e {:content "skopiowano do schowka"}
        v [e]]
    (enc/rnum? (partial = e) v)))

(rf/reg-event-fx
 ::add-notification
 (fn [_ [_ id notification]]
   (let [pred #(not (enc/rsome (partial = notification) %))]
     {:fx [[:commit       [:app [[:dx/match  [:app/id :ui/main] :notifications pred]
                                 [:dx/conj   [:app/id :ui/main] :notifications notification]]]]
           [:commit-later [(enc/ms :secs 3)  [id notification]
                           :app [:dx/delete [:app/id :ui/main] :notifications notification]]]]})))

(rf/reg-event-fx
 ::expand-sidebar
 (fn [_ _]
   {:fx [[:commit [:app [:dx/put [:app/id :ui/main] :sidebar-expanded? true]]]]}))

(rf/reg-event-fx
 ::shrink-sidebar
 (fn [_ _]
   {:fx [[:commit [:app [:dx/put [:app/id :ui/main] :sidebar-expanded? false]]]]}))

(rf/reg-event-fx
 ::set-view
 (fn [_ [_ view]]
   {:fx [[:commit [:app [:dx/put [:app/id :ui/main] :view view]]]]}))

(rf/reg-event-fx
 ::data-loading
 (fn [_ [_ v]]
   {:fx [[:commit [:app [:dx/put [:app/id :ui/main] :show-spinner? v]]]]}))

(rf/reg-event-fx
 ::data-loading-ms
 (fn [_ [id ms]]
   {:fx [[:commit       [      :app [:dx/conj   [:app/id :ui/main] :show-spinner? true]]]
         [:commit-later [ms id :app [:dx/delete [:app/id :ui/main] :show-spinner? false]]]]}))
