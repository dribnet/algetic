
(ns algetic.core
  (:require [clojure.string    :refer [join split]]
            [clojure.edn       :as edn]
            [cascalog.api      :refer :all])
  (:import  [com.twitter.algebird HyperLogLogMonoid HLL]))

(def daily-logs
  [["monday" "jake"]
   ["monday" "tom"]
   ["monday" "ted"]
   ["tuesday" "tom"]
   ["tuesday" "ted"]
   ["tuesday" "lothar"]])

(defn hll-create
  [^HyperLogLogMonoid hll ^String s]
  [(if (= s nil) (.zero hll) (.create hll (.getBytes s)))])

(defn hll-plus
  [^HLL x ^HLL y]
  [(.$plus x y)])

(defparallelagg hll-unique
  :init-var #'hll-create
  :combine-var #'hll-plus)

(defn hll-estimate-cardinality
  [^HLL hll]
  (int (.estimatedSize hll)))

(defmain runcount [& ignored]
  (?<- (stdout)
    [?day ?daily-uniques]
    (identity (HyperLogLogMonoid. 12) :> ?hll-monoid)
    (daily-logs ?day !visitor)
    (hll-unique ?hll-monoid !visitor :> ?visitor-hll)
    (hll-estimate-cardinality ?visitor-hll :> ?daily-uniques)))
