;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Support Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- create-edge-cmd
  ([id]
    (create-edge-cmd id {nil nil}))
  ([id attrs]
    (create-edge-cmd id nil attrs))
  ([id label attrs]
    (let [normed-attrs (merge attrs (when label {:label label}))
          flat-attrs (mapcat vec normed-attrs)]
      (concat [:hmset id] flat-attrs))))

(defn- create-relation-cmd
  [this src-id dst-id edge-id]
  (let [id (get-index this :relation src-id)]
    [:rpush id dst-id]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Core Implementation   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord RedisGraphDB
  [spec
   pool])

(defn- -add-edge
  ([this src-id dst-id]
    (-add-edge this src-id dst-id {nil nil}))
  ([this src-id dst-id attrs]
    (-add-edge this src-id dst-id nil attrs))
  ([this src-id dst-id label attrs]
    (let [edge-id (get-index this :edge)
          result (pipeline
                   this
                   [[:multi]
                    (create-edge-cmd edge-id label attrs)
                    (create-relation-cmd this src-id dst-id edge-id)
                    [:exec]])]
      {:id edge-id
       :result result})))

(defn- -add-vertex
  ([this]
    (-add-vertex this {nil nil}))
  ([this attrs]
    (-add-vertex this nil attrs))
  ([this label attrs]
    (let [id (get-index this :vertex)
          normed-attrs (merge attrs (when label {:label label}))
          flat-attrs (mapcat vec normed-attrs)
          result (apply call (concat [this :hmset id] flat-attrs))]
      {:id id
       :result result})))

(defn- -add-vertices
  [this props]
  :forth-coming)

(defn- -get-edge
  ([this id]
    (-get-edge this id 0))
  ([this id cursor]
    {id (util/list->map (call this :hgetall id))}))

(defn- -get-edges
  [this]
  (find-keys this (get-index this :edge "*")))

(defn- -get-relations
  [this]
  (find-keys this (get-index this :relation "*")))

(defn- -get-vertex
  ([this id]
    (-get-vertex this id 0))
  ([this id cursor]
    {id (util/list->map (call this :hgetall id))}))

(defn- -get-vertex-relations
  [this id]
  (call this :lrange (get-index this :relation id) 0 -1))

(defn- -get-vertices
  [this]
  (find-keys this (get-index this :vertex "*")))

(defn- -get-vertices-relations
  [this ids]
  (->> ids
       (map (fn [x] [:lrange (get-index this :relation x) 0 -1]))
       (pipeline this)
       vec))

(defn- -remove-edge
  [this id]
  (call this :del id))

(defn- -remove-edges
  [this]
  (-delete-all this -get-edges))

(defn- -remove-relation
  [this relation-id vertex-id]
  (vec (call this :lrem relation-id 0 vertex-id)))

(defn- -remove-relations
  ([this]
    (-delete-all this -get-relations))
  ([this vertex-id]
    (let [relation-id (get-index this :relation vertex-id)]
      (->> vertex-id
           (-get-vertex-relations this)
           (map (partial remove-relation this relation-id))
           vec))))

(defn- -remove-vertex
  [this id]
  (call this :del id))

(defn- -remove-vertices
  [this]
  (-delete-all this -get-vertices))

(defn- -show-features
  [this]
  :not-implemented)

(defn- -edges
  ([this]
    (-edges this (get-edges this)))
  ([this ids]
    (->> ids
         (map (partial get-edge this))
         (reduce merge))))

(defn- -relations
  ([this]
    (-relations this (get-edges this)))
  ([this ids]
    (->> ids
         (map (partial get-edge this))
         vec)))

(defn- -vertices
  ([this]
    (-vertices this (get-vertices this)))
  ([this ids]
    (->> ids
         (map (partial get-vertex this))
         (reduce merge))))

(def graphdb-behaviour
  {:add-edge -add-edge
   :add-vertex -add-vertex
   :edges -edges
   :get-edge -get-edge
   :get-edges -get-edges
   :get-index -get-index
   :get-relations -get-relations
   :get-vertex -get-vertex
   :get-vertex-relations -get-vertex-relations
   :get-vertices -get-vertices
   :get-vertices-relations -get-vertices-relations
   :relations -relations
   :remove-edge -remove-edge
   :remove-edges -remove-edges
   :remove-relation -remove-relation
   :remove-relations -remove-relations
   :remove-vertex -remove-vertex
   :remove-vertices -remove-vertices
   :show-features -show-features
   :vertices -vertices})

(extend RedisGraphDB
        DBAPI
        db-behaviour)

(extend RedisGraphDB
        GraphDBAPI
        graphdb-behaviour)

(defn- create-relation-cmd
  [this src-id dst-id edge-id]
  (let [id (get-index this :relation src-id)]
    [:rpush id dst-id]))
