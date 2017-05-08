(ns {{name}}.state
  (:require [pit-plugin.history :refer [bind-history-state]]))

;; Atom page validator
;;
;; :page has to be either:
;;    - a keyword
;;    - a collection of keyword
(defn page-valid? [m]
  (let [page (get m :page [])]
    (and (coll? page)
             (every? (fn [x] (or (number? x)
                                 (keyword? x)))
                     page))))

;; Atom declaration
(defonce app-state (atom {} :validator page-valid?))

(def not-devcards? (. js/document getElementById "container")) 
(when not-devcards? (bind-history-state app-state [:page]))
