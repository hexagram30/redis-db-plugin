(defrecord RedisSessionDB
  [spec
   pool])

(defn -id
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :id))

(defn -type
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :type))

(defn -login-attempts
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :login-attempts))

(defn -authenticated?
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :authenticated?))

(defn -user-data
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :user-data))

(defn -shell-stack
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :shell-stack))

(defn -current-shell
  [this user-id]
  (-> this
      -shell-stack
      first))

(defn -update
  [this user-id data]
  (cmd this
       redis/set
       (schema/user-session user-id)
       data))

(def session-behaviour
  {:id -id
   :type -type
   :login-attempts -login-attempts
   :authenticated? -authenticated?
   :user-data -user-data
   :shell-stack -shell-stack
   :current-shell -current-shell
   :update -update})

(extend RedisSessionDB
        DBAPI
        db-behaviour)

(extend RedisSessionDB
        SessionDBAPI
        session-behaviour)

(defn create-session-db
  [conn]
  (map->RedisSessionDB conn))
