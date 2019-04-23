(ns org.fversnel.freights.spec
    (:require [clojure.spec.alpha :as s]))
  
  (s/def ::state
    (s/cat :type any? :data any?))
  
  (s/def ::message-handler
    (s/cat
     :message-type any?
     :body any?))
  
  (s/def ::message-handlers
    (s/cat
     :params (and
              ::s/param-list
              #(= (count %) 2))
     :handlers (s/+ ::message-handler)))
  
  (s/def ::dispatch-map
    (s/+
     (s/cat
      :state any?
      :fn ::message-handlers)))