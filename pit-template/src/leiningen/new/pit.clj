(ns leiningen.new.pit
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]
            [clojure.string :as string :only (join split)]
            ))

(def render (renderer "pit"))


(defn wrap-indent  [wrap n list]
  (fn  []
    (->> list
      (map-indexed #(str (when (not= 0 %1) (apply str (repeat n " "))) (wrap %2)))
      (string/join "\n"))))

(defn indent  [n list]
    (wrap-indent identity n list))

(defn pit
  "FIXME: write documentation"
  [name & params]
  (let [async? (some #(= "+async" %) params)
        default-dependencies ['[org.clojure/core.match      "0.3.0-alpha4"]
                              '[org.clojure/clojure         "1.8.0"]
                              '[org.clojure/clojurescript   "1.9.229"]
                              '[pit-plugin                  "0.1.3"]
                              '[devcards "0.2.2" :exclusions [cljsjs/react]]
                              '[rum "0.10.7" :exclusions [cljsjs/react]]
                              '[cljsjs/react-dom            "15.4.2-2"]
                              '[cljsjs/react-with-addons "15.4.2-2"]]
        deps (cond-> default-dependencies
                               async?       (conj '[org.clojure/core.async "0.2.395"] '[io.nervous/kvlt "0.1.4"])
                               (not async?) (conj '[cljs-ajax "0.5.8"]))
        data {:name name
              :sanitized (name-to-path name)
              :dependencies (indent 17 (map pr-str deps))
              :api-lib (if async? 'async 'ajax)
              }
        raw-files [;; Project
                   ["project.clj" (render "project.clj" data)]

                   ;; Core files
                   ["src/{{sanitized}}/core.cljs" (render "src/core.cljs" data)]
                   ["src/{{sanitized}}/state.cljs" (render "src/state.cljs" data)]
                   ["src/{{sanitized}}/ui.cljs" (render "src/ui.cljs" data)]

                   ;; Utils
                   ["src/{{sanitized}}/utils/ui.cljs" (render "src/utils/ui.cljs" data)]

                   ;; Cards
                   ["src/{{sanitized}}/cards/core.cljs" (render "src/cards/core.cljs" data)]
                   ["src/{{sanitized}}/cards/app.cljs" (render "src/cards/app.cljs" data)]
                   ["src/{{sanitized}}/cards/actions.cljs" (render "src/cards/actions.cljs" data)]
                   (if async?
                     ["src/{{sanitized}}/cards/async.cljs" (render "src/cards/async.cljs" data)]
                     ["src/{{sanitized}}/cards/ajax.cljs" (render "src/cards/ajax.cljs" data)])

                   ;; Dispatch
                   ["src/{{sanitized}}/actions/core.cljs" (render "src/actions/core.cljs" data)]

                   ;; Components
                   ["src/{{sanitized}}/components/app.cljs" (render "src/components/app.cljs" data)]
                   ["src/{{sanitized}}/components/common_ui.cljs" (render "src/components/common_ui.cljs" data)]
                   ["src/{{sanitized}}/components/actions.cljs" (render "src/components/actions.cljs" data)]
                   (if async?
                     ["src/{{sanitized}}/components/async_library.cljs" (render "src/components/async_library.cljs" data)]
                     ["src/{{sanitized}}/components/ajax_library.cljs" (render "src/components/ajax_library.cljs" data)])

                   ;; Ressouces
                   ["resources/public/cards.html" (render "resources/public/cards.html" data)]
                   ["resources/public/index.html" (render "resources/public/index.html" data)]
                   ["resources/public/css/style.css" (render "resources/public/css/style.css" data)]
                   [".gitignore" (render ".gitignore" data)]]
        files (filter some? raw-files)]

    (main/info "Generating fresh 'lein new' pit project.")
    (when async?
      (main/info "    Using core.async and KLVT for asynchronous programming and HTTP requests"))
    (apply ->files data files)))
