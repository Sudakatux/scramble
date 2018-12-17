(defproject scramble "0.1.0-SNAPSHOT"
  :description "Scramble sample clojure+clojurescript app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [ring-server "0.5.0"]
                 [reagent "0.8.1"]
                 [reagent-utils "0.3.1"]
                 [ring "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [cljs-ajax "0.7.4"]
                 [yogthos/config "1.1.1"]
                 [org.clojure/clojurescript "1.10.439"
                  :scope "provided"]
                 [metosin/reitit "0.2.9"]
                 [metosin/reitit-spec "0.1.0"]
                 [venantius/accountant "0.2.4"
                  :exclusions [org.clojure/tools.reader]]]

   :plugins [[lein-environ "1.1.0"]
            [lein-cljsbuild "1.1.7"]
            [lein-asset-minifier "0.2.7"
             :exclusions [org.clojure/clojure]]]

  :ring {:handler scramble.handler/app
         :uberwar-name "scramble.war"}

  :min-lein-version "2.5.0"
  :uberjar-name "scramble.jar"
  :main scramble.server
  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  :resource-paths ["resources" "target/cljsbuild"]

   :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to        "target/cljsbuild/public/js/app.js"
              :output-dir       "target/cljsbuild/public/js"
              :source-map       "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel {:on-jsload "scramble.core/mount-root"}
             :compiler
             {:main "scramble.dev"
              :asset-path "/js/out"
              :output-to "target/cljsbuild/public/js/app.js"
              :output-dir "target/cljsbuild/public/js/out"
              :source-map true
              :optimizations :none
              :pretty-print  true}}
            :js-dom-test
            {:source-paths ["src/cljs" "src/cljc" "test/cljs" "env/dev/cljs"]
             :notify-command ["node" "node-unit-tests.js"]
             :compiler
             {:main "scrumble.components-test-runner"
              :output-to "test.js" #_"target/cljsbuild/dom-test/test.js"
              :output-dir "target/cljsbuild/dom-test/js"
              :optimizations :simple
              :pretty-print  false}}



            }
   }

  :figwheel
  {:http-server-root "public"
   :server-port 3449
   :nrepl-port 7002
   :nrepl-middleware [cider.piggieback/wrap-cljs-repl
                      cider.nrepl/cider-middleware
                      refactor-nrepl.middleware/wrap-refactor
                      ]
   :css-dirs ["resources/public/css"]
   :ring-handler scramble.handler/app}



  :profiles {:dev {:repl-options {:init-ns scramble.repl}
                   :dependencies [[cider/piggieback "0.3.10"]
                                  [binaryage/devtools "0.9.10"]
                                  [ring/ring-mock "0.3.2"]
                                  [ring/ring-devel "1.7.1"]
                                  [prone "1.6.1"]
                                  [figwheel-sidecar "0.5.17"]
                                  [nrepl "0.4.5"]
                                  [midje "1.9.4"]
                                  [pjstadig/humane-test-output "0.9.0"]]
                   :test-paths ["test/cljc"]
                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.17"]
                             [cider/cider-nrepl "0.18.0"]
                             [lein-midje "3.2.1"]
                             [org.clojure/tools.namespace "0.3.0-alpha4"
                              :exclusions [org.clojure/tools.reader]]
                             [refactor-nrepl "2.4.0"
                              :exclusions [org.clojure/clojure]]]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})
