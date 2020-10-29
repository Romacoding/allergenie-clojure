(ns allergenie.app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [hiccup.page :refer [html5 include-js]]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [allergenie.util :as util]
            [allergenie.pages.glossary :as g]
            [allergenie.pages.about :as a]
            [allergenie.pages.journal :as j]
            [allergenie.components.head :as head]
            [allergenie.components.header :as h]
            [allergenie.components.nav :as n]
            [allergenie.components.footer :as f]
            [allergenie.components.weather :as w]
            [allergenie.components.input :as i]
            [allergenie.components.pollen :as p]
            [allergenie.components.air :as air])
  (:gen-class))

(def pollen-info (atom {}))
(def air-info (atom []))
(def weather-info (atom {}))

(defn main-page [_]
  (html5 {:lang "en"}
         (head/head)
         [:body
          [:div {:class "container"}
           (h/header)
           (n/nav-bar)
           (i/input)
           (f/footer)]
          (include-js "/script.js")]))

(defn forecast-page [req]
  (let [zip (or (:zip (:params req)) "12345")]
    (reset! air-info (util/get-air zip))
    (reset! weather-info (util/get-weather zip))
    (reset! pollen-info (util/get-pollen zip))

    (html5 {:lang "en"}
           (head/head)
           [:body
            [:div {:class "container"}
             (h/header)
             (n/nav-bar)
             (i/input)
             [:h3 {:class "title is-3 has-text-centered m-6"} (str "Information for: " (:location @pollen-info) " " zip)]
             [:div {:class "box columns is-8"}
              (air/air @air-info)
              (p/pollen @pollen-info)
              (w/weather @weather-info)]]]
           (f/footer))))

(defroutes app
  (GET "/" [] main-page)
  (GET "/about" [] a/about-page)
  (GET "/info" [] forecast-page)
  (GET "/journal" [] j/journal-page)
  (GET "/glossary" [] g/glossary-page)
  (not-found "<h1>Sorry, page not found!</h1>"))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") (:port env)))]
    (run-jetty (wrap-defaults app site-defaults) {:port port})))
