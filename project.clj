(defproject app "0.1.0-SNAPSHOT"
  :dependencies [
                 [org.clojure/clojure "1.8.0"]
                 [com.cerner/clara-rules "0.15.1"]]
  :main ^:skip-aot app.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
