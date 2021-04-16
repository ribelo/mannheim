(ns electron.core
  (:require [cljs.nodejs :refer [require]]))

(def electron (js/require "electron"))
(def app (.-app electron))
(def browser-window (.-BrowserWindow electron))
(def crash-reporter (.-crashReporter electron))

(def main-window (atom nil))

(defn init-browser []
  (reset! main-window (browser-window.
                       #js {:width           800
                            :height          600
                            :minWidth        800
                            :minHeight       600
                            :autoHideMenuBar true
                            :useContentSize  true
                            :fullscreen      false
                            :webPreferences  #js{:enableRemoteModule      true
                                                 :contextIsolation        false
                                                 :nodeIntegration         true
                                                 :nodeIntegrationInWorker true}}))
  (.maximize ^js/electron.BrowserWindow @main-window)
                                        ;(.setMenu @main-window nil)
                                        ; Path is relative to the compiled js file (main.js in our case)
                                        ;(.log js/console (str "file://" js/__dirname "/public/index.html"))
                                        ;(.loadURL @main-window "http://localhost:3449/index.html")
  (.loadURL ^js/electron.BrowserWindow @main-window (str "file://" js/__dirname "/public/index.html"))
  (.on ^js/electron.BrowserWindow @main-window "closed" #(reset! main-window nil)))

                                        ;(.start crash-reporter
                                        ;        (clj->js
                                        ;          {:companyName "MyAwesomeCompany"
                                        ;           :productName "MyAwesomeApp"
                                        ;           :submitURL   "https://example.com/submit-url"
                                        ;           :autoSubmit  false}))

(defn main []
  (.on app "ready" init-browser)
  (.on app "window-all-closed" #(when-not (= js/process.platform "darwin")
                                  (.quit app))))
