(defrecord RedisLangDB [
  spec
  pool])

(defn -ingest-stats
  ([this language]
    )
  ([this race-name name-type]
    ))

(defn -lang-stats
  [this language]
  )

(defn -name-stats
  [this race-name name-type]
  )

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
