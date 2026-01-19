(ns org.fversnel.freights.executor.atom
  (:require [org.fversnel.freights :as-alias freights]))

(defn construct-fsm
  ([fsm]
   (construct-fsm fsm (fsm)))
  ([fsm current-state]
   (atom
    {::freights/fsm           fsm
     ::freights/current-state current-state})))

(defn send-message [atom-fsm message]
  (swap!
   atom-fsm
   (fn [{::freights/keys [fsm] :as atom-state}]
     (update
      atom-state
      ::freights/current-state
      #(fsm % message)))))

(defn current-state [atom-fsm]
  (::freights/current-state @atom-fsm))