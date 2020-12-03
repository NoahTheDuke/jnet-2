(ns jnet.actions
  (:require [jnet.player :as player]
            [jnet.pipeline :as pipeline]))

(defn keep-hand
  [game side]
  (update game side player/keep-hand))

(defn mulligan-hand
  [game side]
  (update game side player/mulligan-hand))

(def actions-list
  {"keep" keep-hand
   "mulligan" mulligan-hand})

(defn process-action
  [game side command]
  (let [action (get actions-list command)]
    (if action
      (-> game
          (action side)
          (pipeline/continue))
      (println "something broke" command))))
