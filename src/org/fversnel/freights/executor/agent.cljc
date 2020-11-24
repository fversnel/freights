(ns org.fversnel.freights.executor.agent)

(defn agent-fsm [fsm]
  (agent
   {:fsm fsm
    :current-state (fsm)}))

(defn send-message [fsm-agent message]
  (send
   fsm-agent
   (fn [{:keys [fsm] :as agent-state}]
     (update
      agent-state
      :current-state
      #(fsm % message)))))