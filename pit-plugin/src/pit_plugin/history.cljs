(ns pit-plugin.history
  (:require [clojure.string :refer [join split]]
            [clojure.reader :refer [read-string]]
            [goog.events :as events]
            [goog.history.EventType :as EventType])
  (:import goog.History))

(defonce h (History.))

(defn bind-history-state
  ([state path] (bind-history-state state path []))
  ([state path args] 
   ;; Bind History => :page
   (doto h
     (events/listen
       EventType/NAVIGATE
       (fn [event]
         (let [token (.-token event)
               array (split token "/")
               keywordize-or-numberize (fn [x]
                                         (let [number? (re-matches #"[0-9]+" x)
                                               numberized (when number? (read-string x))
                                               keywordized (keyword x)]
                                           (if number? numberized keywordized)))
               page (vec (map keywordize-or-numberize (filter #(not= "" %) array)))
               page (if (= [] page) args page)]
           (swap! state assoc-in path page))))
     (.setEnabled true))

   ;; Bind :page => History
   (add-watch state
              :history-watch
              (fn [_ _ old-state new-state]
                (let [old-page (get-in old-state path)
                      new-page (get-in new-state path)] 
                  (when (not= old-page new-page)
                    (let [normalize (fn [x] (if (keyword? x) (name x) x))
                          array (map normalize new-page)
                          token (join "/" array)]
                      (.setToken h token))))))))

