(ns hxgm30.db.plugin.redis.api.schema)

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

(defn lang-stats
  [language generator-type]
  (format lang-stats-tmpl language generator-type))

(defn name-stats
  [race-name name-type generator-type]
  (format name-stats-tmpl race-name name-type generator-type))

(defn stats
  [args]
  (cond (= 2 (count args))
        (apply lang-stats args)

        (= 3 (count args))
        (apply name-stats args)))
