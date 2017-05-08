(ns {{name}}.ui
  (:require [{{name}}.state :as state]
            [{{name}}.components.app :as ui]
            [rum.core :as rum]))

;; Render function
(defn render [node]
  (rum/mount (ui/app state/app-state) node))

;; Trigger re-render
(when-let [node (. js/document getElementById "container")]
  (add-watch state/app-state
             :main-watch
             (fn [_ _ _ _]
               (render node)))
  (render node))
