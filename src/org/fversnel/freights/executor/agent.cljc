(ns org.fversnel.freights.executor.agent)

(defn fsm [fsm]
  (agent
   {:fsm fsm
    :current-state (fsm)}))

(defn send-message [agent-fsm message]
  (send
   agent-fsm
   (fn [{:keys [fsm] :as agent-state}]
     (update
      agent-state
      :current-state
      #(fsm % message)))))

(defn current-state [agent-fsm]
  (:current-state @agent-fsm))