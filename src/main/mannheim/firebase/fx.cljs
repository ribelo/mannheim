(ns mannheim.firebase.fx
  (:require
   [re-frame.core :as rf]
   [re-frame.registrar]
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [meander.epsilon :as m]
   [ribelo.firenze :as firenze]
   [ribelo.firenze.realtime-database :as rdb]
   [ribelo.doxa :as dx]
   [editscript.core :as es]
   [editscript.edit :as ese]
   [applied-science.js-interop :as j]
   [shadow.resource :as rc]
   [mannheim.transit :refer [write-transit read-transit]]
   [cljs.reader :as edn]
   [promesa.core :as p]))

(defn- -download-store [store]
  (dx/with-dx [dx_ store]
    [(rdb/once [store :data] (fn [data] (swap! dx_ (fn [db] (with-meta data (meta db))))))
     (rdb/once [store :txs ] (fn [m   ] (swap! dx_ (fn [db] (with-meta db m)))))]))

(rf/reg-fx
 ::download-store
 (fn [store]
   (timbre/info :download-store store)
   (dx/with-dx [dx_ store]
     (let [ps (-download-store store)
           k  (enc/merge-keywords [:firebase.download store])]
       (dx/commit! dx_ [:dx/put [:app.settings/id k] {:promises ps}])
       (-> (p/all ps)
           (p/then  (fn [   ] (rf/dispatch [:mannheim.firebase.events/download-store.success store    ])))
           (p/catch (fn [err] (rf/dispatch [:mannheim.firebase.events/download-store.failure store err]))))))))

(rf/reg-fx
 ::init-firebase
 (fn [app-info]
   (timbre/info :init-firebase)
   (when app-info (firenze/initialize-app app-info))))

(defn- -patch-rdb [store db]
  (let [tx (:tx (meta db))
        t  (:t  (meta db))
        h  (:h  (meta db))
        m  {:t t :tx tx :h h}
        it (iter tx)]
    (timbre/debug :patch-firebase m)
    (rdb/push [store :txs] m)
    (loop []
      (when (.hasNext it)
        (let [elem (.next it)]
          (m/find elem
            [[?tid] :+ {?eid ?m}]
            (rdb/set [store :data ?tid ?eid] ?m)
            ;;
            [[?tid ?eid] :+ {:as ?m}]
            (rdb/set [store :data ?tid ?eid] ?m)
            ;;
            [[?tid ?eid ?k] :+ ?v]
            (rdb/set [store :data ?tid ?eid ?k] ?v)
            ;;
            [[?tid ?eid ?k ?i] :+ ?v]
            (rdb/set [store :data ?tid ?eid ?k] (get-in db [?tid ?eid ?k]))
            ;;
            [[?tid ?eid] :-]
            (rdb/remove [store :data ?tid ?eid])
            ;;
            [[?tid ?eid ?k] :-]
            (rdb/remove [store :data ?tid ?eid ?k])
            ;;
            [[?tid ?eid ?k ?i] :-]
            (enc/cond
              :let [v (get-in db [?tid ?eid ?k])]
              (some? v) (rdb/set    [store :data ?tid ?eid ?k] v)
              (nil?  v) (rdb/remove [store :data ?tid ?eid ?k]))
            ;;
            [[?tid ?eid ?k] :r ?v]
            (rdb/set [store :data ?tid ?eid ?k] ?v)
            ;;
            ?x (throw (ex-info "bad txs" {:txs ?x})))
          (recur))))))

(defn- -patch-store [store m]
  (dx/with-dx [dx_ store]
    (let [{:keys [tx h t]} (meta @dx_)
          it (iter m)]
      (loop [valid? false]
        (enc/cond
          :if-not tx ((re-frame.registrar/get-handler :fx ::download-store) :market)
          (and valid? (not (.hasNext it)))
          nil
          (and (not valid?) (not (.hasNext it)))
          (do (timbre/warnf "outdated dx %s" store)
              (-download-store store))
          :let [[_ {:keys [tx' h' t']}] (.next it)]
          (identical? t' t)
          (recur true)
          (and valid? (> t' t))
          (do (dx/patch! dx_ tx' t') (recur valid?))
          :else (recur valid?))))))

(rf/reg-fx
 ::patch-store
 (fn [store]
   (timbre/info :patch-store)
   (-> (rdb/query [store :txs] [:limit-to-last 128])
       (rdb/once (fn [m] (-patch-store store m))))))

(rf/reg-fx
 ::sync
 (fn [store]
   (timbre/info :sync store)
   (dx/with-dx [db_ store]
     (dx/listen! @db_ (enc/merge-keywords [:sync/rdb store]) (partial -patch-rdb store)))
   (-> (rdb/query [store :txs] [:limit-to-last 128])
       (rdb/on (fn [m] (-patch-store store m))))))

(rf/reg-fx
 ::unsync
 (fn [store]
   (timbre/info :unsync store)
   (dx/with-dx [db_ store]
     (dx/unlisten! @db_ (enc/merge-keywords [:sync/rdb store])))
   (rdb/off [store :txs])))
