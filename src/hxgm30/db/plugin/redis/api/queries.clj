(ns hxgm30.db.plugin.redis.api.queries
  (:require
    [loom.alg :as alg]
    [loom.alg-generic :as alg-generic]
    [loom.attr :as attr]
    [loom.flow :as flow]
    [loom.graph :as graph]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Graph DB Queries   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn adjacency-map
  [vertex-ids relations]
  (zipmap vertex-ids relations))

(defn graph
  ([vertex-ids relations]
    (graph/graph (adjacency-map vertex-ids relations)))
  ([vertex-ids relations attrs]
    (let [g (graph vertex-ids relations)]
      (assoc g :attrs attrs))))
