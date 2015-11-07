; REPL: (require `ch1.importing :verbose :reload)
; (load-data (str "http://www.ericrochester.com/" "clj-data-analysis/data/small-sample-table.html"))

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

;(defn -main (println "hello"))