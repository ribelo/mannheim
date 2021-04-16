(ns mannheim.file-storage.fx
  (:require
   [re-frame.core :as rf]
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [applied-science.js-interop :as j]
   [ribelo.doxa :as dx]
   [mannheim.transit :refer [write-transit read-transit]]))

(def fs (js/require "fs"))

(rf/reg-fx
 ::write
 (fn [store]
   (dx/with-dx [db_ store]
     (if db_
       (let [file-name (enc/format "./data/%s.transit" (name store))]
         (when-not (j/call fs :existsSync "./data") (j/call fs :mkdirSync "./data"))
         (j/call fs :writeFile
                 file-name
                 (write-transit @db_)
                 (fn [err]
                   (if err
                     (timbre/error err)
                     (timbre/infof "")))))
       (timbre/warnf "can't save store: %s, the store does not exist" store)))))

(rf/reg-cofx
 ::file-storage
 (fn [cofx storage]
   (let [file-name (enc/format "./data/%s.transit" (name storage))
         data      (when (j/call fs :existsSync file-name)
                     (read-transit (j/call fs :readFileSync file-name)))]
     (assoc cofx storage data))))
