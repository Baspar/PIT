(ns {{name}}.components.actions
  (:require [pit-plugin.actions :refer-macros [dispatch!]]
            [rum.core :refer [defc]]
            [{{name}}.components.common-ui :as ui]))

(defc counter [state]
  [:div
   [:div
    [:button {:on-click #(dispatch! state :dec :log)} "-"]
    (str (@state :cpt 0))
    [:button {:on-click #(dispatch! state :inc :log)} "+"]]
   [:button {:on-click #(dispatch! state [! :reset] :log)} "Reset"]])
