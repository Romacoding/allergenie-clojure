(ns allergenie.app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            #_[hiccup.core :refer :all]
            [hiccup.page :refer [html5 include-js]]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [allergenie.util :as util]
            [allergenie.pages.glossary :as g]
            [allergenie.pages.about :as a]
            [allergenie.pages.journal :as j]
            [allergenie.components.head :as h]
            [allergenie.components.nav :as n]
            [allergenie.components.footer :as f]
            [allergenie.components.weather :as w]
            [allergenie.components.input :as i]
            [allergenie.components.pollen :as p]
            [allergenie.components.air :as air])
  (:gen-class))

(def pollen-info (atom {}))
(def zip-info (atom {:zip "11223"}))
(def air-info (atom []))
(def weather-info (atom {}))

(defn main-page [_]
  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered m-4"} "AllerGenie"]
           (n/nav-bar)
           (i/input)
           (f/footer)]
          (include-js "/script.js")]))

(defn forecast-page [req]
  (reset! zip-info {:zip (or (:zip (:params req)) "12345")})
  (reset! air-info (util/get-air (:zip @zip-info)))
  (reset! weather-info (util/get-weather (:zip @zip-info)))
  (reset! pollen-info (util/get-pollen (:zip @zip-info)))
  (swap! pollen-info assoc :level (util/calc-pollen-level (:index @pollen-info)))
  (swap! pollen-info assoc :level-color (util/calc-pollen-color (:index @pollen-info)))
  (swap! weather-info assoc :wind-dir (util/calc-wind-dir (num (:wind-deg @weather-info))))

  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered m-4"} "AllerGenie"]
           (n/nav-bar)
           (i/input)
           [:h3 {:class "title is-3 has-text-centered m-6"} (str "Information for: " (:location @pollen-info) " " (:zip @zip-info))]
           [:div {:class "box columns is-8"}
            (air/air @air-info)
            (p/pollen @pollen-info)
            (w/weather @weather-info)]]]
         (f/footer)))

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
