(ns org.fversnel.freights.executor.atom)

(defn fsm [fsm]
  (atom
   {:fsm fsm
    :current-state (fsm)}))

(defn send-message [atom-fsm message]
  (swap!
   atom-fsm
   (fn [{:keys [fsm] :as atom-state}]
     (update
      atom-state
      :current-state
      #(fsm % message)))))

(defn current-state [atom-fsm]
  (:current-state @atom-fsm))