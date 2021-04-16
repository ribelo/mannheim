(ns firenze.realtime-database
  (:refer-clojure :exclude [set update remove])
  (:require
   ["firebase/app" :as firebase]
   ["firebase/database"]
   [applied-science.js-interop :as j]
   [cljs-bean.core :as bean :refer [->clj ->js]]
   [clojure.string :as str]
   [datascript.transit :as dt]))

(defn- encode-key [k]
  (-> k ->js (str/replace #"\." "_") (str/replace #"/" ":")))

(defn- decode-key [s]
  (-> s (str/replace #"_" ".") (str/replace #":" "/")))

(defn database [] (j/call firebase :database))

(defn server-timestamp []
  (j/get-in firebase [:database :ServerValue :TIMESTAMP]))

(defmulti ->path (fn [x] (type x)))

(defmethod ->path cljs.core/PersistentVector
  [path]
  (str/join "/" (mapv encode-key path)))

(defmethod ->path cljs.core/Keyword
  [path]
  (encode-key path))

(defmethod ->path js/String
  [path]
  path)

(defn ref [path]
  (-> (database)
      (j/call :ref (->path path))))

(defn set
  ([path doc]
   (set path doc {}))
  ([path doc {:keys [on-success on-failure]}]
   (-> (ref path)
       (j/call :set (dt/write-transit-str doc))
       (cond-> on-success
         (j/call :then on-success))
       (cond-> on-failure
         (j/call :catch #(on-failure %))))))

(defn push
  ([path]
   (-> (ref path)
       (j/call :push)
       (j/get :key)))
  ([path doc]
   (push path doc {}))
  ([path doc {:keys [on-success on-failure]}]
   (-> (ref path)
       (j/call :push (dt/write-transit-str doc))
       (cond-> on-success
         (j/call :then on-success))
       (cond-> on-failure
         (j/call :catch #(on-failure %))))))

(defn update
  ([path doc]
   (update path doc {}))
  ([path doc {:keys [on-success on-failure]}]
   (-> (ref path)
       (j/call :update (dt/write-transit-str doc))
       (cond-> on-success
         (j/call :then on-success))
       (cond-> on-failure
         (j/call :catch #(on-failure %))))))

(defn once
  ([path cb]
   (once path cb {}))
  ([path cb {:keys [on-failure]}]
   (-> (ref path)
       (j/call :once "value" (fn [snap] (cb (persistent!
                                             (reduce-kv
                                              (fn [acc k v]
                                                (assoc! acc k (dt/read-transit-str v)))
                                              (transient {})
                                              (->clj (j/call snap :val)))))))
       (cond-> on-failure
         (j/call :catch #(on-failure %))))))

(defn- -on [event path cb {:keys [on-failure]}]
  (-> (ref path)
      (j/call :on event
              (fn [snap & _] (cb (dt/read-transit-str (->clj (j/call snap :val))))))
      (cond-> on-failure
        (j/call :catch #(on-failure %)))))

(defmulti on (fn [event path cb opts] event))

(defmethod on :value
  [event path cb {:keys [on-failure]}]
  (-> (ref path)
      (j/call :on "value"
              (fn [snap] (cb (persistent!
                              (reduce-kv
                               (fn [acc k v]
                                 (assoc! acc k (dt/read-transit-str v)))
                               (transient {})
                               (->clj (j/call snap :val)))))))
      (cond-> on-failure
        (j/call :catch #(on-failure %)))))

(defmethod on :child-added
  [event path cb {:keys [on-failure] :as opts}]
  (-on "child_added" path cb opts))

(defmethod on :child-removed
  [event path cb {:keys [on-failure] :as opts}]
  (-on "child_removed" path cb opts))

(defmethod on :child-changed
  [event path cb {:keys [on-failure] :as opts}]
  (-on "child_changed" path cb opts))

(defn off [path]
  (-> (ref path)
      (j/call :off)))

(defn remove
  ([path]
   (remove path {}))
  ([path {:keys [on-success on-failure]}]
   (-> (ref path)
       (j/call :remove)
       (cond-> on-success
         (j/call :then on-success))
       (cond-> on-failure
         (j/call :catch #(on-failure %))))))
