(ns jnet.pipeline-test
  (:require [clojure.test :refer :all]
            [jnet.game :refer :all]
            [jnet.pipeline :refer :all]
            [test-helper :refer :all]))

(comment
  (make-pipeline)
  (make-pipeline :asdf)
  (make-pipeline [:asdf]))

(comment
  (-> {:gp (make-pipeline {:name "1"})}
      (queue-step {:name "2"})
      (queue-step {:name "3"})
      (cancel-step)))

(comment
  (-> {:gp (make-pipeline {:continue false
                           :step-fn (fn [game] 1)})}
      (queue-step {:continue true
                   :step-fn (fn [game] 2)})
      (queue-step {:continue true
                   :step-fn (fn [game] 3)})
      (prepare-current-step)
      clojure.pprint/pprint))

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

(deftest continue-test
  (let [game (-> (make-game)
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue true
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (continue))]
    (is (= 5 (count (get-in game [:corp :hand]))))
    (is (= 3 (count (get-in game [:gp :pipeline])))))
  (let [game (-> (make-game)
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue true
                              :step-fn (fn [game] (draw game :corp 1))})
                 (queue-step {:continue false
                              :step-fn (fn [game] (draw game :corp 1))})
                 (continue)
                 (assoc-in [:gp :pipeline 0 :continue] true)
                 (continue))]
    (is (= 7 (count (get-in game [:corp :hand]))))
    (is (= 1 (count (get-in game [:gp :pipeline]))))))
