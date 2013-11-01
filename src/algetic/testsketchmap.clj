(ns algetic.testsketchmap
  (:require [clojure.string    :refer [join split]]
            [clojure.edn       :as edn]
            [cascalog.api      :refer :all]
            [cascalog.ops      :as ops]
            [clojure.data.codec.base64 :as b64]
            [algetic.core      :refer [daily-logs]]
            [cascalog.more-taps :refer [hfs-delimited]])
  (:import  [scala Tuple2]
            [scala.collection JavaConversions]
            [net.drib.algetic CrossOver]))

(def monoid (CrossOver/MakeSketchMapMonoid 5))

; k/v is string/long
(defn elevate [k v]
  (.create monoid (Tuple2. k v)))

(defn total-value [m]
  (.totalValue m))

(defn frequency [m k]
  (.frequency m k))

(defn heavy-hitters [m]
  (map #(hash-map (._1 %) (._2 %)) 
    (JavaConversions/asJavaIterable (.heavyHitters m))))

(defn plus
  ([m] m)
  ([m1 m2] (.plus monoid m1 m2)))

; (defn to-string [m]
;   (String. (b64/encode (HyperLogLog/toBytes m)) "UTF-8"))

; (defn from-string [s]
;   (HyperLogLog/fromBytes (b64/decode (.getBytes s))))

(defmain run [& ignored]
  (let [data (take 1000 (repeatedly (fn [] [(str "key" (rand-int 10)) 1])))
        elevated (map #(apply elevate %) data)
        aggregate (reduce plus elevated)]
    (println (str "total value: " (total-value aggregate)))
    (doseq [n (range 10)]
      (println (str "frequency " n ":" (frequency aggregate (str "key" n)))))
    (println "top 5 hitters:")
    (println (heavy-hitters aggregate))))

