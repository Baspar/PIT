(ns pit-plugin.actions)

;; Atom
(def actions-list (atom {}))

;; Multimethods
(defmulti apply-action
  (fn [_ action-name & _]
    action-name))
(defmethod apply-action :default
  [m action-name & _]
  (println (str "/!\\ Cannot find action \"" action-name "\""))
  (println (str "     You can define it with the function (defaction " (name action-name) " [m] (...))"))
  m)
(defmulti apply-action!
  (fn [_ action-name & _]
    action-name))
(defmethod apply-action! :default
  [_ action-name & _]
  (println (str "/!\\ Cannot find side-affect action \"" (name action-name) "\""))
  (println (str "     You can define it with the function (defaction! " action-name " [state] (...))")))

;; Helpers
(defn atom? [x]
  (= cljs.core/Atom (type x)))

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
