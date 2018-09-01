(defproject hexagram30/redis-db-plugin "0.1.0-SNAPSHOT"
  :description "A hexagram30 db plugin for Redis"
  :url "https://github.com/hexagram30/redis-db-plugin"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :dependencies [
    [aysylu/loom "1.0.1"]
    [clojusc/dev-system "0.1.0"]
    [clojusc/trifl "0.2.0"]
    [clojusc/twig "0.3.2"]
    [com.taoensso/carmine "2.18.0"]
    [hexagram30/common "0.1.0-SNAPSHOT"]
    [org.clojure/clojure "1.8.0"]]
  :jvm-opts ["-Ddb.backend=redis"]
  :source-paths ["src"]
  :target-path "../../target/%s/"
  :clean-targets ^:replace []
  :profiles {
    :ubercompile {
      :aot :all}
    :dev {
      :exclusions [
        org.clojure/tools.namespace]
      :dependencies [
        [clojusc/trifl "0.3.0"]
        [org.clojure/tools.namespace "0.2.11"]]
      :plugins [
        [lein-shell "0.5.0"]
        [venantius/ultra "0.5.2"]]
      :source-paths ["dev-resources/src"]
      :repl-options {
        :init-ns hxgm30.db.plugin.redis.repl
        :prompt ~get-prompt
        :init ~(println (get-banner))}}
    :lint {
      :exclusions [
        org.clojure/tools.namespace]
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"]]
      :source-paths ^:replace ["src"]
      :test-paths ^:replace []
      :plugins [
        [jonase/eastwood "0.2.9"]
        [lein-ancient "0.6.15"]
        [lein-bikeshed "0.5.1"]
        [lein-kibit "0.1.6"]
        [venantius/yagni "0.1.4"]]}
    :test {
      :plugins [
        [lein-ltest "0.3.0"]]}
    :server {
      :jvm-opts ["-XX:MaxDirectMemorySize=512g"]
      :main hxgm30.graphdb.server}
    :redis-plugin {
      :jvm-opts ["-Ddb.backend=redis"]
      :source-paths [
        "plugins/redis/src"
        "plugins/redis/dev"]
      :resource-paths ["plugins/redis/resources"]}}
  :aliases {
    ;; Dev Aliases
    "repl" ["do"
      ["clean"]
      ["repl"]]
    "ubercompile" ["do"
      ["clean"]
      ["with-profile" "+ubercompile" "compile"]]
    "check-vers" ["with-profile" "+lint" "ancient" "check" ":all"]
    "check-jars" ["with-profile" "+lint" "do"
      ["deps" ":tree"]
      ["deps" ":plugin-tree"]]
    "check-deps" ["do"
      ["check-jars"]
      ["check-vers"]]
    "kibit" ["with-profile" "+lint" "kibit"]
    "eastwood" ["with-profile" "+lint" "eastwood" "{:namespaces [:source-paths]}"]
    "lint" ["do"
      ["kibit"]
      ;["eastwood"]
      ]
    "ltest" ["with-profile" "+test" "ltest"]
    "ltest-clean" ["do"
      ["clean"]
      ["ltest"]]
    "build" ["do"
      ["clean"]
      ["check-vers"]
      ["lint"]
      ["ltest" ":all"]
      ["uberjar"]
    ;; Plugin
    "start-db" ["shell"
          "docker-compose"
            "-f" "plugins/redis/resources/docker/docker-compose-redis-graph.yml"
            "up"]
    "stop-db" ["shell"
      "docker-compose"
        "-f" "plugins/redis/resources/docker/docker-compose-redis-graph.yml"
        "down"]]})

