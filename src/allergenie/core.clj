(ns allergenie.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [allergenie.util :refer [get-pollen calc-wind-dir calc-pollen-level get-air get-weather calc-pollen-color]]
            [allergenie.components.glossary :refer [glossary-page]]
            [allergenie.components.about :refer [about-page]]
            [allergenie.components.journal :refer [journal-page]]
            [allergenie.components.nav :refer [nav-bar]]
            [allergenie.components.footer :refer [footer]]
            [allergenie.components.weather :refer [weather]]
            [allergenie.components.input :refer [input]]
            [allergenie.components.pollen :refer [pollen]]
            [allergenie.components.air :refer [air]])
  (:gen-class))

(def pollen-info (atom {}))
(def zip-info (atom {:zip "11223"}))
(def air-info (atom []))
(def weather-info (atom {}))

(defn main-page [_]
  (html5 {:lang "en"}
         [:head [:title "AllerGenie"]
          (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
          (include-js "/script.js")
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered m-4"} "AllerGenie"]
           (nav-bar)
           (input)
           (footer)]]))

(defn forecast-page [req]
  (reset! zip-info {:zip (:zip (:params req))})
  (reset! air-info (get-air (:zip @zip-info)))
  (reset! weather-info (get-weather (:zip @zip-info)))
  (reset! pollen-info (get-pollen (:zip @zip-info)))
  (swap! pollen-info assoc :level (calc-pollen-level (:index @pollen-info)))
  (swap! pollen-info assoc :level-color (calc-pollen-color (:index @pollen-info)))
  (swap! weather-info assoc :wind-dir (calc-wind-dir (num (:wind-deg @weather-info))))

  (html5 {:lang "en"}
         [:head [:title "AllerGenie"]
          (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered m-4"} "AllerGenie"]
           (nav-bar)
           (input)
           [:h3 {:class "title is-3 has-text-centered m-6"} (str "Information for: " (:location @pollen-info) " " (:zip @zip-info))]
           [:div {:class "box columns is-8"}
            (air @air-info)
            (pollen @pollen-info)
            (weather @weather-info)]]]
         (footer)))

(defroutes app
  (GET "/" [] main-page)
  (GET "/about" [] about-page)
  (GET "/info" [] forecast-page)
  (GET "/journal" [] journal-page)
  (GET "/glossary" [] glossary-page)
  (not-found "<h1>Sorry, page not found!</h1>"))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") (:port env)))]
    (run-jetty (wrap-defaults app site-defaults) {:port port})))