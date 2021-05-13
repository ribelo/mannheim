(ns mannheim.file-storage.fx
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [applied-science.js-interop :as j]
   [ribelo.doxa :as dx]
   [mannheim.transit :refer [write-transit read-transit]]))

(def fs (js/require "fs"))

(defn- -write-to-file [file-name data]
  (when-not (j/call fs :existsSync "./data") (j/call fs :mkdirSync "./data"))
  (j/call
   fs :writeFile
   file-name
   (write-transit data)
   (fn [err]
     (if err
       (timbre/error err)
       (timbre/infof "")))))

(rf/reg-fx
 ::write
 (fn [store]
   (dx/with-dx [dx_ store]
     (if dx_
       (let [file-name (enc/format "./data/%s.transit" (name store))]
         (-write-to-file file-name (vary-meta @dx_ dissoc :listeners)))
       (timbre/warnf "can't save store: %s, the store does not exist" store)))))

(rf/reg-fx
 ::read
 (fn [store]
   (timbre/info ::read store)
   (let [file-name (enc/format "./data/%s.transit" (name store))]
     (if (j/call fs :existsSync file-name)
       (let [data (read-transit (j/call fs :readFileSync file-name))]
         (dx/with-dx [dx_ store]
           (if dx_
             (swap! dx_ (fn [] data))
             (timbre/warnf "can't replace store: %s, the store does not exist" store))))
       (timbre/warnf "can't replace store: %s, the file does not exist" store)))))

(rf/reg-fx
 ::sync
 (fn [store]
   (timbre/info ::sync store)
   (let [file-name (enc/format "./data/%s.transit" (name store))]
     (dx/with-dx [dx_ store]
       (dx/listen! @dx_ (enc/merge-keywords [:sync/file store])
                   (fn [db]
                     (tap> [:sync :db db])
                     (when db
                       (-write-to-file file-name (vary-meta db dissoc :listeners)))))))))

(rf/reg-cofx
 ::file-storage
 (fn [cofx storage]
   (let [file-name (enc/format "./data/%s.transit" (name storage))
         data      (when (j/call fs :existsSync file-name)
                     (read-transit (j/call fs :readFileSync file-name)))]
     (assoc cofx storage data))))

(comment
  (ribelo.doxa/with-dx [dx_ :market]
    (write-transit (vary-meta @dx_ dissoc :listeners)))
  )
