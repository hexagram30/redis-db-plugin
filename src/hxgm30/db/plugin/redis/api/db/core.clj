(defrecord RedisDB [
  spec
  pool])

(defn- -backup
  [this]
  (log/infof "%s ..." (call this :bgrewriteaof))
  :ok)

(defn- -commit
  [this]
  :not-implemented)

(defn- -configuration
  [this]
  :not-implemented)

(defn- -get-index
  ([this data-type]
    (-get-index this data-type (uuid4)))
  ([this data-type id]
    (case data-type
      :edge (schema/edge id)
      :relation (schema/relation id)
      :vertex (schema/vertex id))))

(defn- -disconnect
  [this]
  :not-implemented)

(defn- -dump
  [this]
  (log/infof "%s ..." (call this :bgsave))
  :ok)

(defn- -explain
  [this query-str]
  :not-implemented)

(defn- -flush
  [this]
  :not-implemented)

(defn- -rollback
  [this]
  :not-implemented)

(def db-behaviour
  {:backup -backup
   :commit -commit
   :configuration -configuration
   :disconnect -disconnect
   :dump -dump
   :explain -explain
   :flush -flush
   :rollback -rollback})

(extend RedisDB
        DBAPI
        db-behaviour)
