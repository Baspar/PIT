(ns {{name}}.components.async-library
  (:require [cljs.core.async :as async :refer  [<!]]
            [rum.core :refer [defc]]
            [kvlt.chan :as kvlt]
            [{{name}}.components.common-ui :as ui])
  (:require-macros [cljs.core.async.macros :refer  [go]]))

(def url "http://petstore.swagger.io/v2/swagger.json")

(defc get-info [state]
      [:button {:on-click #(go
                             (let  [{:keys  [status body]}  (<! (kvlt.chan/request! {:url url :as :json}))]
                               (if (= status 200) (swap! state assoc :get-info body) "err")))} "GET REQUEST"])
