(defrecord RedisLangDB [
  spec
  pool])

(defn -ingest-stats
  ([this language data]
    (cmd :set (schema/lang-stats language) data))
  ([this race-name name-type data]
    (cmd :get (schema/name-stats race-name name-type) data)))

(defn -lang-stats
  [this language]
  (cmd :get (schema/lang-stats language)))

(defn -name-stats
  [this race-name name-type]
  (cmd :get (schema/name-stats race-name name-type)))

(def lang-behaviour
  {:ingest-stats -ingest-stats
   :lang-stats -lang-stats
   :name-stats -name-stats})

(extend RedisLangDB
        DBAPI
        db-behaviour)

(extend RedisLangDB
        LangDBAPI
        lang-behaviour)

(defn create-lang-db
  [conn]
  (map->RedisLangDB conn))
