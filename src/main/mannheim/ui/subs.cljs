(ns mannheim.ui.subs
  (:require
   [reagent.ratom :as ra]
   [re-frame.core :as rf]))

#_(rp/reg-query-sub
 ::notifications
 '[:find  [?v ...]
   :where [?e :mannheim.ui/notifications ?v]])

#_(rp/reg-query-sub
 ::sidebar-expanded?
 '[:find ?v .
   :where [?e :mannheim.ui/sidebar-expanded? ?v]])

#_(rp/reg-query-sub
 ::current-view
 '[:find ?v .
   :where [?e :mannheim.ui/view ?v]])

#_(rp/reg-query-sub
 ::show-spinner?
 '[:find ?v .
   :where [?e :mannheim.ui/show-spinner? ?v]])
