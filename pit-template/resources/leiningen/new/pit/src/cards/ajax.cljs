(ns {{name}}.cards.ajax
  (:require [{{name}}.components.ajax-library :refer [get-info]])
  (:require-macros [devcards.core :refer [defcard]]))

; define hostname of api
;; (def url "http://petstore.swagger.io/v2/swagger.json")

(defcard get-info
         "# HTTP request with cljs-ajax
         ## cljs-ajax
         cljs-ajax is a library for HTTP request based on callbacks.

         There are multiple main functions, like:

           - **POST**: function used for a post request
           - **GET** function used for a get request

         Those functions have to be followed by the URL of the request, and a map of parameters among:

           - **handler** function: calback function in case of success of the request.
           - **error-handler** function: calback function in case of failure of the request.
           - **body** map: content of the body of the request.
           - **params** map: content of the parameters of the request.

         Full documentation can be found at [https://github.com/JulianBirch/cljs-ajax](https://github.com/JulianBirch/cljs-ajax)

         ## Code snippet

         ```clojure
         ;; Requiring ajax.core library
         (ns example
           (:require [ajax.core :refer [GET POST]]))

         ;; Function called on on-click event
         #(GET url {:handler (fn [x] (swap! state assoc :get-info (js->clj x)))
                    :error-handler (fn [x] (println (str \"err\" x)))})
         ```"
         (fn [state]
           (get-info state))
         {:get-info ""}
         {:inspect-data true})




