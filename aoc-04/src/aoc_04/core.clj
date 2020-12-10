(ns aoc-04.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.pprint :as pprint])
  (:gen-class))

(def required-fields-v1
  ["byr"
   "iyr"
   "eyr"
   "hgt"
   "hcl"
   "ecl"
   "pid"
   ])

(defn valid?-v1 [passport-map]
  (every? #(get passport-map %) required-fields-v1))

; Elapsed time: 12.166113 msecs
(defn- validate-passports-v1 []
  (let [passports (as-> "input.txt" v
                        (io/resource v)
                        (io/file v)
                        (slurp v)
                        (str/split v #"\n\n")
                        (map #(str/split % #"(\n|\s|:)") v)
                        (map #(apply hash-map %) v)
                        (filter valid?-v1 v)
                        (count v))]
    passports))

; Elapsed time: 13.165216 msecs
(defn- validate-passports-v1-transduce []
  (let [passport-strings (as-> "input.txt" v
                               (io/resource v)
                               (io/file v)
                               (slurp v)
                               (str/split v #"\n\n"))]
    (count (into [] (->> passport-strings
                         (map #(str/split % #"(\n|\s|:)"))
                         (map #(apply hash-map %))
                         (filter valid?-v1))))))

(defn- year-between [start end value]
  (and (= 4 (count value))
       (<= start (read-string value) end)))

(def eye-colors ["amb" "blu" "brn" "gry" "grn" "hzl" "oth"])

(def validators
  {"byr" (partial year-between 1920 2002)
   "iyr" (partial year-between 2010 2020)
   "eyr" (partial year-between 2020 2030)
   "hgt" (fn [value]
           (let [amount (read-string (re-find #"^\d+" value))
                 unit (re-find #"[a-z]+$" value)]
             (cond
               (= unit "cm") (<= 150 amount 193)
               (= unit "in") (<= 59 amount 76)
               :else nil)))
   "hcl" (fn [value]
           (re-find #"^#[0-9a-f]{6}$" value))
   "ecl" (fn [value]
           (not (empty? (filter #(= % value) eye-colors))))
   "pid" (fn [value]
           (re-find #"^\d{9}$" value))})

(defn valid? [passport-map]
  (every? (fn [[key f]]
            (let [value (get passport-map key)
                  valid? (and value (f (get passport-map key)))]
              valid?))
          validators))

; Elapsed time: 18.838961 msecs
(defn- validate-passports []
  (let [passports (as-> "input.txt" v
                        (io/resource v)
                        (io/file v)
                        (slurp v)
                        (str/split v #"\n\n")
                        (map #(str/split % #"(\n|\s|:)") v)
                        (map #(apply hash-map %) v)
                        (filter #(<= 7 (count %) 8) v)
                        (filter valid? v)
                        (count v))]
    passports))

(defn -main []
  (time (pprint/pprint (validate-passports))))
