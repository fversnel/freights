(ns org.fversnel.freights.executor.agent
  (:require [org.fversnel.freights :as-alias freights]))

(defn construct-fsm
  ([fsm]
   (construct-fsm fsm (fsm)))
  ([fsm current-state]
   (agent
    {::freights/fsm fsm
     ::freights/current-state current-state})))

(defn send-message [agent-fsm message]
  (send
   agent-fsm
   (fn [{::freights/keys [fsm] :as agent-state}]
     (update
      agent-state
      ::freights/current-state
      #(fsm % message)))))

(defn current-state [agent-fsm]
  (::freights/current-state @agent-fsm))