(ns jnet.player)

(defn new-corp
  [user c-identity deck]
  {:agenda-points 0
   :agenda-points-req 7
   :bad-publicity {:base 0
                   :additional 0}
   :clicks 0
   :clicks-per-turn 3
   :credits 5
   :deck deck
   :discard []
   :hand []
   :hand-size {:base 5
               :total 5}
   :identity c-identity
   :play-area []
   :ready-to-start false
   :rfg []
   :scored []
   :set-aside []
   :user user})

(defn new-runner
  [user r-identity deck]
  {:agenda-points 0
   :agenda-points-req 7
   :brain-damage 0
   :clicks 0
   :clicks-per-turn 4
   :credits 5
   :deck deck
   :discard []
   :hand []
   :hand-size {:base 5
               :total 5}
   :identity r-identity
   :link 0
   :memory 4
   :play-area []
   :prompt {:select-card false
            :menu-title ""
            :buttons []}
   :ready-to-start false
   :rfg []
   :scored []
   :set-aside []
   :tag {:base 0
         :total 0
         :is-tagged false}
   :toast []
   :user user})

(defn init-hand
  [{:keys [deck] :as player}]
  (let [[hand deck] (split-at 5 (shuffle deck))]
    (assoc player
           :deck (into [] deck)
           :hand (into [] hand))))

(defn set-prompt
  [player new-prompt]
  (let [prompt {:select-card (or (:select-card new-prompt) false)
                :menu-title (or (:menu-title new-prompt) "")
                :buttons (or (:buttons new-prompt) [])}]
    (assoc player :prompt prompt)))

(defn get-corp-state
  [player active-player?]
  nil)

(defn get-runner-state
  [player active-player?]
  nil)

(defn get-player-state
  [player side active-player?]
  (let [user (:user player)]
    (merge
      {:user (select-keys user [:username :id])
       :identity (:identity player)
       :deck-count (count (:deck player))
       :hand (if active-player?
               (:hand player)
               (into [] (repeat (count (:hand player)) {})))
       :discard (:discard player)
       :prompt (when active-player? (:prompt player))
       :agenda-points (:agenda-points player)}
      (select-keys player [:play-area :scored :rfg :set-aside])
      (get-corp-state player active-player?)
      (get-runner-state player active-player?))))
