(ns pit-plugin.actions)

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

;; Macro - Dispatch!
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
(defmacro dispatch! [state & args]
  (let [params args
        indexed-params (map-indexed (fn [i x] [i x]) params)
        partitioning (fn [[index item]] (if (or (keyword? item)
                                      (and (coll? item)
                                           (not= (symbol "!") (first item))))
                                -1
                                index))
        partitioned-params (partition-by partitioning indexed-params)
        mapped-params (map (fn [x]
                             (let [remove-index (map second x)
                                   remove-bang (map (fn [y] (if (or (keyword? y)
                                                                    (not= '! (first y)))
                                                              y
                                                              (vec (rest y))))
                                                    remove-index)]
                               (vec (concat
                                      [(if (= -1 (partitioning (first x))) :normal :side-effect)]
                                      (map second x)))))
                           partitioned-params)
        final-params (map (fn [x]
                            (if (= :side-effect (first x))
                              [:side-effect (vec (rest (second x)))]
                              x))
                          mapped-params)]
    `(pit-plugin.actions/-dispatch! ~state ~@final-params)))

;; Macros - Defaction
(defn -defaction [bang? action-name & body]
  (assert (symbol? action-name) "The action name has to be a symbol")
  (let [fn-name (gensym)


        doc-string (when (string? (first body)) (first body))
        body (if doc-string (rest body) body)

        in-coll? (list? (first body))
        body (if in-coll? body (list body))
        body (map (fn [[params & rest-body]]
                    (list params `(let [~'reaction ~fn-name]
                                    ~@rest-body)))
                  body)

        action-ns (str *ns*)
        signatures (->> body
                        (map first)
                        #_(map #(mapv str %)))

        action-list-key (if bang?
                          [(symbol "!") (keyword action-name)]
                          (keyword action-name))
        defmulti-key (if bang?
                       'pit-plugin.actions/apply-action!
                       'pit-plugin.actions/apply-action)]
    (swap! actions-list
           assoc action-list-key
           {:params signatures
            :namespace action-ns
            :documentation doc-string})
    `(do
       (defn ~fn-name ~@body)
       (defmethod ~defmulti-key ~(keyword action-name)
         [& ~'params]
         (apply ~fn-name (keep-indexed #(when (not= 1 %1) %2)
                                       ~'params))))))
(defmacro defaction! [& params]
  (apply -defaction true params))
(defmacro defaction [& params]
  (apply -defaction false params))

;; Functions - RemoveAction
(defn remove-action [action-name]
  (assert (keyword? action-name) "The action name has to be a keyword")
  (swap! actions-list dissoc action-name)
  (remove-method apply-action action-name))
(defn remove-action! [action-name]
  (assert (keyword? action-name) "The action name has to be a keyword")
  (swap! actions-list dissoc action-name)
  (remove-method apply-action! action-name))
