(ns {{name}}.cards.app
  (:require [{{name}}.components.app :as app])
  (:require-macros [devcards.core :refer [defcard]]))

(defcard app-card
  (fn [state]
    (app/app state))
  {}
  {:inspect-data true})
