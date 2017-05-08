(ns {{name}}.components.ajax-library
  (:require [ajax.core :refer [GET POST]]
            [rum.core :refer [defc]]
            [{{name}}.components.common-ui :as ui]))

(def url "http://petstore.swagger.io/v2/swagger.json")

(defc get-info [state]
      [:button {:on-click #(GET url {:handler (fn [x] (do
                                                        (println (str "url link : " url))
                                                        (swap! state assoc :get-info (js->clj x))
                                                        (println (js->clj x))))
                                     :error-handler (fn [x] (println (str "err" x)))})} "GET REQUEST"])
