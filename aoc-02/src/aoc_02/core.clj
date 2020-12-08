(ns aoc-02.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pprint])
  (:gen-class))

(defn- count-letters [string letter]
  (reduce (fn [total checked-letter]
            (if (= letter checked-letter) (inc total) total))
          0
          string))

(defn- to-number [string-or-char]
  (read-string (str string-or-char)))

; Elapsed time: 21.897671 msecs
(defn- get-count []
  (let [passwords (as-> "input.txt" v
                        (io/resource v)
                        (io/file v)
                        (slurp v)
                        (str/split v #"\n")
                        (map (fn [string]
                               (let [parts (str/split string #"\s")
                                     min-max (str/split (first parts) #"-")]
                                 {:min (to-number (first min-max))
                                  :max (to-number (last min-max))
                                  :letter (first (second parts))
                                  :password (last parts)}))
                             v))]
    (reduce (fn [total {:keys [min max letter password] :as opts}]
              (let [count (count-letters password letter)]
                (if (and (<= min count max))
                  (do
                    (pprint/pprint "VALID")
                    (pprint/pprint opts)
                    (inc total))
                  (do
                    (pprint/pprint "INVALID")
                    (pprint/pprint opts)
                    total))))
            0
            passwords)))

(defn -main []
  (time (pprint/pprint (get-count))))

