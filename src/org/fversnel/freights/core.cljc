(ns org.fversnel.freights.core)

(def pairs (partial partition 2))

(defn message-handler->fn
  [state-id param-list [message-type & forms]]
  [[state-id message-type]
   (cons 'fn (cons param-list forms))])

(defmacro compile-dispatch-map
  [& forms]
  {:pre [(even? (count forms))]}
  (let [compile-state-fn
        (fn [[state-id [param-list & message-handler-forms]]]
          (eduction
           (map (partial message-handler->fn state-id param-list))
           (pairs message-handler-forms)))]
    (into
     {}
     (mapcat compile-state-fn)
     (pairs forms))))

(defn fsm->fn
  [{:keys [dispatch-fn dispatch-map initial-state] :as opts}]
  (fn dispatch-message
    ([] initial-state)
    ([message]
     (dispatch-message initial-state message))
    ([data message]
     (let [dispatch-key (dispatch-fn data message)
           message-handler (dispatch-map dispatch-key)]
       (if message-handler
         (message-handler data message)
         data)))))

(defmacro fsm
  [{:keys [dispatch-fn initial-state] :as opts} & forms]
  `(fsm->fn
    {:dispatch-fn ~dispatch-fn
     :dispatch-map (compile-dispatch-map ~@forms)
     :initial-state ~initial-state}))
