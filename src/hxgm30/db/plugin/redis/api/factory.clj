(ns hxgm30.db.plugin.redis.api.factory
  (:require
    [hxgm30.db.plugin.redis.api.db :as redis])
  (:import
    (clojure.lang Keyword))
  (:refer-clojure :exclude [drop]))

(load "/hxgm30/db/plugin/protocols/factory")

(defrecord RedisFactory [
  spec
  pool])

(defn- -connect
  [this]
  (redis/map->RedisGraph this))

(defn- -destroy
  [this]
  ;; No-op
  )

(def behaviour
  {:connect -connect
   :destroy -destroy})

(extend RedisFactory
        DBFactoryAPI
        behaviour)

(defn create
  ([spec]
    (create spec {}))
  ([spec pool]
    (map->RedisFactory
      {:spec spec
       :pool pool})))
