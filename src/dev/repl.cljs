(ns mannheim.repl
  (:require
   [re-frame.core :as rf]
   [datascript.core :as d]
   [datascript.transit :as dt]
   [taoensso.encore :as e]
   [mannheim.dc-order.groups]
   [cljs-bean.core :refer [->js ->clj]]
   [applied-science.js-interop :as j]
   [mannheim.electron.core :refer [fs]]
   [ribelo.fireposh.fx :as fireposh.fx]
   [ribelo.firenze.realtime-database :as rdb]
   [taoensso.timbre :as timbre]))

(d/transact! @re-posh.db/store
             mannheim.dc-order.groups/groups-ids)
(rf/dispatch [:ribelo.fireposh.events/set-schema mannheim.db/schema])
(rf/dispatch [:mannheim.file-storage.events/write])
(rf/dispatch [:mannheim.dc-order.events/ui.set-sort-order :dc.warehouse.report/product-name])

