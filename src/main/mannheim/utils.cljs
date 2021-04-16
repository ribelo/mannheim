(ns mannheim.utils
  (:require
   [taoensso.encore :as e]
   [cljs-bean.core :refer [bean ->clj ->js]]
   [taoensso.timbre :as timbre]
   [mannheim.electron.core :as el]
   [applied-science.js-interop :as j]
   [cuerdas.core :as str]))

(def xlsx (el/require "xlsx"))

(defn excel->json [file sheet-name]
  (let [workbook (j/call xlsx :readFile file)
        sheet (j/get-in workbook [:Sheets sheet-name])]
    (j/call-in xlsx [:utils :sheet_to_json] sheet)))
