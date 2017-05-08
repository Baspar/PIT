(ns {{name}}.cards.async
  (:require [{{name}}.components.async-library :refer [get-info]])
  (:require-macros [devcards.core :refer [defcard]]))

(defcard get-info
         "# HTTP requests with KVLT and core.async
         ## KVLT
         KVLT is a HTTP request library.

         `request! params` is the major function of the library, that can be called with the following parameters:

           - **url**: url for the request
           - **method**: `:post` or `:get`
           - **body**: map of request parameters
           - **type**: type of the parameters
           - **as**: type of the response

         Full documentation can be found at [https://github.com/nervous-systems/kvlt](https://github.com/nervous-systems/kvlt)

         ## core.async
         Core.async is a library to handle concurrent processes.
         Main component of this library are:

           - **go** macro: creating a \"thread\" where you can use the symbol `<!` and `>!`
           - symbol `<!` : waiting for a go block/channel to return a value.
             It blocks the execution of the go block.
           - symbol `>!` : sending a value to the channel.
             It blocks the execution of the go block until the value can be sent.

         Full documentation can be found at [http://clojure.github.io/core.async/](http://clojure.github.io/core.async/)

         ## Code snippet

         ```
         ;; Requiring kvlt and core.async libraries
         (ns yoloo.components.async-library
              (:require [cljs.core.async :as async :refer [<!]]
                        [kvlt.chan :as kvlt])
              (:require-macros [cljs.core.async.macros :refer [go]]))

         ;; Function called on on-click event
         #(go
             (let [{:keys  [status body]} (<! (kvlt.chan/request! {:url url :as :json}))]
                (if (= status 200) (swap! state assoc :get-info body) \"err\")))
         ```"
         (fn [state]
           (get-info state))
         {:get-info ""}
         {:inspect-data true})
