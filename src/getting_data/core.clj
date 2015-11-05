(ns getting-data.core)

(use 'incanter.core
	 'incanter.io)

(deft read_csv
	(read-dataset "data/data.csv"))



(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
