(ns algetic.testmh
  (:require [clojure.string    :refer [join split]]
            [clojure.edn       :as edn]
            [cascalog.api      :refer :all]
            [cascalog.ops      :as ops]
            [clojure.data.codec.base64 :as b64]
            [algetic.core      :refer [daily-logs]]
            [cascalog.more-taps :refer [hfs-delimited]])
  (:import  [com.twitter.algebird MinHasher16]))

(def mono (MinHasher16. 0.1 128))

(defn elevate [s]
  (.init mono s))

(defn similarity [m1 m2]
  (.similarity mono m1 m2))

(defn plus
  ([m] m)
  ([m1 m2] (.plus mono m1 m2)))

; (defn to-string [m]
;   (String. (b64/encode (HyperLogLog/toBytes m)) "UTF-8"))

; (defn from-string [s]
;   (HyperLogLog/fromBytes (b64/decode (.getBytes s))))

(defmain run [& ignored]
  (let [monday-users (map second (filter #(= (first %) "monday") daily-logs))
        tuesday-users (map second (filter #(= (first %) "tuesday") daily-logs))
        monday-set (reduce plus (map elevate monday-users))
        tuesday-set (reduce plus (map elevate tuesday-users))]
    (println (str "monday/tuesday similarity: " (similarity monday-set tuesday-set)))))
