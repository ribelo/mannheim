(ns mannheim.market.payments.events
  (:require
   [meander.epsilon :as m]
   ["date-fns" :as dt]
   [re-frame.core :as rf]
   [taoensso.encore :as enc]
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
   [ribelo.danzig :as dz :refer [=>>]]
   [mannheim.market.specs :as market.specs]
   [mannheim.market.payments.api :as api]))


(comment
  (->> (api/read-file "/home/ribelo/Public/rk/payments/f01752.txt")
       (api/buffer->market-payments)
       (first)))

(rf/reg-event-fx
 ::read-file
 (fn [_ [_eid]]
  (let [files (->clj (j/call el/dialog :showOpenDialogSync
                             (->js {:properties [:openFile :multiSelections]
                                    :filters    [{:name "txt" :extensions [:txt :csv]}]})))]
    (println files)
    (when (seq files)
      {:worker (=>> files
                    (map (fn [file-path]
                           {:event [::api/read-file file-path]
                            :on-success
                            (fn [[type data]]
                              (enc/cond
                                (= :market-payments type)
                                (rf/dispatch [::ui-show-market-chooser {:event [::read-payment-file file-path data]}])
                                (= :pxec type)
                                (rf/dispatch [::read-pxec-file file-path data])
                                :else
                                (println :type type)))
                            :on-failure
                            (fn [] (timbre/error _eid file-path))})))}))))

(rf/reg-event-fx
 ::read-pxec-file
 (fn [_ [_eid file-path data]]
   (timbre/info _eid file-path)
   (when (seq data)
     (let [txs (=>> data
                    (mapcat (fn [{:document/keys [id transfer-date]}]
                              [[:dx/match [:document/id id] some?]
                               [:dx/put [:document/id id] :document/transfer-date transfer-date]])))]
       {:fx [[:commit   [:market txs]]
             [:dispatch [:mannheim.ui.events/add-notification [_eid file-path]
                         {:content (enc/format "poprawnie wczytano plik %s" file-path)}]]]}))))

(rf/reg-event-fx
 ::read-payment-file
 (fn [_ [_eid file-path data market-id]]
   (timbre/info _eid market-id file-path)
   (if (seq data)
     (let [txs
           (->> data
                (into []
                      (comp
                       (map (fn [m]
                              (assoc m :document/market {:market/id market-id})))
                       (mapcat (fn [{:document/keys [id] :as m}]
                                 (when id
                                   [[:dx/match [:document/id id] :document/transfer-date nil]
                                    [:dx/put m]]))))))]
       {:fx [[:commit   [:market txs]]
             [:dispatch [::read-payment-file-successful file-path]]]})
     {:fx [[:dispatch [::read-payment-file-successful file-path]]]})))

(comment
  (def fs (js/require "fs"))
  (def back (enc/read-edn (str (.readFileSync fs "./mannheim_payments_back.edn"))))
  (def txs
    (=>> back
         (dz/rename-columns {:document/market.id :document/market-id
                             :document/contractor.nip :document/contractor-id
                             :document/contractor.name :document/contractor-name})
         (map (fn [{:document/keys [id market-id contractor-id contractor-name issue-date net-value gross-value] :as m}]
                (-> m
                    (assoc
                     :document/id [id contractor-id issue-date]
                     :document/market {:market/id market-id}
                     :document/vat-value (enc/round2 (- gross-value net-value))
                     :document/contractor {:contractor/id contractor-id :contractor/name contractor-name}
                     :document/type :document.type/invoice)
                    (dissoc :document/contractor-id :document/contractor-name :document/market-id
                            :db/path))))
         (filter mannheim.market.specs/document?)
         (map (fn [doc] [:dx/put doc]))))

  (dx/with-dx [dx_ :market]
    (dx/commit! dx_ txs))

  (dx/with-dx [dx_ :market]
    (-> @dx_ :document/id vals first))


  (def atm (atom nil))
  (rf/dispatch [::read-payment-file :f01752])
  (rf/dispatch [:init-db])

  (keys @dx/dxs_)
  (dx/with-dx [dx_ :market]
    (count (:document/id @dx_)))
  (dx/with-dx [db :market]
    (keys @db))




  (meta @re-frame.db/app-db))

(rf/reg-event-fx
 ::read-payment-file-failure
 (fn [_ [k file-path]]
   (timbre/warn k file-path)
   {:fx [[:dispatch [:mannheim.ui.events/add-notification
                     {:title "import pliku" :content "nie udało się poprawnie zaimportować danych z pliku"}]]]}))

(rf/reg-event-fx
 ::read-payment-file-successful
 (fn [_ [k file-path]]
   (timbre/info k file-path)
   {:fx [[:dispatch [:mannheim.ui.events/add-notification
                     {:title "import pliku" :content "poprawnie zaimportowano dane z pliku"}]]]}))

(rf/reg-event-fx
 ::ui-set-market-query
 (fn [_ [id s & {:keys [ms]}]]
   (dx/with-dx [dx_ :app]
     (let [ms (or ms 500)
           v  (get-in @dx_ [:app/id :ui.market/payments :market-query-tmp])]
       (if-not (identical? (str/lower s) v)
         {:fx [[:commit       [      :app [:dx/put    [:app/id :ui.market/payments] :market-query-tmp (str/lower s)]]]
               [:commit-later [ms id :app [:dx/put    [:app/id :ui.market/payments] :market-query     (str/lower s)]]]]}
         {:fx [[:commit       [      :app [:dx/delete [:app/id :ui.market/payments] :market-query-tmp]]]
               [:commit       [      :app [:dx/delete [:app/id :ui.market/payments] :market-query]]]]})))))

(rf/reg-event-fx
 ::ui-set-contractor-query
 (fn [_ [id s & {:keys [ms]}]]
   (dx/with-dx [dx_ :app]
     (let [ms (or ms 500)
           v  (get-in @dx_ [:app/id :ui.market/payments :contractor-query-tmp])]
       (if-not (identical? (str/lower s) v)
         {:fx [[:commit       [      :app [:dx/put    [:app/id :ui.market/payments] :contractor-query-tmp (str/lower s)]]]
               [:commit-later [ms id :app [:dx/put    [:app/id :ui.market/payments] :contractor-query     (str/lower s)]]]]}
         {:fx [[:commit       [      :app [:dx/delete [:app/id :ui.market/payments] :contractor-query-tmp]]]
               [:commit       [      :app [:dx/delete [:app/id :ui.market/payments] :contractor-query]]]]})))))

(rf/reg-event-fx
 ::ui-set-nip-query
 (fn [_ [id s & {:keys [ms]}]]
   (dx/with-dx [dx_ :app]
     (let [ms (or ms 500)
           v (get-in @dx_ [:app/id :ui.market/payments :nip-query-tmp])]
       (if-not (identical? s v)
         {:fx [[:commit       [      :app [:dx/put    [:app/id :ui.market/payments] :nip-query-tmp (str/lower s)]]]
               [:commit-later [ms id :app [:dx/put    [:app/id :ui.market/payments] :nip-query     (str/lower s)]]]]}
         {:fx [[:commit       [      :app [:dx/delete [:app/id :ui.market/payments] :nip-query-tmp]]]
               [:commit       [      :app [:dx/delete [:app/id :ui.market/payments] :nip-query]]]]})))))

(rf/reg-event-fx
 ::ui-set-document-query
 (fn [_ [id s]]
   {:fx [[:commit       [       :app [:dx/put [:app/id :ui.market/payments] :document-query-tmp (str/lower s)]]]
         [:commit-later [500 id :app [:dx/put [:app/id :ui.market/payments] :document-query     (str/lower s)]]]]}))

(rf/reg-event-fx
 ::ui-set-net-value-query
 (fn [_ [id s]]
   {:fx [[:commit       [       :app [:dx/put [:app/id :ui.market/payments] :net-value-query-tmp (str/lower s)]]]
         [:commit-later [500 id :app [:dx/put [:app/id :ui.market/payments] :net-value-query     (str/lower s)]]]]}))

(rf/reg-event-fx
 ::ui-set-gross-value-query
 (fn [_ [id s]]
   {:fx [[:commit       [       :app [:dx/put [:app/id :ui.market/payments] :gross-value-query-tmp (str/lower s)]]]
         [:commit-later [500 id :app [:dx/put [:app/id :ui.market/payments] :gross-value-query     (str/lower s)]]]]}))

(rf/reg-event-fx
 ::ui-set-accounting-date-query
 (fn [_ [id s]]
   {:fx [[:commit       [       :app [:dx/put [:app/id :ui.market/payments] :accounting-date-query-tmp (str/lower s)]]]
         [:commit-later [500 id :app [:dx/put [:app/id :ui.market/payments] :accounting-date-query     (str/lower s)]]]]}))

(rf/reg-event-fx
 ::ui-set-payment-date-query
 (fn [_ [id s]]
   {:fx [[:commit       [       :app [:dx/put [:app/id :ui.market/payments] :payment-date-query-tmp (str/lower s)]]]
         [:commit-later [500 id :app [:dx/put [:app/id :ui.market/payments] :payment-date-query     (str/lower s)]]]]}))

(rf/reg-event-fx
 ::ui-set-transfer-date-query
 (fn [_ [id s]]
   {:fx [[:commit       [       :app [:dx/put [:app/id :ui.market/payments] :transfer-date-query-tmp (str/lower s)]]]
         [:commit-later [500 id :app [:dx/put [:app/id :ui.market/payments] :transfer-date-query     (str/lower s)]]]]}))

;; #_(rf/reg-event-fx
;;  ::ui.change-transfer-filter
;;  [(rf/inject-cofx :ds)]
;;  (fn [{:keys [ds]} [id s]]
;;    (let [eid    (d/entid @@re-posh.db/store [:mannheim/type :mannheim/ui])
;;          status (d/q '[:find ?v .
;;                        :in $ ?e
;;                        :where [?e :payments.ui/transfer-filter ?v]]
;;                      @@re-posh.db/store
;;                      eid)]
;;      {:transact [{:db/id                       [:mannheim/type :mannheim/ui]
;;                   :payments.ui/transfer-filter (condp = status
;;                                                  :all      :paid
;;                                                  :paid     :not-paid
;;                                                  :not-paid :all
;;                                                  :paid)}]})))

(rf/reg-event-fx
 ::ui-show-market-chooser
 (fn [_ [_ opts]]
   {:fx [[:commit [:app [:dx/put [:app/id :ui.market.payments/market-chooser] opts]]]]}))

(rf/reg-event-fx
 ::ui-hide-market-chooser
 (fn [_ _]
   {:fx [[:commit [:app [:dx/delete [:app/id :ui.market.payments/market-chooser]]]]]}))

(rf/reg-event-fx
 ::set-transfer-date
 (fn [_ [_ id date]]
   (enc/cond
     :when id
     (enc/kw-identical? date :now)
     {:fx [[:commit [:market [:dx/put [:document/id id] :document/transfer-date (dt/format (js/Date.) "yyyy-MM-dd")]]]]}
     (nil? date)
     {:fx [[:commit [:market [:dx/delete [:document/id id] :document/transfer-date]]]]})))

(comment
  (dx/with-dx [dx_ :market]
    (get-in @dx_ [:document/id ["9120747062" "7791906082" "2019-08-21"]])))


;; (rf/reg-event-fx
;;  ::select-document
;;  (fn [_ [_ id]]
;;    {:transact [[:db/add [:mannheim/type :mannheim/ui] :payments.ui/selected-documents id]]}))

;; (rf/reg-event-fx
;;  ::unselect-document
;;  (fn [_ [_ id]]
;;    {:transact [[:db/retract [:mannheim/type :mannheim/ui] :payments.ui/selected-documents id]]}))

;; (rf/reg-event-fx
;;  ::toggle-document
;;  [(rf/inject-cofx :ds)]
;;  (fn [{:keys [ds]} [_ id]]
;;    (let [selected (set (d/q '[:find [?v ...]
;;                               :where [?e :payments.ui/selected-documents ?v]]
;;                             ds))]
;;      {:dispatch (if-not (contains? selected id)
;;                   [::select-document id]
;;                   [::unselect-document id])})))

(rf/reg-event-fx
 ::toggle-document
 (fn [_ [_ id]]
   (dx/with-dx [dx_ :app]
     (let [docs (get-in @dx_ [:app/id :ui.market/payments :selected-documents])]
       (if-not (enc/rsome #{id} docs)
         {:fx [[:commit [:app [:dx/conj   [:app/id :ui.market/payments] :selected-documents id]]]]}
         {:fx [[:commit [:app [:dx/delete [:app/id :ui.market/payments] :selected-documents id]]]]})))))

(rf/reg-event-fx
 ::reset-document-selection
 (fn [_ _]
   {:fx [[:commit [:app [:dx/delete [:app/id :ui.market/payments]]]]]}))

;; (rf/reg-event-fx
;;  ::reset-document-selection
;;  [(rf/inject-cofx :ds)]
;;  (fn [{:keys [ds]} _]
;;    (let [selected (d/q '[:find [?v ...]
;;                          :where [?e :payments.ui/selected-documents ?v]]
;;                        ds)]
;;      {:dispatch-n (for [id selected]
;;                     [::unselect-document id])})))
