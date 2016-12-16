(ns {{name}}.ui
  (:require [rum.core :refer [defc]]
            [{{name}}.services :as services]))

(defc app [state]
  [:div
   [:h1 "Welcome to {{name}} app !"]])
