(ns mannheim.cg.events
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as e]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [taoensso.timbre :as timbre]
   [mannheim.electron.core :as el :refer [fs]]
   [mannheim.electron.fx]
   [net.cgrand.xforms :as x]
   [cuerdas.core :as str]
   [datascript.core :as d]
   [applied-science.js-interop :as j]
   [cuerdas.core :as str]))

(def iconv (el/require "iconv-lite"))

(def report-keys
  [:cg.warehouse/product-name
   :cg.warehouse/product-ean
   :cg.warehouse/product-stock
   :cg.warehouse/product-purchase-net-price
   :cg.warehouse/product-sell-net-price-1
   :cg.warehouse/product-sell-net-price-2])

(defn read-file [path]
  (let [s (j/call fs :readFileSync path)]
    (j/call iconv :decode s "cp1250")))

(defn- raw-data->maps [raw]
  (let [lines (str/lines raw)]
    (->> lines
         (into []
               (comp
                (map (fn [s]
                       (->> (str/split s ";")
                            (mapv #(-> % (str/trim) (str/replace "\"" ""))))))
                (map (fn [coll] (zipmap report-keys coll)))
                (map (fn [m]
                       (let [m* (-> m
                                    (assoc  :db/path [:mannheim/cg :cg/warehouse])
                                    (update :cg.warehouse/product-name str/lower)
                                    (update :cg.warehouse/product-stock e/as-?float)
                                    (update :cg.warehouse/product-purchase-net-price e/as-?float)
                                    (update :cg.warehouse/product-purchase-sell-price-1 e/as-?float)
                                    (update :cg.warehouse/product-purchase-sell-price-2 e/as-?float))]
                         (e/filter-vals identity m*)))))))))

(rf/reg-event-fx
 ::read-cg-file
 [(rf/inject-cofx :ds)
  (rf/inject-cofx :dialog)]
 (fn [{:keys [db ds dialog]} _]
   (when-not (empty? dialog)
     (let [raw-data (read-file (first dialog))]
       (let [coll (raw-data->maps raw-data)]
         {:transact coll
          :dispatch [::read-cg-file.successful (first dialog)]})))))

(rf/reg-event-fx
 ::read-cg-file.successful
 (fn [_ [_ file-path]]
   {:dispatch [:mannheim.ui.events/add-notification
               [:title   nil
                :type    :success
                :content (str "poprawnie wczytano plik " file-path)]]}))

(rf/reg-event-fx
 ::read-cg-file.invalid-data
 (fn [_ [_ file-path]]
   {:dispatch [:mannheim.ui.events/add-notification
               [:title   nil
                :type    :error
                :content (str "nie udało sie wczytać pliku " file-path)]]}))

(comment
  (d/q '[:find [?e ...]
         :where
         [?e :cg.warehouse/product-ean ?ean]]
       @@re-posh.db/store)
  )
