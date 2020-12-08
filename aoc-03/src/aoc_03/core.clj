(ns aoc-03.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pprint])
  (:gen-class))

; Elapsed time: 8.825214 msecs
(defn- count-trees-v1 []
  (let [lines (as-> "input.txt" v
                    (io/resource v)
                    (io/file v)
                    (slurp v)
                    (str/split v #"\n"))
        line-length (count (first lines))]
    (loop [position 3
           lines (rest lines)
           trees 0]
      (if (empty? lines)
        trees
        (recur (+ position 3)
               (rest lines)
               (do
                 (if (= (nth (first lines) (mod position line-length)) \#)
                   (inc trees)
                   trees)))))))

(defn- count-trees [right down]
  (let [lines (as-> "input.txt" v
                    (io/resource v)
                    (io/file v)
                    (slurp v)
                    (str/split v #"\n"))
        line-length (count (first lines))]
    (loop [position right
           lines (drop down lines)
           trees 0]
      (if (empty? lines)
        trees
        (recur (+ position right)
               (drop down lines)
               (if (= (nth (first lines) (mod position line-length)) \#)
                 (inc trees)
                 trees))))))

(defn -main []
  (time (pprint/pprint (* (count-trees 1 1)
                          (count-trees 3 1)
                          (count-trees 5 1)
                          (count-trees 7 1)
                          (count-trees 1 2)))))
