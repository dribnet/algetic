(ns algetic.testsketchmap
  (:require [clojure.string    :refer [join split]]
            [clojure.edn       :as edn]
            [cascalog.api      :refer :all]
            [clojure.data.codec.base64 :as b64]
            [algetic.core      :refer [daily-logs]])
  (:import  [scala Tuple2]
            [scala.collection JavaConversions]
            [net.drib.algetic CrossOver]))

; (def monoid (CrossOver/MakeSketchMapMonoid 5))

(defn get-monoid [topcount]
  (CrossOver/MakeSketchMapMonoid topcount))

; k/v is string/long
(defn elevate-with [monoid k v]
  (.create monoid (Tuple2. k v)))

(defn total-value [m]
  (.totalValue m))

(defn frequency [m k]
  (.frequency m k))

(defn heavy-hitters [m]
  (vec (map #(vector (._1 %) (._2 %)) 
    (JavaConversions/asJavaIterable (.heavyHitters m)))))

(defn plus-with
  ([monoid m] m)
  ([monoid m1 m2] (.plus monoid m1 m2)))

; (defn to-string [m]
;   (String. (b64/encode (HyperLogLog/toBytes m)) "UTF-8"))

; (defn from-string [s]
;   (HyperLogLog/fromBytes (b64/decode (.getBytes s))))

(def monoid100 (get-monoid 100))

(defn sm-create
  [k v]
  [(elevate-with monoid100 k v)])

(defn sm-plus
  [x y]
  [(plus-with monoid100 x y)])

(defparallelagg sketchmap-agg
  :init-var #'sm-create
  :combine-var #'sm-plus)

(def count-logs
  [["monday" 10]
   ["tuesday" 20]
   ["wednesday" 30]
   ["monday" 20]
   ["tuesday" 30]
   ["wednesday" 40]])

(defn tops [sketchmap]
  [(heavy-hitters sketchmap)])

(defmain runcount [& ignored]
  (?<- (stdout)
    [?total ?heavy-hitters]
    (count-logs ?day ?visitors)
    (sketchmap-agg ?day ?visitors :> ?logs-sketch-map)
    (total-value ?logs-sketch-map :> ?total)
    (tops ?logs-sketch-map :> ?heavy-hitters)))

(defmain run [& ignored]
  (let [data (take 1000 (repeatedly (fn [] [(str "key" (rand-int 10)) 1])))
        monoid (get-monoid 10)
        elevate (partial elevate-with monoid)
        plus (partial plus-with monoid)
        elevated (map #(apply elevate %) data)
        aggregate (reduce plus elevated)]
    (println (str "total value: " (total-value aggregate)))
    (doseq [n (range 5)]
      (println (str "frequency " n ":" (frequency aggregate (str "key" n)))))
    (println "top 8 hitters:")
    (println (heavy-hitters aggregate))))

