(ns pit-plugin-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.pprint :refer [pprint]]
            [pit-plugin.actions :refer [defaction dispatch! actions-list]]))

(defaction test-TWO
  "Action for test 2"
  [mm]
  (assoc mm :test 1))
(defaction inc-or-assoc
  "Action for test 1"
  ([m]
   (update m :test #(if % (inc %) 1)))
  ([m value]
   (assoc m :test value)))

(deftest simple-call
  (testing "Call without params"
    (let [state (atom {})]
      (dispatch! state :inc-or-assoc)
      (is (= {:test 1}
             @state))))

  (testing "Call with a param"
    (let [state (atom {})]
      (dispatch! state [:inc-or-assoc 42])
      (is (= {:test 42}
             @state))))

  )

;; (pprint @actions-list)
