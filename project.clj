(defproject algetic "0.1.0-SNAPSHOT"

  :repositories [["factual" "http://maven.corp.factual.com/nexus/content/groups/public/"]
                 ["releases" "http://maven.corp.factual.com/nexus/content/repositories/releases"]
                 ["snapshots" "http://maven.corp.factual.com/nexus/content/repositories/snapshots"]
                 ["conjars" "http://conjars.org/repo"]
                 ["sonatype" "http://oss.sonatype.org/content/repositories/releases"]
  		           ["twitter-repo" "http://maven.twttr.com"]]
  :plugins [[lein-scalac "0.1.0"]]
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.4"]
                 [cascalog "1.10.2"]
                 [org.scala-lang/scala-library "2.10.3"]
                 [com.twitter/algebird-core_2.10 "0.3.0"]
                 [com.twitter/bijection-core_2.10 "0.5.3"]
                 [com.twitter/chill_2.10 "0.3.4"]
                 [com.twitter/chill-bijection_2.10 "0.3.4"]
                 [org.apache.hadoop/hadoop-core "1.1.2"]]
  :jvm-opts ["-Xmx1g"] 
  :aot [algetic.core]
  :uberjar-name "algetic.jar")

