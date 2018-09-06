(defn- parse-results
  [results]
  (log/trace "Got results:" results)
  (log/trace "Results type:" (type results))
  (condp = results
    "OK" :ok
    results))

(defn- prepare
  [args]
  (log/trace "Got args:" args)
  (apply redis/redis-call args))

(defn cmd
  [this lib-cmd & args]
  (log/debug "Making carmine call to Redis:" lib-cmd)
  (-> this
      (select-keys [:spec :pool])
      (redis/wcar (apply lib-cmd args))
      (parse-results)))

(defn- pipeline
  [this & cmds]
  (log/debug "Making call(s) to Redis:" cmds)
  (-> this
      (select-keys [:spec :pool])
      (redis/wcar (doall (mapcat prepare cmds)))
      (parse-results)))

(defn call
  [this & args]
  (log/trace "Using 'call' with args:" args)
  (log/tracef "Wrapping args: [%s] ..." args)
  (pipeline this [args]))

(defn- call-with-cursor
  [this cursor-func cursor]
  (loop [[next-cursor results] (cursor-func cursor)
         acc []]
    (if (= "0" next-cursor)
      (concat acc results)
      (recur (cursor-func next-cursor) results))))

(defn- get-attrs
  ([this id]
    (get-attrs this id 0))
  ([this id cursor]
    (let [results (call-with-cursor
                   this
                   (fn [cursor] (call this :hscan id cursor))
                   cursor)]
      (if (util/tuple? results)
        (util/tuple->map results)
        (util/tuples->map results)))))

(defn find-keys
  [this pattern]
  (if (= "*" pattern)
    (let [msg "Provided pattern would result in expensive query."]
      (log/error msg)
      {:error {:type :bad-query
               :msg msg}})
    (call this :keys pattern)))

(defn- -delete-all
  [this get-all-fn]
  (->> this
       get-all-fn
       (map :del)
       (pipeline this)
       vec))
