(defrecord RedisSessionDB
  [spec
   pool])

(defn -session-id
  [this user-id]
  (-> this
      (cmd redis/get (schema/user-session user-id))
      :id))

(defn -session-type
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

(defn -session-user-data
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

(defn -get-session
  [this user-id]
  (cmd this
       redis/get
       (schema/user-session user-id)))

(defn -update-session
  [this user-id data]
  (cmd this
       redis/set
       (schema/user-session user-id)
       data))

(def session-behaviour
  {:session-id -session-id
   :session-type -session-type
   :login-attempts -login-attempts
   :authenticated? -authenticated?
   :session-user-data -session-user-data
   :shell-stack -shell-stack
   :current-shell -current-shell
   :get-session -get-session
   :update-session -update-session})

(extend RedisSessionDB
        DBAPI
        db-behaviour)

(extend RedisSessionDB
        SessionDBAPI
        session-behaviour)

(defn create-session-db
  [conn]
  (map->RedisSessionDB conn))
