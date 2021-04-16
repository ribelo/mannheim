(ns mannheim.electron.fx
  (:require
   [re-frame.core :as rf]
   [applied-science.js-interop :as j]
   [cljs-bean.core :refer [->js ->clj]]
   [mannheim.electron.core :as el]))

(rf/reg-cofx
 :dialog
 (fn [cofx]
   (assoc cofx :dialog
          (->clj (j/call el/dialog :showOpenDialogSync
                         (->js {:properties [:openFile :multiSelections]}))))))
