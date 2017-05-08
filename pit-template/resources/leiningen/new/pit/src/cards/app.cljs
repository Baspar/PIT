(ns {{name}}.cards.app
  (:require [{{name}}.ui :as ui])
  (:require-macros [devcards.core :refer [defcard]]))

(defcard app
  (fn [state]
    (ui/app state))
  {}
  {:inspect-data true})
