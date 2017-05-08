(ns pit-plugin-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.pprint :refer [pprint]]
            [pit-plugin.actions :refer [remove-action defaction dispatch! actions-list]]))

(defaction inc-or-assoc
  "Action for test 1"
  ([m]
   (update m :test #(if % (inc %) 1)))
  ([m value]
   (assoc m :test value)))
(deftest simple-call-test
  (testing "Call without params"
    (let [state (atom {})]
      (dispatch! state :inc-or-assoc)
      (is (= {:test 1}
             @state))))

  (testing "Call with a param"
    (let [state (atom {})]
      (dispatch! state [:inc-or-assoc 42])
      (is (= {:test 42}
             @state)))))

(deftest actions-list-test
  (testing "Actions-list after adding action"
    (is (= @actions-list {:inc-or-assoc {:params '(["m"] ["m" "value"]),
                                       :namespace "pit-plugin-test",
                                       :documentation "Action for test 1"}})))
  (remove-action :inc-or-assoc)
  (testing "Action-list after removing action"
    (is (= @actions-list {}))))

;; (pprint @actions-list)
