(ns leiningen.new.pit
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "pit"))

(defn pit
  "FIXME: write documentation"
  [name & params]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' pit project.")
    (->files data
             ;; Project
             ["project.clj" (render "project.clj" data)]

             ;; CLJS files
             ["src/{{sanitized}}/core.cljs" (render "src/core.cljs" data)]
             ["src/{{sanitized}}/cards.cljs" (render "src/cards.cljs" data)]
             ["src/{{sanitized}}/services.cljs" (render "src/services.cljs" data)]
             ["src/{{sanitized}}/state.cljs" (render "src/state.cljs" data)]
             ["src/{{sanitized}}/ui.cljs" (render "src/ui.cljs" data)]
             ["src/{{sanitized}}/utils.cljs" (render "src/utils.cljs" data)]
             ["src/{{sanitized}}/cards/app.cljs" (render "src/cards/app.cljs" data)]

             ;; Ressouces
             ["resources/public/cards.html" (render "resources/public/cards.html" data)]
             ["resources/public/index.html" (render "resources/public/index.html" data)]
             ["resources/public/css/styles.css" (render "resources/public/index.html" data)]

             )))
