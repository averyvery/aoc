(ns aoc-01.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pprint])
  (:gen-class))

(defn -main
  "I don't do a whole lot."
  []
  (let [costs (as-> "input.txt" v
                    (io/resource v)
                    (io/file v)
                    (slurp v)
                    (str/split v #"\n")
                    (map read-string v))]
    (println "Hello, World!")))
