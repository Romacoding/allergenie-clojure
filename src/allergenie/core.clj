(ns allergenie.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [rum.core :refer [defc render-static-markup]]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]])
  (:gen-class))

(defroutes app
  (GET "/" [] "<h1>Test header</h1>")
  (GET "/about" [] "<h1>About me</h1>"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty (wrap-defaults app site-defaults) {:port (:port env)}))
