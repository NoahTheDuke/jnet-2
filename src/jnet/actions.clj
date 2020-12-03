(ns jnet.actions
  (:require [jnet.player :refer [init-hand set-prompt]]))

(defn keep-hand
  [player]
  (-> player
      (assoc :ready-to-start true)
      (set-prompt {:menu-title "Waiting for opponent to keep or mulligan"})))

(defn mulligan-hand
  [player]
  (-> player
      (assoc :ready-to-start true
             :mulligan true)
      (init-hand)
      (set-prompt {:menu-title "Waiting for opponent to keep or mulligan"})))

(def actions-list
  {"keep" keep-hand
   "mulligan" mulligan-hand})

(defn process-action
  [game side command]
  (let [action (get actions-list command)]
    (if action
      (update game side action)
      (println "something broke" command))
    ))
