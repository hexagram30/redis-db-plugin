(defn get-banner
  []
  (try
    (str
      (slurp "resources/text/banner.txt")
      (slurp "resources/text/loading.txt"))
    ;; If another project can't find the banner, just skip it;
    ;; this function is really only meant to be used by Dragon itself.
    (catch Exception _ "")))

(defn get-prompt
  [ns]
  (str "\u001B[35m[\u001B[34m"
       ns
       "\u001B[35m]\u001B[33m λ\u001B[m=> "))

(defproject hexagram30/redis-db-plugin "0.1.0-SNAPSHOT"
  :description "A hexagram30 db plugin for Redis"
  :url "https://github.com/hexagram30/redis-db-plugin"
  :license {
    :name "Apache License, Version 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"}
  :exclusions [;; for jar-file-collisions
    [com.taoensso/encore]
    [io.aviso/pretty]]
  :dependencies [
    [aysylu/loom "1.0.2"]
    [clojusc/system-manager "0.3.0"]
    [clojusc/trifl "0.4.2"]
    [clojusc/twig "0.4.0"]
    [com.taoensso/carmine "2.19.1"]
    [hexagram30/common "0.1.0-SNAPSHOT"]
    [hexagram30/db-plugin "0.1.0-SNAPSHOT"]
    [org.clojure/clojure "1.10.0"]
    ;; The following address jar-file-collisions
    [com.taoensso/encore "2.105.0"]
    [io.aviso/pretty "0.1.36"]]
  :jvm-opts ["-Dgraphdb.backend=redis"
             "-Ddb.backend=redis"]
  :source-paths ["src" "dev"]
  :profiles {
    :ubercompile {
      :aot :all}
    :dev {
      :exclusions [
        org.clojure/tools.namespace]
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"]]
      :plugins [
        [lein-shell "0.5.0"]
        [venantius/ultra "0.5.2"]]
      :repl-options {
        ;:init-ns hxgm30.db.plugin.redis.repl
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
        [jonase/eastwood "0.3.4"]
        [lein-ancient "0.6.15"]
        [lein-bikeshed "0.5.1"]
        [lein-kibit "0.1.6"]
        [venantius/yagni "0.1.7"]]}
    :test {
      :plugins [
        [lein-ltest "0.3.0"]]}}
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
      ["ubercompile"]
      ["lint"]
      ["ltest" ":all"]
      ["uberjar"]]
    ;; Plugin
    "start-graph-db" ["shell"
      "docker-compose"
        "-f" "resources/docker/compose-redis-graphdb.yml"
        "up"]
    "stop-graph-db" ["shell"
      "docker-compose"
        "-f" "resources/docker/compose-redis-graphdb.yml"
        "down"]
    "start-db" ["shell"
      "docker-compose"
        "-f" "resources/docker/compose-redis-db.yml"
        "up"]
    "stop-db" ["shell"
      "docker-compose"
        "-f" "resources/docker/compose-redis-db.yml"
        "down"]})

