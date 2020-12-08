(ns aoc-01.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pprint])
  (:gen-class))

; Elapsed time: 1351.506931 msecs
; Other ideas:
; - would loop+recur be faster than using for to generate a lazy sequence of pairs?
; - am I missing opportunities to filter numbers out?
(defn- get-sum []
  (let [cost-found? (atom false)
        costs (as-> "input.txt" v
                    (io/resource v)
                    (io/file v)
                    (slurp v)
                    (str/split v #"\n")
                    (map read-string v))
        sums (into #{} (for [a costs
                             b costs
                             c costs
                             :let [sum (when (and (not= a b)
                                                  (not= b c))
                                         (+ a b c))]
                             :when (= 2020 sum)
                             :while (false? @cost-found?)]
                         (do
                           (reset! cost-found? true)
                           (* a b c))))]
    (first sums)))

(defn -main []
  (time (pprint/pprint (get-sum))))

