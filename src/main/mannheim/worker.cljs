(ns mannheim.worker
  (:require
   [applied-science.js-interop :as j]
   [mannheim.transit :as t]
   [taoensso.encore :as enc]
   [mannheim.api :as api]
   [mannheim.market.payments.api]
   [promesa.core :as p]
   [promesa.exec :as p.exec]
   [sci.core :as sci]
   [goog.dom :as gdom]
   [goog.events :as gevents]
   [goog.events.EventType]))

(comment
  (let [d (p/deferred)]
    (p/resolve! d 100)
    (-> d
        (p/then println)))


  (def gevt (gevents/Event. "sex" {:data 1}))
  (def listener (gevents/listen (gdom/getWindow) "sex" (fn [e]
                                                         (println (-> e .-target)))))
  (gevents/fireListener listener gevt)

  (def myevt (js/CustomEvent. "sex" {:data 1}))
  (js/self.addEventListener "sex" #(println :sex))
  (js/dispatchEvent myevt)
  (js/self.removeEventListener "sex")
  )

(defprotocol IWorker
  (-handle [_ e])
  (-init! [_])
  (-schedule! [_ evt])
  (dispatch! [_ event] [_ event cb])
  (stop! [_]))

(deftype Worker [path ^:mutable worker ^:mutable listener ^:mutable cnt ^:mutable uuid ^:mutable working?]
  ICounted
  (-count [_] cnt)
  IWorker
  (-handle [this e]
    (enc/cond
      :when worker
      working?
      (-schedule! this e)
      :let  [elem (.-target e)
             evt  (when elem (aget ^js elem 0))
             cb   (when elem (aget ^js elem 1))]
      (list? evt)
      (do
        (set! working? true)
        (.addEventListener worker "message"
                           (fn [^js e]
                             (let [data (t/read-transit (.-data e))]
                               (when cb (cb data))
                               (set! cnt (dec cnt))
                               (println :cnt :dec cnt)
                               (set! working? false)))
                           #js {:once true})
        (.postMessage worker
                      (t/write-transit evt)))))
  ;;
  (-init! [this]
    (when (and (not worker) path)
      (let [uuid' (enc/uuid-str)]
        (set! worker   (js/Worker. path))
        (set! listener (gevents/listen (gdom/getWindow) uuid' (partial -handle this)))
        (set! uuid      uuid'))))
  ;;
  ;;
  (-schedule! [_ e]
    (js/setTimeout #(gevents/fireListener listener e)))
  (dispatch!
    [_ event]
    (set! cnt (inc cnt))
    (let [d  (p/deferred)
          cb #(p/resolve! d %)]
      (js/setTimeout #(gevents/fireListener listener (gevents/Event. uuid #js[event cb])))
      d))
  (dispatch!
    [_ event cb]
    (set! cnt (inc cnt))
    (js/setTimeout #(gevents/fireListener listener (gevents/Event. uuid #js [event cb]))))
  (stop! [_]
    (when worker
      (.terminate worker)
      (gevents/unlistenByKey listener)
      (set! worker nil))))

(defn worker [path]
  (let [w (Worker. path nil nil 0 nil false)]
    (-init! w)
    w))

(comment
  (def wrk (worker "js/worker.js"))
  (stop! wrk)
  (dotimes [n 100]
    (dispatch! wrk '(+ 1 10) #(println :result % n))
    (println :n n :cnt (count wrk)))

  (-> (dispatch! wrk '(+ 1 10))
      (p/then println))



  (enc/qb 1
    (count (mapv inc (range 1e6)))
    (count (sci/eval-string "(mapv inc (range 1e6))")))


  (def worker (js/Worker. "js/worker.js"))
  (.addEventListener worker "message" (fn [^js e] (println :e (.-data e))))

  (.postMessage worker (t/write-transit '(+ 1 3)))
  (.terminate worker)
  (def atm (atom true))
  (-> (p/promise
       (fn [resolv reject]
         (while @atm
           )))
      (p/then print)))

(deftype WorkerPool [])

(defn init []
  (js/self.addEventListener
   "message"
   (fn [^js e]
     (enc/cond
       :let [data (t/read-transit (j/get e :data))]
       (vector? data)
       (let [[ok _err] (enc/catch-errors (api/event data))
             msg (if ok (t/write-transit ok) (t/write-transit ::err))]
         (js/self.postMessage msg))
       (list? data)
       (let [[ok _err] (enc/catch-errors (sci/eval-string (str data)))
             msg      (if ok (t/write-transit ok) (t/write-transit ::err))]
         (js/self.postMessage msg))))))
