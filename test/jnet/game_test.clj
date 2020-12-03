(ns jnet.game-test
  (:require [clojure.test :refer :all]
            [jnet.game :refer :all]
            [jnet.pipeline :refer :all]
            [jnet.actions :refer [process-action]]
            [test-helper :refer :all]))

(deftest make-pipeline-test
  (let [gp (make-pipeline)]
    (is (= [] (:pipeline gp)))
    (is (= [] (:queue gp)))
    (is (nil? (:next-step gp))))
  (let [gp (make-pipeline :test)]
    (is (= [:test] (:pipeline gp)))
    (is (= [] (:queue gp)))
    (is (nil? (:next-step gp))))
  (let [gp (make-pipeline [:test])]
    (is (= [:test] (:pipeline gp)))
    (is (= [] (:queue gp)))
    (is (nil? (:next-step gp)))))

(deftest process-steps-test
  (let [game (-> (make-game)
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue true
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (process-steps))]
    (is (= 5 (count (get-in game [:corp :hand]))))
    (is (= 3 (count (get-in game [:gp :pipeline])))))
  (let [game (-> (make-game)
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue true
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (process-steps)
                 (continue-next-step)
                 (process-steps))]
    (is (= 7 (count (get-in game [:corp :hand]))))
    (is (= 1 (count (get-in game [:gp :pipeline]))))))

(deftest start-of-game-test
  (let [game (-> (make-game)
                 (process-action :corp "keep"))]
    (println (get-in game [:corp :prompt]))
    ))
