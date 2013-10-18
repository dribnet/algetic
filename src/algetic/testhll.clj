
(ns algetic.testhll
  (:require [clojure.string    :refer [join split]]
            [clojure.edn       :as edn]
            [cascalog.api      :refer :all]
            [cascalog.ops      :as ops]
            [clojure.data.codec.base64 :as b64]
            [algetic.core      :refer [daily-logs]]
            [cascalog.more-taps :refer [hfs-delimited]])
  (:import  [com.twitter.algebird HyperLogLog HyperLogLogMonoid HLL]))

(def mono (HyperLogLogMonoid. 12))

(defn elevate [s]
  (.create mono (.getBytes s)))

(defn size [m]
  (.estimatedSize m))

(defn plus
  ([m] m)
  ([m1 m2] (.plus mono m1 m2)))

(defn to-string [m]
  (String. (b64/encode (HyperLogLog/toBytes m)) "UTF-8"))

(defn from-string [s]
  (HyperLogLog/fromBytes (b64/decode (.getBytes s))))

(defmain run [& ignored]
  (let [monday-users (map second (filter #(= (first %) "monday") daily-logs))
        tuesday-users (map second (filter #(= (first %) "tuesday") daily-logs))
        monday-set (reduce plus (map elevate monday-users))
        tuesday-set (reduce plus (map elevate tuesday-users))
        full-set (plus monday-set tuesday-set)] ; <- note we are adding partial results here
    (println (str "# monday users: " (size monday-set)))
    (println (str "# tuesday users: " (size tuesday-set)))
    (println (str "# all users: " (size full-set)))))

