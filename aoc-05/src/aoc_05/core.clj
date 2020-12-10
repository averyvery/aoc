(ns aoc-05.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pprint])
  (:gen-class))

(defn- row [pos range]
  (if (empty? pos)
    (first range)
    (let [instruction (first pos)
          split-range (split-at (/ (count range) 2) range)]
      (recur
        (rest pos)
        (if (= instruction \F)
          (first split-range)
          (last split-range))))))

(defn- column [pos range]
  (if (empty? pos)
    (first range)
    (let [instruction (first pos)
          split-range (split-at (/ (count range) 2) range)]
      (recur
        (rest pos)
        (if (= instruction \L)
          (first split-range)
          (last split-range))))))

(defn- row-and-column [[row-pos col-pos]]
  [(row row-pos (range 128)) (column col-pos (range 8))])

; Elapsed time: 33.909238 msecs
(defn- highest-id []
  (let [passes (as-> "input.txt" v
                        (io/resource v)
                        (io/file v)
                        (slurp v)
                        (str/split v #"\n")
                        (map #(split-at 7 %) v)
                        (map row-and-column v)
                        (map #(+ (* (first %) 8) (last %)) v)
                        (sort v)
                        (last v))]
    passes))

(defn -main-v1 []
  (time (pprint/pprint (highest-id))))


(defn- find-missing-id [ids]
  (loop [ids ids
         last-id nil]
    (let [current-id (first ids)]
      (if (and (not (nil? last-id))
               (not= (- current-id 1) last-id))
        (- current-id 1)
        (recur (rest ids) current-id)))))

; Elapsed time: 36.119572 msecs
(defn- my-seat []
  (let [passes (as-> "input.txt" v
                     (io/resource v)
                     (io/file v)
                     (slurp v)
                     (str/split v #"\n")
                     (map #(split-at 7 %) v)
                     (map row-and-column v)
                     (map #(+ (* (first %) 8) (last %)) v)
                     (sort v)
                     (find-missing-id v)
                     #_(last v))]
    passes))

(defn -main []
  (time (pprint/pprint (my-seat))))
