(defproject allergenie "0.1.0-SNAPSHOT"
  :description "Your allergy forecast"
  :url "https://clojure-allergenie.herokuapp.com"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.0"]
                 [compojure "1.6.2"]
                 [yogthos/config "1.1.7"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-session-timeout "0.2.0"]
                 [clj-http "3.10.2"]
                 [org.clojure/data.json "1.0.0"]
                 [hiccup "1.0.5"]
                 [com.novemberain/monger "3.1.0"]
                 [markdown-clj "1.10.5"]]
  :main ^:skip-aot allergenie.app
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :min-lein-version "2.0.0"
  :repl-options {:init-ns app.core})