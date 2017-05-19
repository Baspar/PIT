(ns pit-plugin.actions)

;; Macro - Dispatch!
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

;; Defaction
(defn -defaction [action-infos bang? action-name & body]
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
                        (mapv first)
                        (mapv #(mapv str %)))

        action-list-key (if bang?
                          (keyword (str action-name " !"))
                          (keyword action-name))
        defmulti-key (if bang?
                       'pit-plugin.actions/apply-action!
                       'pit-plugin.actions/apply-action)
        action-line (:line action-infos)]

    `(do
       (swap! actions-list
              update-in [:actions ~action-list-key]
              #(merge %
                      {:params ~signatures
                       :namespace ~action-ns
                       :line ~action-line
                       :documentation ~doc-string}))
       (defn ~fn-name ~@body)
       (defmethod ~defmulti-key ~(keyword action-name)
         [& ~'params]
         (apply ~fn-name (keep-indexed #(when (not= 1 %1) %2)
                                       ~'params))))))

;; Macros - Defaction
(defmacro defaction! [& params]
  (let [action-infos (meta &form)]
    (apply -defaction action-infos true params)))
(defmacro defaction [& params]
  (let [action-infos (meta &form)]
    (apply -defaction action-infos false params)))
