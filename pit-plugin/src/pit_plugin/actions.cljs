(ns pit-plugin.actions
  (:require [clojure.string :refer [join split replace]]
            [pit-plugin.actions-sidecar :refer [actions-component]]
            [pit-plugin.style :refer [style]]
            [rum.core :refer [defc mount]]))


;; Atom
(defonce actions-list (atom {:actions-history []}))

;; Binder
(defn render-actions-list [node]
  (mount (actions-component actions-list) node))
(defn bind-actions-list []
  (let [node (.getElementById js/document "pit--plugin--action--bar")]
    (when-not node
      (let [js-node (.createElement js/document "div")
            css-node (.createElement js/document "style")]

        (aset css-node "innerHTML" style)
        (aset js-node "id" "pit--plugin--action--bar")
        (.appendChild (.-body js/document) js-node)
        (.appendChild (.-head js/document) css-node)

        (add-watch actions-list
                   :actions-list-watch
                   (fn [_ _ old-state new-state]
                     (render-actions-list js-node)))
        (render-actions-list js-node)))))

;; (bind-actions-list)

;; Helpers
(defn atom? [x]
  (= cljs.core/Atom (type x)))
(defn- pop-actions-list [action-name params]
  (let [date (js/Date.)
        h (.getHours date)
        h (str (when (< h 10) 0) h)
        m (.getMinutes date)
        m (str (when (< m 10) 0) m)
        s (.getSeconds date)
        s (str (when (< s 10) 0) s)
        infos {:time (str h ":" m ":" s)
               :name (name action-name)
               :params params}]
    (swap! actions-list update :actions-history
           #(vec (take 50 (concat [infos] %))))))

;; Multimethods
(defmulti apply-action
  (fn [_ action-name & params]
    (when-not (get-in @actions-list [:actions action-name :silent])
      (pop-actions-list action-name params))
    action-name))
(defmethod apply-action nil
  [m & _]
  m)
(defmethod apply-action :default
  [m action-name & body]
  (println "/!\\ Cannot find action \"" action-name "\"\nYou can define it with the function (defaction" action-name "[m] (...))")
  m)
(defmulti apply-action!
  (fn [_ action-name & params]
    (when-not (get-in @actions-list [:actions (keyword (str (name action-name) " !")) :silent])
      (pop-actions-list (keyword (str (name action-name) " !")) params))
    action-name))
(defmethod apply-action! nil
  [& _])
(defmethod apply-action! :default
  [_ action-name & _]
  (println "/!\\ Cannot find side-effect action \"" action-name "\"\nYou can define it with the function (defaction!" action-name "[m] (...))"))

;; Dispatch!
(defn -dispatch! [state & instructions]
  (let [reducer (fn [m actions]
                  (reduce (fn [local-state action]
                            (if (coll? action)
                              (apply apply-action local-state action)
                              (apply-action local-state action)))
                          m
                          actions))]
    (if (atom? state)
      (doseq [instruction instructions]
        (let [type-action (first instruction)
              actions (rest instruction)]

          (if (= :normal type-action)
            (swap! state #(reducer % actions))
            (doseq [action actions]
              (if (coll? action)
                (apply apply-action! state action)
                (apply-action! state action))))))
      (reducer state (rest (first instructions))))))


;; Functions - Remove-action
(defn remove-action [action-name]
  (assert (keyword? action-name) "The action name has to be a keyword")
  (swap! actions-list dissoc action-name)
  (remove-method apply-action action-name))
(defn remove-action! [action-name]
  (assert (keyword? action-name) "The action name has to be a keyword")
  (swap! actions-list dissoc action-name)
  (remove-method apply-action! action-name))
