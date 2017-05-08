(ns {{name}}.utils.history)

(defn back []
  "Go back in history"
  (.. js/window -history back))

(defn forward []
  "Go forward in history"
  (.. js/window -history forward))
