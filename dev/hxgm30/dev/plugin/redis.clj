;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Supplemental, Backend-specific API   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(log/info "Loading Redis-specific dev namespace ...")

(defn-db find-keys pattern)
(defn-db info)
(defn-db latency-doctor)
(defn-db latency-latest)

(defn latency-setup
  ([]
    (backend/db-call (system) 'latency-setup))
  ([milliseconds]
    (backend/db-call (system) 'latency-setup milliseconds)))

(defn slowlog
  ([]
    (backend/db-call (system) 'slowlog))
  ([count]
    (backend/db-call (system) 'slowlog count)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Third-party Tools   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(require
  '[loom.alg :as alg]
  '[loom.alg-generic :as alg-generic]
  '[loom.attr :as attr]
  '[loom.flow :as flow]
  '[loom.graph :as graph]
  '[loom.io :as loom-io])

(require
  '[hxgm30.db.plugin.redis.api.queries :as queries])

(log/debug "Loaded Redis-specific dev namespace.")
