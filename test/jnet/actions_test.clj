(ns jnet.actions-test
  (:require [clojure.test :refer :all]
            [jnet.game :refer :all]
            [jnet.pipeline :refer :all]
            [jnet.actions :refer [process-action]]
            [test-helper :refer :all]))

(deftest mulligan-test
  (testing "keeping hand"
    (let [game (make-game)]
      (is (= "Keep starting hand?" (get-in game [:corp :prompt :menu-title])))
      (is (= "Keep starting hand?" (get-in game [:runner :prompt :menu-title]))))
    (let [game (-> (make-game)
                   (process-action :corp "keep"))]
      (is (nil? (get-in game [:corp :mulligan])))
      (is (= "Waiting for opponent to keep or mulligan"
             (get-in game [:corp :prompt :menu-title])))
      (is (= "Keep starting hand?" (get-in game [:runner :prompt :menu-title]))))
    (let [game (-> (make-game)
                   (process-action :corp "keep")
                   (process-action :runner "keep"))]
      (is (nil? (get-in game [:corp :mulligan])))
      (is (nil? (get-in game [:runner :mulligan])))
      (is (= "Waiting for opponent to keep or mulligan" (get-in game [:corp :prompt :menu-title])))
      (is (= "Waiting for opponent to keep or mulligan" (get-in game [:runner :prompt :menu-title])))))
  (testing "mulliganing hand"
    (let [game (make-game)]
      (is (= "Keep starting hand?" (get-in game [:corp :prompt :menu-title])))
      (is (= "Keep starting hand?" (get-in game [:runner :prompt :menu-title]))))
    (let [game (-> (make-game)
                   (process-action :corp "mulligan"))]
      (is (true? (get-in game [:corp :mulligan])))
      (is (= "Waiting for opponent to keep or mulligan"
             (get-in game [:corp :prompt :menu-title])))
      (is (= "Keep starting hand?" (get-in game [:runner :prompt :menu-title]))))
    (let [game (-> (make-game)
                   (process-action :corp "mulligan")
                   (process-action :runner "mulligan"))]
      (is (true? (get-in game [:corp :mulligan])))
      (is (true? (get-in game [:runner :mulligan])))
      (is (= "Waiting for opponent to keep or mulligan" (get-in game [:corp :prompt :menu-title])))
      (is (= "Waiting for opponent to keep or mulligan" (get-in game [:runner :prompt :menu-title]))))))
