(ns mannheim.market.payments.api
  (:require
   [taoensso.encore :as enc]
   [taoensso.timbre :as timbre]
   [net.cgrand.xforms :as x]
   [cuerdas.core :as str]
   [malli.core :as mc]
   [malli.transform :as mt]
   [mannheim.api :as api]
   [applied-science.js-interop :as j]
   [mannheim.market.specs :as market.specs]
   [ribelo.danzig :as dz :refer [=>>]]))

(def fs (js/require "fs"))
(def iconv (js/require "iconv-lite"))

(defn read-file [path]
  (let [s (.readFileSync fs path)]
    (.decode iconv s "cp1250")))

(def market-payments-keys
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

(def market-payments-header
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

(def market-payments-header-map
  (zipmap market-payments-header market-payments-keys))

(defn- ^boolean valid-market-payments? [^js buffer]
  (let [header (-> buffer str/lines first)]
    (or (enc/revery? #(re-find (re-pattern %) header) (mapv market-payments-header #{0 1 2 3 4 6 7 8 9}))
        (enc/revery? #(re-find (re-pattern %) header) (mapv market-payments-header #{0 2 3 5 7 9})))))

(comment
  (-> (read-file "/home/ribelo/Public/rk/payments/f01752.txt")
      (valid-market-payments?)))

(def pxec-keys
  [:pxec/id
   :document/id
   :document/accounting-date
   :document/transfer-date])

(def pxec-headers
  ["NumerPaczki"
   "NumerFaktury"
   "DataWystawienia"
   "DataRozliczenia"
   "NumerZew"
   "Kwota"
   "Splata"])

(def pxec-header-map
  (zipmap pxec-headers pxec-keys))

(defn ^boolean valid-pxec?
  [buffer]
  (enc/revery? #(re-find (re-pattern %) (-> buffer str/lines first)) pxec-headers))


(defn ^boolean buffer->market-payments [buffer]
  (let [lines           (str/lines buffer)
        header          (mapv str/trim (-> (first lines) (str/split "\t")))]
    (=>> lines
         (drop 1)
         (dz/with  (fn [s]
                (->> (str/split s "\t")
                     (mapv #(-> % (str/trim) (str/replace "\"" ""))))))
         (dz/with  #(zipmap header %))
         (dz/with  #(enc/rename-keys market-payments-header-map %))
         (dz/with  market.specs/->document)
         (dz/where market.specs/document?))))

(comment
  (-> (read-file "/home/ribelo/Public/rk/payments/f01752.txt")
      (buffer->market-payments)))

(defn buffer->pxec [buffer]
  (let [lines (str/lines buffer)
        header (-> (first lines) (str/split ";"))]
    (=>> lines
         (drop 1)
         (dz/with  #(str/split % ";"))
         (dz/with  #(zipmap header %))
         (dz/with  #(enc/rename-keys pxec-header-map %))
         (dz/with  market.specs/->pxec)
         (dz/where market.specs/pxec?))))

(comment
  (->> (read-file "/home/ribelo/Public/rk/paczki_ecsa/PXEC_21_0607357.csv")
       (buffer->pxec)))

(defmethod api/event ::read-file
  [[_ path]]
  (let [buffer (read-file path)]
    (enc/cond
      (valid-market-payments? buffer)
      [:market-payments (buffer->market-payments buffer)]
      ;;
      (valid-pxec? buffer)
      [:pxec (buffer->pxec buffer)])))
