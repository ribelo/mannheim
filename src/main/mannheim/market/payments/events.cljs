(ns mannheim.market.payments.events
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as e]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [taoensso.timbre :as timbre]
   [mannheim.electron.core :as el]
   [mannheim.electron.fx]
   [net.cgrand.xforms :as x]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [applied-science.js-interop :as j]
   [cuerdas.core :as str]
   [malli.core :as mc]
   [malli.transform :as mt]
   [ribelo.doxa :as dx]
   [mannheim.market.specs :as market.specs]
   [mannheim.market.payments.api :as api]))


(comment
  (->> (api/read-file "/home/ribelo/Public/rk/payments/f01752.txt")
       (api/raw-data->maps)
       (first)))

(rf/reg-event-fx
 ::read-payment-file
 (fn [{:keys [db ds]} [_ market-id]]
   (let [dialog (->clj (j/call el/dialog :showOpenDialogSync
                               (->js {:properties [:openFile :multiSelections]
                                      :filters    [{:name "txt" :extensions [:txt]}]})))]
     (e/cond
       :when   (seq dialog)
       :let    [raw-data (api/read-file (first dialog))]
       :if-not (api/data-valid? raw-data)
       {:fx [[:dispatch [::read-payment-file.invalid-data (first dialog)]]]}
       ;;
       :let    [coll (->> (api/raw-data->maps raw-data)
                          (into []
                                (comp (map #(assoc % :market.document/market-id market-id))
                                      (map (fn [m]
                                             (e/cond!
                                               :let [did   (m :document/id)
                                                     cid (m :contractor/id)]
                                               did
                                               [[:dx/match [:document/id did] :document/transfer-date nil]
                                                [:dx/put m]]
                                               ;;
                                               cid
                                               [[:dx/match [:contractor/id cid] :document/transfer-date nil]
                                                [:dx/put m]]))))))]
       :else
       {:fx (into [] (map (fn [tx] [:commit [:market tx]])) coll)}))))

(comment
  (rf/dispatch [::read-payment-file :f01752])
  (rf/dispatch [:init-db])

  dx/dxs_
  (dx/with-dx [db :market]
    (tap> (meta @db))
    (tap> @db))
  (dx/with-dx [db :market]
    (count (:document/id @db)))




  (meta @re-frame.db/app-db))

(rf/reg-event-fx
 ::read-payment-file.invalid-data
 (fn [_ [k file-path]]
   (timbre/info k file-path)
   {:dispatch [:mannheim.ui.events/add-notification
               [:title "import pliku" :content "nie udało się poprawnie zaimportować danych z pliku"]]}))

(rf/reg-event-fx
 ::read-payment-file.successful
 (fn [_ [k file-path]]
   (timbre/info k file-path)
   {:dispatch [:mannheim.ui.events/add-notification
               [:title "import pliku" :content "poprawnie zaimportowano dane z pliku"]]}))



(rf/reg-event-fx
 ::ui.set-market-query
 (fn [_ [id s]]
   {:transact          [{:db/id                        [:mannheim/type :mannheim/ui]
                         :payments.ui/market-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :tx-data [{:db/id                    [:mannheim/type :mannheim/ui]
                                   :payments.ui/market-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-contractor-query
 (fn [_ [id s]]
   {:transact          [{:db/id                            [:mannheim/type :mannheim/ui]
                         :payments.ui/contractor-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :tx-data [{:db/id                        [:mannheim/type :mannheim/ui]
                                   :payments.ui/contractor-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-nip-query
 (fn [_ [id s]]
   {:transact          [{:db/id                     [:mannheim/type :mannheim/ui]
                         :payments.ui/nip-query-tmp s}]
    :transact-debounce [{:id     id
                         :ms     500
                         :tx-data [{:db/id                 [:mannheim/type :mannheim/ui]
                                   :payments.ui/nip-query s}]}]}))

(rf/reg-event-fx
 ::ui.set-document-query
 (fn [_ [id s]]
   {:transact          [{:db/id                          [:mannheim/type :mannheim/ui]
                         :payments.ui/document-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :tx-data [{:db/id                      [:mannheim/type :mannheim/ui]
                                   :payments.ui/document-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-net-value-query
 (fn [_ [id s]]
   {:transact          [{:db/id                           [:mannheim/type :mannheim/ui]
                         :payments.ui/net-value-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :tx-data [{:db/id                       [:mannheim/type :mannheim/ui]
                                   :payments.ui/net-value-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-gross-value-query
 (fn [_ [id s]]
   {:transact          [{:db/id                             [:mannheim/type :mannheim/ui]
                         :payments.ui/gross-value-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :datoms [{:db/id                         [:mannheim/type :mannheim/ui]
                                   :payments.ui/gross-value-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-accounting-date-query
 (fn [_ [id s]]
   {:transact          [{:db/id                                 [:mannheim/type :mannheim/ui]
                         :payments.ui/accounting-date-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :datoms [{:db/id                             [:mannheim/type :mannheim/ui]
                                   :payments.ui/accounting-date-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-payment-date-query
 (fn [_ [id s]]
   {:transact          [{:db/id                              [:mannheim/type :mannheim/ui]
                         :payments.ui/payment-date-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :datoms [{:db/id                          [:mannheim/type :mannheim/ui]
                                   :payments.ui/payment-date-query (str/lower s)}]}]}))

(rf/reg-event-fx
 ::ui.set-transfer-date-query
 (fn [_ [id s]]
   {:transact          [{:db/id                               [:mannheim/type :mannheim/ui]
                         :payments.ui/transfer-date-query-tmp (str/lower s)}]
    :transact-debounce [{:id     id
                         :ms     500
                         :datoms [{:db/id                           [:mannheim/type :mannheim/ui]
                                   :payments.ui/transfer-date-query (str/lower s)}]}]}))

#_(rf/reg-event-fx
 ::ui.change-transfer-filter
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [id s]]
   (let [eid    (d/entid @@re-posh.db/store [:mannheim/type :mannheim/ui])
         status (d/q '[:find ?v .
                       :in $ ?e
                       :where [?e :payments.ui/transfer-filter ?v]]
                     @@re-posh.db/store
                     eid)]
     {:transact [{:db/id                       [:mannheim/type :mannheim/ui]
                  :payments.ui/transfer-filter (condp = status
                                                 :all      :paid
                                                 :paid     :not-paid
                                                 :not-paid :all
                                                 :paid)}]})))

(rf/reg-event-fx
 ::ui.show-market-chooser
 (fn [_ _]
   {:transact [{:db/id                            [:mannheim/type :mannheim/ui]
                :payments.ui/show-market-chooser? true}]}))

(rf/reg-event-fx
 ::ui.hide-market-chooser
 (fn [_ _]
   {:transact [{:db/id                            [:mannheim/type :mannheim/ui]
                :payments.ui/show-market-chooser? false}]}))

(rf/reg-event-fx
 ::set-transfer-date
 (fn [_ [_ id date]]
   {:transact [{:db/id                  [:document/id id]
                :document/transfer-date date}]}))

(rf/reg-event-fx
 ::select-document
 (fn [_ [_ id]]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :payments.ui/selected-documents id]]}))

(rf/reg-event-fx
 ::unselect-document
 (fn [_ [_ id]]
   {:transact [[:db/retract [:mannheim/type :mannheim/ui] :payments.ui/selected-documents id]]}))

(rf/reg-event-fx
 ::toggle-document
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ id]]
   (let [selected (set (d/q '[:find [?v ...]
                              :where [?e :payments.ui/selected-documents ?v]]
                            ds))]
     {:dispatch (if-not (contains? selected id)
                  [::select-document id]
                  [::unselect-document id])})))

(rf/reg-event-fx
 ::reset-document-selection
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} _]
   (let [selected (d/q '[:find [?v ...]
                         :where [?e :payments.ui/selected-documents ?v]]
                       ds)]
     {:dispatch-n (for [id selected]
                    [::unselect-document id])})))
