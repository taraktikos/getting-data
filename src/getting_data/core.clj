; REPL:
; (require `getting-data.core :verbose :reload)
; (load-data (str "http://www.ericrochester.com/" "clj-data-analysis/data/small-sample-table.html"))
; (load-data2 (str "http://www.ericrochester.com/" "clj-data-analysis/data/small-sample-list.html"))

(ns getting-data.core

(:require
  [clojure.string :as string]
  [net.cgrand.enlive-html :as html]
  [incanter.core :as i])
(:import [java.net URL]))


(defn to-keyword
  [input]
  (-> input string/lower-case (string/replace \space \-) keyword))

(defn load-data
  [url]
  (let [page (html/html-resource (URL. url))
        table (html/select page [:table#data])
        headers (->>
                  (html/select table [:tr :th])
                  (map html/text)
                  (map to-keyword)
                  vec)
        rows (->> (html/select table [:tr])
                  (map #(html/select % [:td]))
                  (map #(map html/text %))
                  (filter seq))]
    (i/dataset headers rows)))


(defn get-family
  [article]
  (string/join
    (map html/text (html/select article [:header :h2]))))

(defn get-person
  [li]
  (let [[{pnames :content} rel] (:content li)]
    {:name (apply str pnames)
     :relationship (string/trim rel)}))

(defn get-rows
  [article]
  (let [family (get-family article)]
    (map #(assoc % :family family)
         (map get-person
              (html/select article [:ul :li])))))

(defn load-data2
  [html-url]
  (let [html (html/html-resource (URL. html-url))
        articles (html/select html [:articles])]
    (i/to-dataset (mapcat get-rows articles))))

;(defn -main (println "hello"))