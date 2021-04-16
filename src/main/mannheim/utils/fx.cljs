(ns mannheim.utils.fx
  (:require
   [datascript.core :as d]
   [re-frame.core :as rf]
   [re-frame.router :as router]
   [taoensso.encore :as e]))

(def debounced-effects (atom {}))

(defn clear-debounced-effect [id]
  (some-> (get @debounced-effects id) (e/tf-cancel!))
  (swap! debounced-effects dissoc id))

(defn add-debounced-effect [id ms f]
  (let [timeout (e/call-after-timeout ms (fn [] (clear-debounced-effect id) (f)))]
    (swap! debounced-effects assoc id timeout)))

(rf/reg-fx
 :dispatch-debounce
 (fn [coll]
   (doseq [{:keys [id ms dispatch]} coll]
     (clear-debounced-effect [::dispatch id])
     (add-debounced-effect [::dispatch id] ms #(router/dispatch dispatch)))))

(rf/reg-fx
 :transact-debounce
 (fn [coll]
   (doseq [{:keys [id ms tx-data]} coll]
     (clear-debounced-effect [::transact id])
     (add-debounced-effect [::transact id] ms #(d/transact! nil tx-data)))))
