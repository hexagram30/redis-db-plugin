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

(defn backend-plugin
  [system]
  (get-in (get-cfg system) [:backend :plugin]))

(defn backend-subtype
  [system]
  (get-in (get-cfg system) [:backend :subtype]))

(defn db-host
  [system]
  (get-in (get-cfg system)
          [:backend (backend-subtype system) (backend-plugin system) :host]))

(defn db-port
  [system]
  (get-in (get-cfg system)
          [:backend (backend-subtype system) (backend-plugin system) :port]))

(defn db-spec
  [system]
  {:subtype (backend-subtype system)
   :host (db-host system)
   :port (db-port system)})

(defn log-level
  [system]
  (get-in (get-cfg system) [:logging :level]))

(defn log-nss
  [system]
  (get-in (get-cfg system) [:logging :nss]))

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
