(ns jnet.set-up-test
  (:require [clojure.test :refer :all]
            [test-helper :refer :all]
            [jnet.set-up :as set-up]))

(deftest init-game-test
  (let [game (set-up/init-game {:game-id "1" :name "test"})]
    (is (= 0 (:turn game)))
    (is (= "1" (:game-id game)))
    (is (= "test" (:name game)))
    (is (= "Keep starting hand?" (get-in game [:corp :prompt :menu-title])))))
