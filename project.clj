(defproject net.drib/algetic "0.1.0-SNAPSHOT"

  :repositories [["conjars" "http://conjars.org/repo"]
                 ["sonatype" "http://oss.sonatype.org/content/repositories/releases"]
  		           ["twitter-repo" "http://maven.twttr.com"]]
  :plugins [[lein-scalac "0.1.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]
                 [cascalog/cascalog-core "2.0.0"]
                 [cascalog/cascalog-more-taps "2.0.0"]
                 [org.clojure/data.codec "0.1.0"]
                 [org.scala-lang/scala-library "2.9.3"]
                 [com.twitter/algebird-core_2.9.3 "0.3.0"]
                 [com.twitter/bijection-core_2.9.3 "0.5.3"]
                 [com.twitter/chill_2.9.3 "0.3.4"]
                 [com.twitter/chill-bijection_2.9.3 "0.3.4"]
                 [org.apache.hadoop/hadoop-core "1.1.2"]]
  :scala-source-path "src/scala"
  :prep-tasks ["scalac"]
  :jvm-opts ["-Xmx1g"] 
  :aot [algetic.core algetic.testhll algetic.testmh algetic.testsketchmap]
  :uberjar-name "algetic.jar")

