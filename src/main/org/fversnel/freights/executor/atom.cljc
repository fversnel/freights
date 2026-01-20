(ns org.fversnel.freights.executor.atom
  (:require [org.fversnel.freights :as freights]))

(defn- internal-send-message! [atom-fsm message]
  (swap!
   atom-fsm
   (fn [{::freights/keys [fsm] :as atom-state}]
     (update
      atom-state
      ::freights/current-state
      #(fsm % message)))))

(defn construct-fsm
  ([fsm]
   (construct-fsm fsm (fsm)))
  ([fsm current-state]
   (let [atom-fsm (atom {::freights/fsm fsm
                         ::freights/current-state current-state})]
     (reify freights/Executor
       (send-message! [_ message]
         (internal-send-message! atom-fsm message))

       (current-state [_]
         (::freights/current-state @atom-fsm))

       (underlying [_] atom-fsm)))))