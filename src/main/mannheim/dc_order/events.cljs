(ns mannheim.dc-order.events
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
   [meander.epsilon :as m]
   [mannheim.utils :as u]))

;;
;; * ui
;;

(rf/reg-event-fx
 ::ui.set-name-query
 (fn [_ [id s]]
   {:transact          [{:db/id                      [:mannheim/type :mannheim/ui]
                         :dc.order.ui/name-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                  [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/name-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-ean-query
 (fn [_ [id s]]
   {:transact          [{:db/id                     [:mannheim/type :mannheim/ui]
                         :dc.order.ui/ean-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                 [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/ean-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-stock-query
 (fn [_ [id s]]
   {:transact          [{:db/id                       [:mannheim/type :mannheim/ui]
                         :dc.order.ui/stock-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                   [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/stock-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-demand-query
 (fn [_ [id s]]
   {:transact          [{:db/id                        [:mannheim/type :mannheim/ui]
                         :dc.order.ui/demand-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                    [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/demand-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-ec-query
 (fn [_ [id s]]
   {:transact          [{:db/id                    [:mannheim/type :mannheim/ui]
                         :dc.order.ui/ec-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/ec-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-cg-query
 (fn [_ [id s]]
   {:transact          [{:db/id                    [:mannheim/type :mannheim/ui]
                         :dc.order.ui/cg-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/cg-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-cg-stock-query
 (fn [_ [id s]]
   {:transact          [{:db/id                          [:mannheim/type :mannheim/ui]
                         :dc.order.ui/cg-stock-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                      [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/cg-stock-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-vendor-query
 (fn [_ [id s]]
   {:transact          [{:db/id                        [:mannheim/type :mannheim/ui]
                         :dc.order.ui/vendor-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                    [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/vendor-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-supply-days-query
 (fn [_ [id s]]
   {:transact          [{:db/id                             [:mannheim/type :mannheim/ui]
                         :dc.order.ui/supply-days-query-tmp (str/lower s)}]
    :transact-debounce [{:id      id
                         :ms      1000
                         :tx-data [{:db/id                         [:mannheim/type :mannheim/ui]
                                    :dc.order.ui/supply-days-query (str/lower s)}]}]
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-order-qty-query
 (fn [_ [id s]]
   {:transact         [{:db/id                           [:mannheim/type :mannheim/ui]
                        :dc.order.ui/order-qty-query-tmp (str/lower s)}]
    :transact-debouce {:id      id
                       :ms      1000
                       :tx-data [{:db/id                       [:mannheim/type :mannheim/ui]
                                  :dc.order.ui/order-qty-query (str/lower s)}]}
    :dispatch-debounce [{:id       id
                         :ms       1001
                         :dispatch [::filter-order]}]}))

(rf/reg-event-fx
 ::ui.set-sort-order
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [id k]]
   (let [[po pk] (d/q '[:find ?v .
                        :where [?e :dc.order.ui/sort-order ?v]]
                      ds)
         [o k*]  (m/match [po pk]
                   (m/and [?o ?k]
                          [:asc ~k])  [:desc ?k]
                   (m/and [?o ?k]
                          [:desc ~k]) [:asc ?k]
                   _
                   [:asc k])]
     {:transact [{:db/id                  [:mannheim/type :mannheim/ui]
                  :dc.order.ui/sort-order [o k*]}]})))

(rf/reg-event-fx
 ::ui.groups-settings.select-group
 (fn [_ [_ group-id]]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :dc.order.ui.settings/selected-group group-id]]}))


#_(rf/reg-event-fx
 ::ui.show-settings
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} _]
   (if-let [market-id (d/q '[:find ?v .
                             :where [?e :dc-order/market-id ?v]]
                           @@re-posh.db/store)]
     {:transact [[:db/add [:mannheim/type :mannheim/settings] :dc.order.ui/show-settings? true]]}
     {:dispatch [:mannheim.ui.events/add-notification
                 [:title   nil
                  :type    :error
                  :content "nie wybrano sklepu"]]})))

(rf/reg-event-fx
 ::ui.hide-settings
 (fn [_ _]
   {:transact [[:db/add [:mannheim/type :mannheim/settings] :dc.order.ui/show-settings? false]]}))

(rf/reg-event-fx
 ::ui.make-order.select-group
 (fn [_ [_ group-id]]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :dc.order.ui.make-order/selected-group group-id]]}))

#_(rf/reg-event-fx
 ::ui.show-make-order-dialog
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} _]
   (if-let [market-id (d/q '[:find ?v .
                             :where [?e :dc-order/market-id ?v]]
                           @@re-posh.db/store)]
     {:transact [[:db/add [:mannheim/type :mannheim/settings] :dc.order.ui/show-make-order-dialog? true]]}
     {:dispatch [:mannheim.ui.events/add-notification
                 [:title   nil
                  :type    :error
                  :content "nie wybrano sklepu"]]})))

;;
;; * read file
;;

(def fs (el/require "fs"))

(def iconv (el/require "iconv-lite"))

(def report-keys
  [:dc.warehouse.report/group-id
   :dc.warehouse.report/product-name
   :dc.warehouse.report/product-id
   :dc.warehouse.report/product-ean
   :dc.warehouse.report/rate-of-sales
   :dc.warehouse.report/purchase-price
   :dc.warehouse.report/sell-price
   :dc.warehouse.report/stock])

(def document-header
  ["Indeks grupy"
   "Nazwa"
   "Indeks"
   "PLU"
   "Tempo sprzedaży"
   "Cena kat. od gł. dost"
   "Cena sprz."
   "Stan"])

(def header-map
  (zipmap document-header report-keys))

(defn read-file [path]
  (let [s (j/call fs :readFileSync path)]
    (j/call iconv :decode s "cp1250")))

(defn- data-valid? [data]
  (if (every? #(re-find (re-pattern %) (-> data (str/lines) (first))) document-header)
    true
    (println (mapv (fn [header] [header (re-find (re-pattern header) (-> data (str/lines) (first)))]) document-header))))

(defn- raw-data->maps [raw]
  (let [lines  (str/lines raw)
        header (map str/trim (-> (first lines) (str/split "\t")))]
    (->> lines
         (into []
               (comp
                (drop 1)
                (map (fn [s]
                       (->> (str/split s "\t")
                            (mapv #(-> % (str/trim) (str/replace "\"" ""))))))
                (map #(zipmap header %))
                (map #(clojure.set/rename-keys % header-map))
                (map #(select-keys % report-keys))
                (map (fn [m]
                       (let [m* (-> m
                                    ;; (assoc  :db/path [:mannheim/dc :dc/warehouse-report])
                                    (update :dc.warehouse.report/product-name #(some-> % str/trim str/lower))
                                    (update :dc.warehouse.report/rate-of-sales e/as-?float)
                                    (update :dc.warehouse.report/purchase-price e/as-?float)
                                    (update :dc.warehouse.report/sell-price e/as-?float)
                                    (update :dc.warehouse.report/stock e/as-?float))]
                         (e/filter-vals identity m*)))))))))

(rf/reg-event-fx
 ::read-report-file
 [(rf/inject-cofx :ds)]
 (fn [{:keys [db ds]} [_ market-id]]
   (timbre/debug ::read-report-file)
   (if market-id
     (let [dialog (->clj (j/call el/dialog :showOpenDialogSync
                                 (->js {:properties [:openFile :multiSelections]
                                        :filters    [{:name "txt" :extensions [:txt]}]})))]
       (when-not (empty? dialog)
         {:dispatch-n [[:mannheim.ui.events/data-loading]
                       ^:flush-dom [::read-report-file.read-file market-id (first dialog)]]}))
     {:dispatch [:mannheim.ui.events/add-notification
                 {:type    :error
                  :content "nie wybrano sklepu"}]})))

#_(rf/reg-event-fx
 ::read-report-file.read-file
 [(rf/inject-cofx :ds)]
 (fn [{:keys [db ds dialog]} [_ market-id file-path]]
   (let [raw-data (read-file file-path)]
     (if-not (data-valid? raw-data)
       {:dispatch [::read-report-file.invalid-data (first dialog)]}
       (let [groups      (-> (d/q '[:find [?v ...]
                                    :where [?e :dc.order.settings/group-id ?v]]
                                  @@re-posh.db/store)
                             (set))
             to-rectract (->> (d/q '[:find [?e ...]
                                     :in $ ?market-id
                                     :where [?e :dc.warehouse.report/market-id ?market-id]]
                                   ds market-id)
                              (mapv (fn [eid]
                                      [:db.fn/retractEntity eid])))
             new-data    (->> (raw-data->maps raw-data)
                              (mapv #(assoc % :dc.warehouse.report/market-id market-id))
                              (filterv (fn [m] (groups (:dc.warehouse.report/group-id m)))))]
         {:transact   (into to-rectract new-data)
          :dispatch-n [[::make-order market-id (first dialog)]
                       [:mannheim.dc-order.events/ui.hide-make-order-dialog]
                       [::read-report-file.successful file-path]]})))))

(rf/reg-event-fx
 ::read-report-file.successful
 (fn [_ [_ file-name]]
   (timbre/debug ::read-report-file.successful)
   {:dispatch-n [[:mannheim.ui.events/data-loaded]
                 [:mannheim.ui.events/add-notification
                  {:type    :success
                   :content (str "poprawnie wczytano plik " file-name)}]]}))

(rf/reg-event-fx
 ::read-report-file.invalid-data
 (fn [_ [_ file-name]]
   {:dispatch [:mannheim.ui.events/add-notification
               {:type    :error
                :content (str "nie udało sie wczytać pliku " file-name)}]}))

;;
;; * order
;;

(defn- find-cg-price [db ?product-ean]
  (get (d/entity db [:cg.warehouse/product-ean ?product-ean])
       :cg.warehouse/product-purchase-net-price :missing))


(defn- find-cg-stock [db ?product-ean]
  (get (d/entity db [:cg.warehouse/product-ean ?product-ean])
       :cg.warehouse/product-stock 0.0))

(defn- chose-best-price [?dc-price ?cg-price]
  (m/match [?dc-price ?cg-price]
    [(m/or nil :missing)
     (m/or nil :missing)]         ::none
    [:missing (m/pred number?)]   :cg
    [(m/pred number?) :missing]   :ec
    [(m/and ?x (m/pred number?))
     (m/and ?y (m/pred number?))] (if (< ?x ?y) :ec :cg)))

(rf/reg-event-fx
 ::set-market-id
 (fn [_ [_ market-id]]
   {:transact [[:db/add [:mannheim/type :mannheim/settings] :dc-order/market-id (keyword market-id)]]}))

(rf/reg-event-fx
 ::set-order-qty
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ eid qty]]
   (when-not (neg? qty)
     (let [supply-days (d/q '[:find ?supply .
                              :in $ ?e ?order-qty ?round-fn
                              :where
                              [?e :dc.warehouse.report/stock ?stock]
                              [?e :dc.warehouse.report/rate-of-sales ?rate]
                              [(+ ?stock ?order-qty) ?sum]
                              [(/ ?sum ?rate) ?supply]
                              [(?round-fn ?supply) ?supply]]
                            ds eid qty js/Math.ceil)]
       {:transact [[:db/add eid :dc.warehouse.report/order-qty qty]
                   [:db/add eid :dc.warehouse.report/supply-days supply-days]]}))))

(rf/reg-event-fx
 ::make-order-dialog.toggle-group
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ market-id group-id]]
   (let [gids      (d/q '[:find [?gid ...]
                          :in $ ?parent
                          :where
                          [(count ?parent) ?parent-count]
                          [?e :dc.warehouse.groups/group-id ?gid]
                          [(clojure.string/starts-with? ?gid ?parent)]]
                        ds group-id)
         get-eid-f (fn [ds market-id gid]
                     (d/q '[:find ?e .
                            :in $ ?market-id ?gid
                            :where
                            [?e :dc.order.settings/market-id ?market-id]
                            [?e :dc.order.settings/group-id ?gid]]
                          ds market-id gid))
         tx-data   (->> gids
                        (mapcat
                         (fn [gid]
                           (if-let [eid (get-eid-f ds market-id gid)]
                             [[:db.fn/retractEntity eid]]
                             [[:db/add gid :dc.order.settings/market-id market-id]
                              [:db/add gid :dc.order.settings/group-id gid]]))))]
     {:transact tx-data})))

(rf/reg-event-fx
 ::ui.hide-make-order-dialog
 (fn [_ _]
   {:transact [[:db/add [:mannheim/type :mannheim/settings] :dc.order.ui/show-make-order-dialog? false]]}))

(rf/reg-event-fx
 ::set-group-optimal-order-days
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ market-id group-id days]]
   (when market-id
     (e/when-lets [days* (e/as-?int days)
                   gids (d/q '[:find [?gid ...]
                               :in $ ?parent
                               :where
                               [(count ?parent) ?parent-count]
                               [?e :dc.warehouse.groups/group-id ?gid]
                               [(clojure.string/starts-with? ?gid ?parent)]]
                             ds group-id)
                   get-eid-f (fn [ds market-id gid]
                               (d/q '[:find ?e .
                                      :in $ ?market-id ?gid
                                      :where
                                      [?e :dc.warehouse.groups/market-id ?market-id]
                                      [?e :dc.warehouse.groups/group-id ?gid]]
                                    ds market-id group-id))
                   tx-data (->> gids
                                (mapcat
                                 (fn [gid]
                                   (if-let [id (get-eid-f ds market-id gid)]
                                     [[:db/add id :dc.warehouse.groups/group-optimal-order-days days*]]
                                     [[:db/add gid :dc.warehouse.groups/market-id market-id]
                                      [:db/add gid :dc.warehouse.groups/group-id gid]
                                      [:db/add gid :dc.warehouse.groups/group-optimal-order-days days*]
                                      [:db/add gid :db/path [:mannheim/dc :dc/groups]]]))))]
       {:transact tx-data}))))

(rf/reg-event-fx
 ::make-order
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ market-id file-name]]
   (let [order-cleanup-tx  (->> (d/q '[:find [?e ...]
                                       :in $ ?market-id
                                       :where
                                       [?e :dc.order/market-id ?market-id]]
                                     ds market-id)
                                (mapv (fn [?e] [:db.fn/retractEntity ?e])))
         order-tx          (->> (d/q '[:find ?e ?market-id ?product-name ?product-ean ?stock ?demand ?supply ?order ?ec-price ?cg-price ?cg-stock ?vendor
                                       :in $ ?market-id ?round-fn ?cg-price-fn ?cg-stock-fn ?chose-price-fn
                                       :where
                                       [?e :dc.warehouse.report/market-id ?market-id]
                                       [?e :dc.warehouse.report/product-name ?product-name]
                                       [?e :dc.warehouse.report/product-ean ?product-ean]
                                       [?e :dc.warehouse.report/rate-of-sales ?rate-of-sales]
                                       [?e :dc.warehouse.report/stock ?stock]
                                       [(max ?stock 0.0) ?stock]
                                       [?e :dc.warehouse.report/group-id ?gid]
                                       [?oe :dc.order.settings/market-id ?market-id]
                                       [?oe :dc.order.settings/group-id ?gid]
                                       [(get-else $ ?e :dc.warehouse.report/purchase-price :missing) ?ec-price]
                                       [(?cg-price-fn $ ?product-ean) ?cg-price]
                                       [(?cg-stock-fn $ ?product-ean) ?cg-stock]
                                       [(?chose-price-fn ?ec-price ?cg-price) ?vendor]
                                       [(not= ?vendor ::none)]
                                       [?ge :dc.warehouse.groups/group-id ?gid]
                                       [?ge :dc.warehouse.groups/group-optimal-order-days ?days]
                                       [(* ?rate-of-sales ?days) ?demand]
                                       [(?round-fn ?demand) ?demand]
                                       [(pos? ?demand)]
                                       [(- ?demand ?stock) ?order]
                                       [(?round-fn ?order) ?order]
                                       [(max 0.0 ?order) ?order]
                                       [(+ ?order ?stock) ?supply]
                                       [(/ ?supply ?rate-of-sales) ?supply]
                                       [(?round-fn ?supply) ?supply]]
                                     ds market-id
                                     js/Math.ceil find-cg-price
                                     find-cg-stock chose-best-price)
                                (into []
                                      (mapcat (fn [[?e ?market-id ?product-name ?product-ean ?stock ?demand ?supply ?order ?ec-price ?cg-price ?cg-stock ?vendor]]
                                                [[:db/add ?e :dc.order/market-id ?market-id]
                                                 [:db/add ?e :dc.order/product-name ?product-name]
                                                 [:db/add ?e :dc.order/product-ean ?product-ean]
                                                 [:db/add ?e :dc.order/stock ?stock]
                                                 [:db/add ?e :dc.order/demand ?demand]
                                                 [:db/add ?e :dc.order/supply ?supply]
                                                 [:db/add ?e :dc.order/order ?order]
                                                 [:db/add ?e :dc.order/ec-price ?ec-price]
                                                 [:db/add ?e :dc.order/cg-price ?cg-price]
                                                 [:db/add ?e :dc.order/cg-stock ?cg-stock]
                                                 [:db/add ?e :dc.order/vendor ?vendor]
                                                 [:db/add ?e :dc.order/show? true]]))))
         report-cleanup-tx (->> (d/q '[:find [?e ...]
                                       :in $ ?market-id
                                       :where [?e :dc.warehouse.report/market-id ?market-id]]
                                     ds market-id)
                                (mapv (fn [?e] [:db.fn/retractEntity ?e])))
         groups-cleanup-tx (->> (d/q '[:find [?e ...]
                                       :in $ ?market-id
                                       :where
                                       [?e :dc.order.settings/market-id ?market-id]
                                       [?e :dc.order.settings/group-id _]]
                                     ds market-id)
                                (mapv (fn [?e] [:db.fn/retractEntity ?e])))
         ui-cleanup-tx     [[:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/name-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/name-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/ean-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/ean-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/stock-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/stock-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/demand-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/demand-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/supply-days-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/supply-days-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/order-qty-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/order-qty-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/ec-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/ec-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/cg-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/cg-query-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/cg-stock]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/cg-stock-tmp]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/vendor-query]
                            [:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/vendor-query-tmp]]
         tx-data           (e/into-all []
                                       order-cleanup-tx report-cleanup-tx ui-cleanup-tx
                                       groups-cleanup-tx order-tx)]
     {:transact tx-data})))

(rf/reg-event-fx
 ::set-supply-days
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ eid days]]
   (when (pos? days)
     (let [order-qty (d/q '[:find ?order-qty .
                            :in $ ?e ?days ?round-fn
                            :where
                            [?e :dc.warehouse.report/stock ?stock]
                            [?e :dc.warehouse.report/rate-of-sales ?rate]
                            [(* ?days ?rate) ?order-qty]
                            [(?round-fn ?order-qty) ?order-qty]]
                          ds eid days js/Math.ceil)]
       {:transact [[:db/add eid :dc.warehouse.report/order-qty order-qty]
                   [:db/add eid :dc.warehouse.report/supply-days days]]}))))

(def ^:private fn-map
  {">"  >
   "<"  <
   "="  =
   ">=" >=
   "<=" <=})

(defn- number->query [s]
  (if (not-empty s)
    (as-> s s
      (str/split s " ")
      (partition-all 2 s)
      (mapcat (fn [coll]
                (if (= (count coll) 2)
                  [(get fn-map (first coll) =) (or (e/as-?float (second coll)) 0.00)]
                  [= (or (e/as-?float (first coll)) 0.00)])) s))
    [(constantly true) js/NaN]))


(rf/reg-event-fx
 ::filter-order
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} _]
   (println ::filter-order)
   (let [name        (d/q '[:find ?v . :where [?e :dc.order.ui/name-query ?v]] ds)
         ean         (d/q '[:find ?v . :where [?e :dc.order.ui/ean-query ?v]] ds)
         stock       (-> (d/q '[:find ?v . :where [?e :dc.order.ui/stock-query ?v]] ds) (number->query))
         demand      (-> (d/q '[:find ?v . :where [?e :dc.order.ui/demand-query ?v]] ds) (number->query))
         supply      (-> (d/q '[:find ?v . :where [?e :dc.order.ui/supply-days-query ?v]] ds) (number->query))
         order-qty   (-> (d/q '[:find ?v . :where [?e :dc.order.ui/order-qty-query ?v]] ds) (number->query))
         ec-price    (-> (d/q '[:find ?v . :where [?e :dc.order.ui/ec-query ?v]] ds) (number->query))
         cg-price    (-> (d/q '[:find ?v . :where [?e :dc.order.ui/cg-query ?v]] ds) (number->query))
         cg-stock    (-> (d/q '[:find ?v . :where [?e :dc.order.ui/cg-stock-query ?v]] ds) (number->query))
         vendor      (d/q '[:find ?v . :where [?e :dc.order.ui/vendor-query ?v]] ds)
         hide-all-tx (->> (d/q '[:find [?e ...]
                                 :where [?e :dc.order/show? true]]
                               ds)
                          (mapv (fn [eid] [:db/add eid :dc.order/show? false])))
         filter-tx   (->> (d/q '[:find [?e ...]
                                 :in $ [?q-name ?q-ean
                                        [?fn-stock ?q-stock]
                                        [?fn-demand ?q-demand]
                                        [?fn-supply ?q-supply]
                                        [?fn-order-qty ?q-order-qty]
                                        [?fn-ec-price ?q-ec-price]
                                        [?fn-cg-price ?q-cg-price]
                                        [?fn-cg-stock ?q-cg-stock]
                                        ?q-vendor]
                                 :where
                                 ;; filter name
                                 [(str ?q-name) ?q-name]
                                 [?e :dc.order/product-name ?product-name]
                                 [(re-pattern ?q-name) ?name-pattern]
                                 [(re-find ?name-pattern ?product-name)]
                                 ;; filter ean
                                 [(str ?q-ean) ?q-ean]
                                 [?e :dc.order/product-ean ?product-ean]
                                 [(re-pattern ?q-ean) ?ean-pattern]
                                 [(re-find ?ean-pattern ?product-ean)]
                                 ;; filter stock
                                 [?e :dc.order/stock ?stock]
                                 [(?fn-stock ?stock ?q-stock)]
                                 ;; filter demand
                                 [?e :dc.order/demand ?demand]
                                 [(?fn-demand ?demand ?q-demand)]
                                 ;; filter supply
                                 [?e :dc.order/supply ?supply]
                                 [(?fn-supply ?supply ?q-supply)]
                                 ;; filter order-qty
                                 [?e :dc.order/order ?order-qty]
                                 [(?fn-order-qty ?order-qty ?q-order-qty)]
                                 ;; filter ec-price
                                 [?e :dc.order/ec-price ?ec-price]
                                 [(?fn-ec-price ?ec-price ?q-ec-price)]
                                 ;; filter cg-price
                                 [?e :dc.order/cg-price ?cg-price]
                                 [(?fn-cg-price ?cg-price ?q-cg-price)]
                                 ;; filter cg-stock
                                 [?e :dc.order/cg-stock ?cg-stock]
                                 [(?fn-cg-stock ?cg-stock ?q-cg-stock)]
                                 ;; filter vendor
                                 [(str ?q-vendor) ?q-vendor]
                                 [?e :dc.order/vendor ?vendor]
                                 [(str ?vendor) ?vendor]
                                 [(re-pattern ?q-vendor) ?vendor-pattern]
                                 [(re-find ?vendor-pattern ?vendor)]]
                               ds [name ean stock demand supply order-qty
                                   ec-price cg-price cg-stock vendor])
                          (mapv (fn [eid] [:db/add eid :dc.order/show? true])))]
     {:transact (into hide-all-tx filter-tx)})))

(comment
  (rf/dispatch [::filter-order])
  )

(rf/reg-event-fx
 ::remove-product
 (fn [_ [_ eid]]
   {:transact [[:db.fn/retractEntity eid]]}))

(rf/reg-event-fx
 ::select-product
 (fn [_ [_ eid]]
   {:transact [[:db/add [:mannheim/type :mannheim/ui] :dc.order.ui/selected-products eid]]}))

(rf/reg-event-fx
 ::unselect-product
 (fn [_ [_ eid]]
   {:transact [[:db/retract [:mannheim/type :mannheim/ui] :dc.order.ui/selected-products eid]]}))

(rf/reg-event-fx
 ::toggle-product
 [(rf/inject-cofx :ds)]
 (fn [{:keys [ds]} [_ id]]
   (let [selected (set (d/q '[:find [?v ...]
                              :where [?e :dc.order.ui/selected-products ?v]]
                            ds))]
     {:dispatch (if-not (contains? selected id)
                  [::select-product id]
                  [::unselect-product id])})))
