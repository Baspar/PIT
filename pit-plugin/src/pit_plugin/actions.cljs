(ns pit-plugin.actions)

(defmulti apply-action!
  (fn [_ action-name & _]
    action-name))

(defmethod apply-action! :default
  [_ action-name & _]
  (println (str "/!\\ Cannot find side-affect action \"" (name action-name) "\""))
  (println (str "     You can define it with the function (defaction! " action-name " [state] (...))")))

(defmulti apply-action
  (fn [_ action-name & _]
    action-name))

(defmethod apply-action :default
  [m action-name & _]
  (println (str "/!\\ Cannot find action \"" action-name "\""))
  (println (str "     You can define it with the function (defaction " (name action-name) " [m] (...))"))
  m)

(defn -dispatch! [state & instructions]
  (doseq [instruction instructions]
    (let [type-action (first instruction)
          actions (rest instruction)]
      (if (= :normal type-action)
        (swap! state #(reduce (fn [local-state action] (if (coll? action)
                                                         (apply apply-action local-state action)
                                                         (apply-action local-state action)))
                              %
                              actions))
        (doseq [action actions]
          (if (coll? action)
            (apply apply-action! state action)
            (apply-action! state action)))))))
