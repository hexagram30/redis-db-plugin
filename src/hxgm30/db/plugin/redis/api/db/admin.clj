;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;   Redis Admin Functions   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn drop-data
  [this]
  (call this :flushdb))

(defn latency-setup
  ([this]
    (latency-setup this 100))
  ([this milliseconds]
    (call this :config :set "latency-monitor-threshold" milliseconds)))

(defn latency-latest
  [this]
  (call this :latency :latest))

(defn latency-doctor
  [this]
  (print (call this :latency :doctor))
  :ok)

(defn slowlog
  ([this]
    (call this :slowlog :get))
  ([this count]
    (call this :slowlog :get count)))

(defn info
  [this]
  (call this :info))
