(ns hxgm30.db.plugin.redis.components.core
  (:require
    [com.stuartsierra.component :as component]
    [hxgm30.db.plugin.redis.api.db :as db]
    [hxgm30.db.plugin.redis.api.factory :as factory]
    [hxgm30.db.plugin.redis.components.config :as config]
    [taoensso.timbre :as log])
  (:import
    (clojure.lang Symbol)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Constants   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def component-deps [:config :logging])
(def db-ns 'hxgm30.db.plugin.redis.api.db)
(def factory-ns 'hxgm30.db.plugin.redis.api.factory)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Redis Component API   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-conn
  [system]
  (get-in system [:backend :conn]))

(defn get-factory
  [system]
  (get-in system [:backend :factory]))

(defn db-call
  ([system ^Symbol func]
    (db-call system func []))
  ([system ^Symbol func args]
    (log/trace "Component preparing to call into plugin db API ...")
    (log/tracef "Got system, func, args: %s, %s, %s" system func args)
    (let [resolved-func (ns-resolve db-ns func)]
      (log/trace "Got resolved function:" resolved-func)
      (when (nil? resolved-func)
        (log/error (str "Couldn't find function in given namespace; "
                        "has it been added to the protocol/behaviour?")))
      (apply
        resolved-func
        (concat [(get-conn system)] args)))))

(defn db*
  [system ^Symbol func & args]
  (db-call system func args))

(defn factory-call
  ([system ^Symbol func]
    (factory-call system func []))
  ([system ^Symbol func args]
    (log/trace "Component preparing to call into plugin factory API ...")
    (apply
      (ns-resolve factory-ns func)
      (concat [(get-factory system)] args))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Lifecycle Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord Redis [conn])

(defn start
  [this]
  (log/info "Starting Redis component ...")
  (let [f (factory/create (get-spec this))
        conn (factory/connect f)]
    (log/debug "Started Redis component.")
    (assoc this :conn conn)))

(defn stop
  [this]
  (log/info "Stopping Redis component ...")
  (log/debug "Stopped Redis component.")
  (assoc this :conn nil))

(def lifecycle-behaviour
  {:start start
   :stop stop})

(extend Redis
  component/Lifecycle
  lifecycle-behaviour)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Component Constructor   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn create-component
  ""
  []
  (map->Redis {}))
