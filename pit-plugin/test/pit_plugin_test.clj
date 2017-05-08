(ns pit-plugin-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.pprint :refer [pprint]]
            [pit-plugin.actions :refer [defaction dispatch! actions-list]]))

(defaction test-TWO
  "Action for test 2"
  [mm]
  (assoc mm :test 1))
(defaction test-one
  "Action for test 1"
  [mm]
  (assoc mm :test 1))

(deftest xxx
  (testing "The test"
    (let [sstate (atom {})]
      (dispatch! sstate [:test-one])
      (is (= {:test 1}
             @sstate)))))

(pprint @actions-list)
