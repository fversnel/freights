(ns org.fversnel.freights.executor.agent
  (:require [org.fversnel.freights :as freights]))

(defn- internal-send-message! [agent-fsm message]
  (send
   agent-fsm
   (fn [{::freights/keys [fsm] :as agent-state}]
     (update
      agent-state
      ::freights/current-state
      #(fsm % message)))))

(defn construct-fsm
  ([fsm]
   (construct-fsm fsm (fsm)))
  ([fsm current-state]
   (let [agent-fsm (agent {::freights/fsm fsm
                           ::freights/current-state current-state})]
     (reify freights/Executor
       (send-message! [_ message]
         (internal-send-message! agent-fsm message))

       (current-state [_]
         (::freights/current-state @agent-fsm))

       (underlying [_] agent-fsm)))))