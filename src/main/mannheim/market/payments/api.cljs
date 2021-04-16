(ns mannheim.market.payments.api
  (:require
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [cuerdas.core :as str]
   [malli.core :as mc]
   [malli.transform :as mt]
   [mannheim.api :as api]
   [applied-science.js-interop :as j]
   [mannheim.market.specs :as market.specs]))

(def fs (js/require "fs"))
(def iconv (js/require "iconv-lite"))

(def document-keys
  [:document/issue-date
   :document/payment-date
   :document/accounting-date
   :document/contractor-id
   :document/gross-value
   :document/gross-value
   :document/net-value
   :document/contractor-name
   :document/type
   :document/id
   :document/transfer-date])

(def document-header
  ["Data wyst. dokumentu"
   "Data zapłaty"
   "Data księgowania"
   "NIP kontrahenta"
   "Wartość dokumentu brutto"
   "Wartość w cenach zakupu brutto"
   "Wartość dokumentu"
   "Kontrahent"
   "Typ"
   "Numer"
   "Data opłacenia"])

(def header-map
  (zipmap document-header document-keys))

(defn read-file [path]
  (let [s (.readFileSync fs path)]
    (.decode iconv s "cp1250")))

(comment
  (->> (read-file "/home/ribelo/Public/rk/payments/f01752.txt")
       str/lines
       (take 2)
       (mapv #(str/split % "\t"))))

(defn- data-valid? [data]
  (or (enc/revery? #(re-find (re-pattern %) data) (map document-header #{0 1 2 3 4 6 7 8 9}))
      (enc/revery? #(re-find (re-pattern %) data) (map document-header #{0 2 3 5 7 9}))))

(defn raw-data->maps [raw]
  (let [lines           (str/lines raw)
        header          (map str/trim (-> (first lines) (str/split "\t")))]
    (->> lines
         (into []
               (comp
                (drop 1)
                (map (fn [s]
                       (->> (str/split s "\t")
                            (mapv #(-> % (str/trim) (str/replace "\"" ""))))))
                (map #(zipmap header %))
                (map #(enc/rename-keys header-map %))
                (map market.specs/->document)
                (filter market.specs/document?))))))

(defmethod api/event ::read-file
  [[_ path]]
  (-> (read-file path) raw-data->maps))
