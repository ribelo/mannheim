(ns mannheim.worker
  (:refer-clojure :exclude [-reset!])
  (:require
   [applied-science.js-interop :as j]
   [mannheim.transit :as t]
   [taoensso.encore :as enc]
   [sci.core :as sci]
   [mannheim.api :as api]
   [mannheim.market.payments.api]))

(def ^:private sci-opts
  {:namespaces {'api {'event api/event}}})

(defn init []
  (js/self.addEventListener
   "message"
   (fn [^js e]
     (tap> {:e e})
     (enc/cond
       :let [msg (t/read-transit (j/get e :data))]
       (list? msg)
       (let [[ok err] (enc/catch-errors (sci/eval-string (str msg) sci-opts))
             resp     (if ok (t/write-transit [:ok ok]) (t/write-transit [:err {:msg  (ex-message err)
                                                                                :code (str msg)}]))]
         (js/self.postMessage resp))
       (vector? msg)
       (let [[ok err] (enc/catch-errors (api/event msg))
             resp     (if ok (t/write-transit [:ok ok]) (t/write-transit [:err {:msg  (ex-message err)
                                                                                :code (str msg)}]))]
         (js/self.postMessage resp))))))
