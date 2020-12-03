(ns jnet.game
  (:require [jnet.pipeline :refer [make-pipeline]]
            [jnet.player :refer [get-player-state]]))

(defn new-game
  [{:keys [name game-id corp runner]}]
  {:active-player :runner
   :corp corp
   :game-id game-id
   :log []
   :gp (make-pipeline)
   :name name
   :runner runner
   :turn 0})

(defn get-state
  [game]
  {:name (:name game)
   :corp (get-player-state (:corp game))
   :runner (get-player-state (:runner game))})
