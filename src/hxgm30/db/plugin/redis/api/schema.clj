(ns hxgm30.db.plugin.redis.api.schema
  "Schema setup for graph data.

  Adding a node:
    HMSET node:UUID KEY1 VAL1 [KEY2 VAL2 [...]]

  Adding an edge:
    HMSET edge:UUID KEY1 VAL1 [KEY2 VAL2 [...]]

  Adding a relation:
    RPUSH adjc:UUID [UUID1 [UUID2 [...]]

    ")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Graph DB Schemas   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def edge-tmpl "edge:%s")
(def relation-tmpl "adjc:%s")
(def vertex-tmpl "node:%s")

(def edge #(format edge-tmpl %))
(def relation #(format relation-tmpl %))
(def vertex #(format vertex-tmpl %))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Language DB Schemas   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def lang-stats-tmpl "lang:stats%s%s")
(def name-stats-tmpl "name:stats%s%s%s")

(def lang-stats #(format lang-stats-tmpl %1 %2))
(def name-stats #(format name-stats-tmpl %1 %2 %3))

(defn stats
  [args]
  (cond (= 2 (count args))
        (apply lang-stats args)

        (= 3 (count args))
        (apply name-stats args)))
