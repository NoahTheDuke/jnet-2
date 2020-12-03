(ns jnet.pipeline)

(defn make-pipeline
  ([] (make-pipeline []))
  ([steps]
   (let [steps (if (sequential? steps)
                 (into [] steps)
                 [steps])]
     {:pipeline steps
      :queue []
      :continue-step nil})))

(comment
  (make-pipeline)
  (make-pipeline :asdf)
  (make-pipeline [:asdf])
  )

(defn make-step
  [{:keys [continue step-fn card-clicked menu-command]}]
  {:continue (or continue false)
   :step-fn (or step-fn (constantly false))
   :card-clicked (or card-clicked (constantly false))
   :menu-command (or menu-command (constantly false))})

(defn ui-prompt
  [{:keys [active-prompt waiting-prompt]}]
  {:completed false
   :active-prompt (or active-prompt (constantly ""))
   :waiting-prompt (or waiting-prompt "Waiting for opponent")
   })

(defn queue-step
  [game step]
  (update-in game [:gp :queue] conj step))

(defn get-current-step
  [{pipeline :pipeline}]
  (first pipeline))

(defn cancel-step
  [game]
  (update-in game [:gp :pipeline] #(into [] (next %))))

(comment
  (-> {:gp (make-pipeline {:name "1"})}
      (queue-step {:name "2"})
      (queue-step {:name "3"})
      (cancel-step)))

(defn prepare-current-step
  [game]
  (let [gp (:gp game)
        gp (assoc gp
                   :pipeline (into [] (concat (:queue gp) (:pipeline gp)))
                   :queue []
                   :continue-step nil)
        game (assoc game :gp gp)]
    (if (empty? (:pipeline gp))
      game
      (let [step (get-current-step gp)
            continue? (:continue step)
            result (if (fn? continue?)
                     (continue? game)
                     continue?)]
        (assoc game :gp (assoc gp :continue-step result))))))

(comment
  (-> {:gp (make-pipeline {:continue false
                           :step-fn (fn [game] 1)})}
      (queue-step {:continue true
                   :step-fn (fn [game] 2)})
      (queue-step {:continue true
                   :step-fn (fn [game] 3)})
      (prepare-current-step)
      clojure.pprint/pprint))

(defn process-steps
  [game]
  (let [{:keys [gp] :as game} (prepare-current-step game)]
    (if (:continue-step gp)
      (let [step-fn (:step-fn (get-current-step gp))]
        (-> game
            (step-fn)
            (update-in [:gp :pipeline] #(into [] (next %)))
            (recur)))
      game)))

(defn continue-next-step
  [game]
  (assoc-in game [:gp :pipeline 0 :continue] true))

(defn draw
  [game side qty]
  (let [cards (take qty (get-in game [side :deck]))]
    (-> game
        (update-in [side :deck] #(drop qty %))
        (update-in [side :hand] #(into [] (concat % cards))))))
