(ns mannheim.init
  (:require
   [reagent.ratom :as ra :refer [reaction]]
   [re-frame.core :as rf]
   [day8.re-frame.async-flow-fx]
   [cljs-bean.core :as bean :refer [->js ->clj]]
   [shadow.resource :as rc]
   [applied-science.js-interop :as j]
   [taoensso.timbre :as timbre]
   [ribelo.doxa :as dx]
   [mannheim.worker.fx]
   [mannheim.file-storage.fx]
   [mannheim.file-storage.events]
   [mannheim.firebase.fx]
   [mannheim.firebase.events]
   [mannheim.db.core]
   [mannheim.db.fx]
   [mannheim.db.events :as db.evt]
   [mannheim.ui.subs]
   [mannheim.ui.events]
   [mannheim.auth.fx]
   [mannheim.auth.subs]
   [mannheim.auth.events]))

(comment
  (j/call JSON :jsonParse (rc/inline "app-info.json"))


  (fs/get-doc [])
  )

(rf/reg-event-fx
 ::set-boot-successful
 (fn [_ _]
   (timbre/info ::set-boot-successful)
   {:fx [[:commit [:app [:dx/put [:app/id :settings] :boot-successful? true]]]]}))

(comment
  (rf/dispatch [::set-boot-successful])
  (dx/with-dx [dx_ :app]
    @dx_))

(rf/reg-sub-raw
 ::boot-successful?
 (fn [_ _]
   (dx/with-dx [dx_ :app]
     (reaction
      (get-in @dx_ [:app/id :settings :boot-successful?])))))



(rf/reg-event-fx
 ::boot
 (fn [{:keys [db]} _]
   (let [app-info (j/call js/JSON :parse (rc/inline "app-info.json"))]
     {:fx [[:dispatch [::db.evt/init-db]]
           [:mannheim.file-storage.fx/read :market]
           [:mannheim.file-storage.fx/sync :market]
           [:mannheim.firebase.fx/init-firebase app-info]
           [:mannheim.firebase.fx/sync :market]
           [:mannheim.firebase.fx/sync :users]
           [:dispatch [::set-boot-successful]]
           ]})))

(comment
  (rf/dispatch [::boot])
  (meta @re-frame.db/app-db))

(comment
  (meta @re-frame.db/app-db)
  (rf/reg-sub-raw
   ::tmp
   (fn [db _]
     (println @((meta @db) :store_))
     (ra/make-reaction
      (dx/pull @((meta @db) :store_) [:name] [:db/id :petr]))))
  @(rf/subscribe [::tmp])
  (rf/clear-subscription-cache!))

;; (rf/reg-event-fx
;;  ::boot
;;  [(rf/inject-cofx :mannheim.file-storage.fx/file-storage)]
;;  (fn [{:keys [file-storage ds]} [_]]
;;    (let [app-info (j/call js/JSON :parse (rc/inline "app-info.json"))]
;;      (timbre/info ::boot)
;;      (when (and file-storage (not= (:schema file-storage) db/schema))
;;        (timbre/warn "incorrect local database - download correct from firebase")
;;        (timbre/warn "local-schema" (:schema file-storage))
;;        (timbre/warn "db-schema   " db/schema))
;;      {:async-flow
;;       {:first-dispatch [::fireposh.events/init-firebase app-info]
;;        :rules          [{:when     :seen? :events [::fireposh.events/init-firebase]
;;                          :dispatch (if (and file-storage (= (:schema file-storage) db/schema))
;;                                      [::fireposh.events/create-connection.from-db file-storage]
;;                                      [::fireposh.events/create-connection.firebase-schema])}
;;                         {:when     :seen-any-of? :events [::fireposh.events/create-connection.from-db
;;                                                           ::fireposh.events/create-connection.from-schema]
;;                          :dispatch [::initialize-db]}
;;                         {:when     :seen? :events [::initialize-db]
;;                          :dispatch [::fireposh.events/link-db 5000]}
;;                         {:when     :seen? :events [::fireposh.events/link-db]
;;                          :dispatch [::set-boot-successful]}
;;                         {:when     :seen? :events [::set-boot-successful]
;;                          :dispatch [::fireposh.events/link-paths
;;                                     [[:mannheim/payments :payments/documents]
;;                                      [:mannheim/dc :dc/groups]
;;                                      [:mannheim/dc :dc/warehouse-report]
;;                                      [:mannheim/cg :cg/warehouse]
;;                                      ]]}
;;                         {:when     :seen? :events [::set-boot-successful]
;;                          :dispatch [::initialize-db]}
;;                         {:when     :seen? :events [::set-boot-successful]
;;                          :dispatch [::run-autosave]}
;;                         {:when     :seen? :events [::set-boot-successful]
;;                          :dispatch [:mannheim.ui.events/data-loading-ms 5000]}]
;;        }
;;       }
;;      )))
