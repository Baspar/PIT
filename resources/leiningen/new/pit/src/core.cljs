(ns {{name}}.core
  (:require [{{name}}.ui :as ui]
            [{{name}}.state :as state]
            [{{name}}.cards]
            [rum.core :as rum]))

(when-let [node (. js/document getElementById "container")]
  (rum/mount (ui/app state/app-state) node))
