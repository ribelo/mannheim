(ns mannheim.electron.core
  (:refer-clojure :exclude [require])
  (:require
   [applied-science.js-interop :as j]))

(defonce electron (js/require "electron"))
(defonce remote (j/get electron :remote))
(defonce app (j/get remote :app))
(defonce browser-window (j/get remote :BrowserWindow))
(defonce current-window (j/call remote :getCurrentWindow))
(defonce ipc-renderer (j/get electron :ipcRenderer))
(defonce dialog (j/get remote :dialog))

(comment (j/call dialog :showOpenDialog))
