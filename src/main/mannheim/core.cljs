(ns mannheim.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rd]
   [re-frame.core :as rf]
   [taoensso.timbre :as timbre]
   [mannheim.init :as init]
   ;; [mannheim.ui :as ui]
   ))

(defn mount-components []
  (timbre/info :mount-components)
  ;; (rd/render [#'ui/view] (.getElementById js/document "app"))
  )

(defn ^:export init []
  (timbre/info :init)

  (timbre/set-level! :debug)
  (rf/dispatch-sync [::init/boot])
  (mount-components))
