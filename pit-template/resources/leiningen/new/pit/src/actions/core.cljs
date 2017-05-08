(ns {{name}}.actions.core
  (:require [clojure.pprint :refer [pprint]]
            [pit-plugin.actions :refer-macros [defaction! defaction]]))

(defaction inc
  [m]
  (update m :cpt inc))

(defaction dec
  [m]
  (update m :cpt dec))

(defaction log
  [m]
  (pprint m)
  m)

(defaction! reset
  [state]
  (swap! state assoc :cpt 0))
