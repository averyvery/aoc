(ns aoc-07.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.set :as c.set]
            [clojure.pprint :as pprint]
            [clojure.pprint :as pp])
  (:gen-class))

(defn- structure-rule [line]
  (let [[type contents] (str/split line #" bags contain ")
        contents (as-> contents contents
                       (str/replace contents #" bags?" "")
                       (str/split contents #", ")
                       (map #(str/split % #" " 2) contents)
                       (keep #(if (= "no" (first %))
                                nil
                                [(read-string (first %))
                                 (last %)])
                             contents))]
    {:type type
     :contents (if (empty? contents) nil contents)}))

(defn- find-parents [type rules]
  (->> rules
       (filter (fn [{:keys [_ contents]}]
                 (= 1 (count (filter #(= type (last %)) contents)))))
       (map :type)))

(defn- create-parents [rules]
  (loop [processing-rules rules
         new-rules {}]
    (if (empty? processing-rules)
      new-rules
      (let [{:keys [type]} (first processing-rules)]
        (recur (rest processing-rules)
               (assoc new-rules type (find-parents type rules)))))))

(defn- ancestors
  ([parent-map type] (ancestors parent-map type #{}))
  ([parent-map type current-parents]
   (let [parent-types (get parent-map type)]
     (if (zero? (count parent-types))
       current-parents
       (c.set/union current-parents
                    parent-types
                    (apply c.set/union
                           (set (map #(ancestors parent-map % current-parents)
                                     parent-types))))))))

; Elapsed time: 215.31135 msecs
(defn- count-bags-v1 []
  (let [parent-map (as-> "input.txt" v
                         (io/resource v)
                         (io/file v)
                         (slurp v)
                         (str/split v #"\n")
                         (map #(str/replace % #"\." "") v)
                         (map structure-rule v)
                         (create-parents v)
                         (apply concat v)
                         (apply hash-map v)
                         (ancestors v "shiny gold")
                         (set v)
                         (count v))]
    parent-map))

; Elapsed time: 26.768928 msecs
(defn- count-descendants [rules type]
  (let [contents (:contents (first (filter #(= (:type %) type) rules)))]
    (if (nil? contents)
      0
      (apply + (map (fn [[number content-type]]
                      (+ number (* number
                                   (count-descendants rules content-type))))
                    contents)))))

(defn- count-bags []
  (let [output (as-> "input.txt" v
                     (io/resource v)
                     (io/file v)
                     (slurp v)
                     (str/split v #"\n")
                     (map #(str/replace % #"\." "") v)
                     (map structure-rule v)
                     (count-descendants v "shiny gold"))]
    output))

(defn -main []
  (time (pprint/pprint (count-bags))))
