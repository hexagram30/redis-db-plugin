(ns hxgm30.db.plugin.redis.components.config
  (:require
    [com.stuartsierra.component :as component]
    [hxgm30.graphdb.config :as config]
    [taoensso.timbre :as log]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Utility Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-cfg
  [system]
  (get-in system [:config :data]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Config Component API   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn redis-db-host
  [system]
  (get-in (get-cfg system) [:backend :db :redis :host]))

(defn redis-db-port
  [system]
  (get-in (get-cfg system) [:backend :db :redis :port]))

(defn db-spec
  [system]
  {:host (redis-db-host system)
   :port (redis-db-port system)})

(defn redis-graphdb-host
  [system]
  (get-in (get-cfg system) [:backend :graphdb :redis :host]))

(defn redis-graphdb-port
  [system]
  (get-in (get-cfg system) [:backend :graphdb :redis :port]))

(defn graphdb-spec
  [system]
  {:host (redis-graphdb-host system)
   :port (redis-graphdb-port system)})

(defn log-level
  [system]
  (get-in (get-cfg system) [:logging :level]))

(defn log-nss
  [system]
  (get-in (get-cfg system) [:logging :nss]))

(defn backend-plugin
  [system]
  (get-in (get-cfg system) [:backend :plugin]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord PluginConfig [data])

(defn start
  [this]
  (log/info "Starting plugin config component ...")
    (log/trace "Using plugin configuration:" (:data this))
    (log/debug "Started plugin config component.")
    this)

(defn stop
  [this]
  (log/info "Stopping plugin config component ...")
  (log/debug "Stopped plugin config component.")
  (assoc this :data nil))

(def lifecycle-behaviour
  {:start start
   :stop stop})

(extend PluginConfig
  component/Lifecycle
  lifecycle-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-component
  ""
  [data]
  (map->PluginConfig {:data data}))
