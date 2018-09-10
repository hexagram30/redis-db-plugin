(defrecord RedisLangDB [
  spec
  pool])

(defn -ingest-stats
  ([this data]
    (pipeline this
              (mapv (fn [[args stats]] [:set (schema/stats args) stats]) data)))
  ([this language gen-type data]
    (cmd this
         redis/set
         (schema/lang-stats language gen-type)
         data))
  ([this race-name name-type gen-type data]
    (cmd this
         redis/set
         (schema/name-stats race-name name-type gen-type)
         data)))

(defn -lang-stats
  [this language gen-type]
  (cmd this redis/get (schema/lang-stats language gen-type)))

(defn -name-stats
  [this race-name name-type gen-type]
  (cmd this redis/get (schema/name-stats race-name name-type gen-type)))

(defn -get-stats
  ([this language gen-type]
    (-lang-stats this this language gen-type))
  ([this race-name name-type gen-type]
    (-name-stats this this race-name name-type gen-type)))

(def lang-behaviour
  {:ingest-stats -ingest-stats
   :lang-stats -lang-stats
   :name-stats -name-stats
   :get-stats -get-stats})

(extend RedisLangDB
        DBAPI
        db-behaviour)

(extend RedisLangDB
        LangDBAPI
        lang-behaviour)

(defn create-lang-db
  [conn]
  (map->RedisLangDB conn))
