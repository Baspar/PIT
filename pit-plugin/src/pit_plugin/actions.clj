(ns pit-plugin.actions)

(defn partitioning [[index item]]
    (if (or (keyword? item)
            (and (coll? item)
                 (not= (symbol "!") (first item))))
        -1
        index))

(defmacro dispatch! [state & args]
    (let [params args
          indexed-params (map-indexed (fn [i x] [i x]) params)
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
        `(pit-plugin.actions/-dispatch! ~'state ~@final-params)))

(defmacro defaction! [action-name & body]
    (assert (symbol? action-name) "The action name has to be a symbol")
    (let [fn-name (gensym)

          doc-string (when (string? (first body)) (first body))
          body (if doc-string (rest body) body)

          in-coll? (list? (first body))
          body (if in-coll? body (list body))
          body (map (fn [[params & rest-body]]
                        (list params `(let [~'reaction ~fn-name]
                                          ~@rest-body)))
                    body)]
        `(do
             (defn ~fn-name ~@body)
             (defmethod apply-action! ~(keyword action-name)
                 [& ~'params]
                 (apply ~fn-name (keep-indexed #(when (not= 1 %1) %2)
                                               ~'params))))))

(defmacro defaction [action-name & body]
    (assert (symbol? action-name) "The action name has to be a symbol")
    (let [fn-name (gensym)

          doc-string (when (string? (first body)) (first body))
          body (if doc-string (rest body) body)

          in-coll? (list? (first body))
          body (if in-coll? body (list body))
          body (map (fn [[params & rest-body]]
                        (list params `(let [~'reaction ~fn-name]
                                          ~@rest-body)))
                    body)]
        `(do
             (defn ~fn-name ~@body)
             (defmethod apply-action ~(keyword action-name)
                 [& ~'params]
                 (apply ~fn-name (keep-indexed #(when (not= 1 %1) %2)
                                               ~'params))))))

