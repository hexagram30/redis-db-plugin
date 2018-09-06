(ns hxgm30.db.plugin.redis.api.db
  "Items of interest for implementors:

  * https://github.com/aysylu/loom
  * http://www.vldb.org/pvldb/1/1453965.pdf (Hexastore)
  * https://redis.io/topics/indexes"
  (:require
    [clojure.string :as string]
    [hxgm30.common.util :as util]
    [hxgm30.db.plugin.redis.api.queries :as queries]
    [hxgm30.db.plugin.redis.api.schema :as schema]
    [hxgm30.db.plugin.redis.api.util :as plugin-util]
    [taoensso.carmine :as redis]
    [taoensso.timbre :as log]
    [trifl.java :refer [uuid4]])
  (:refer-clojure :exclude [flush]))

(declare get-index)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Support Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load "/hxgm30/db/plugin/redis/api/db/base")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   DB API Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load "/hxgm30/db/plugin/protocols/db")
(load "/hxgm30/db/plugin/redis/api/db/core")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Redis-specific Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load "/hxgm30/db/plugin/redis/api/db/admin")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   GraphDB API Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load "/hxgm30/db/plugin/protocols/graphdb")
(load "/hxgm30/db/plugin/redis/api/db/graph")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;  LangDB API Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(load "/hxgm30/db/plugin/protocols/langdb")
(load "/hxgm30/db/plugin/redis/api/db/lang")
