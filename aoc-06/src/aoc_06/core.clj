(ns aoc-06.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as c.set]
            [clojure.pprint :as pprint])
  (:gen-class))

; Elapsed time: 14.208578 msecs
(defn- count-yesses-v1 []
  (let [passes (as-> "input.txt" v
                     (io/resource v)
                     (io/file v)
                     (slurp v)
                     (str/split v #"\n\n")
                     (map #(str/replace % #"\n" "") v)
                     (map set v)
                     (map count v)
                     (apply + v))]
    passes))

; Elapsed time: 20.732331 msecs
(defn- count-yesses []
  (let [passes (as-> "input.txt" v
                     (io/resource v)
                     (io/file v)
                     (slurp v)
                     (str/split v #"\n\n")
                     (map #(str/split % #"\n") v)
                     (map #(map set %) v)
                     (map #(apply c.set/intersection %) v)
                     (map count v)
                     (apply + v))]
    passes))

(defn -main []
  (time (pprint/pprint (count-yesses))))
