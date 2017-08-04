(defproject {{ name }} "0.1.0 SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [{{{dependencies}}}]

  :plugins [[lein-figwheel "0.5.8"]
            [lein-cljsbuild "1.1.4" :exclusions [org.clojure/clojure]]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :source-paths ["src"]

  :cljsbuild {:builds [{:id "devcards"
                        :source-paths ["src"]
                        :figwheel {:devcards true
                                   :open-urls ["http://localhost:3449/cards.html"]}
                        :compiler {:main       "dev.core"
                                   :asset-path "js/compiled/devcards_out"
                                   :output-to  "resources/public/js/compiled/{{sanitized}}_devcards.js"
                                   :output-dir "resources/public/js/compiled/devcards_out"
                                   :source-map-timestamp true }}
                       {:id "dev"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main       "dev.core"
                                   :asset-path "js/compiled/out"
                                   :output-to  "resources/public/js/compiled/{{sanitized}}.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :source-map-timestamp true }}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:main       "{{name}}.core"
                                   :asset-path "js/compiled/out"
                                   :output-to  "resources/public/js/compiled/{{sanitized}}.js"
                                   :optimizations :advanced}}]}

  :figwheel { :css-dirs ["resources/public/css"] }

  :profiles {:dev {:dependencies [[binaryage/devtools "0.8.2"]
                                  [figwheel-sidecar "0.5.8"]
                                  [com.cemerick/piggieback "0.2.1"]]
                   :source-paths ["src" "dev"]
                   :repl-options {:init (set! *print-length* 50)
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
