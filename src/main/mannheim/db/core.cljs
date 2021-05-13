(ns mannheim.db.core
  (:require
   [re-frame.db]
   [ribelo.doxa :as dx]
   [reagent.core :as r]))

(def default-db    (dx/create-dx))
(def users         (dx/create-dx {} {:with-diff? true}))
(def market        (dx/create-dx {} {:with-diff? true}))
(def work-schedule (dx/create-dx {} {:with-diff? true}))

(dx/reg-dx! :app           re-frame.db/app-db    )
(dx/reg-dx! :users         (r/atom users        ))
(dx/reg-dx! :market        (r/atom market       ))
(dx/reg-dx! :work-schedule (r/atom work-schedule))
