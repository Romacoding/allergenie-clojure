(ns allergenie.app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET POST DELETE]]
            [compojure.route :refer [not-found]]
            [hiccup.page :refer [html5 include-js]]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.util.response :as resp]
            [allergenie.util :as util]
            [allergenie.db :as db]
            [allergenie.pages.glossary :as g]
            [allergenie.pages.about :as a]
            [allergenie.pages.journal :as j]
            [allergenie.pages.record :as r]
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
             [:h3 {:class "title is-3 has-text-centered m-6"} (str "Information for " (:location @pollen-info) " " zip)]
             [:div {:class "columns is-8"}
              (air/air @air-info)
              (p/pollen @pollen-info)
              (w/weather @weather-info)]]]
           (f/footer))))

(defroutes app
  (GET "/" [] main-page)
  (GET "/about" [] a/about-page)
  (GET "/info" [] forecast-page)
  (GET "/journal" [] j/journal-page)
  (GET "/records/new" [] (r/edit-record nil))
  (POST "/records" [title body]
    (do (db/create-record title body)
        (resp/redirect "/journal")))
  (GET "/records/:rec-id" [rec-id] (r/record (db/get-record-by-id rec-id)))
  (GET "/records/:rec-id/edit" [rec-id] (r/edit-record (db/get-record-by-id rec-id)))
  (POST "/records/:rec-id" [rec-id title body]
        (do (db/update-record rec-id title body)
            (resp/redirect (str "/records/" rec-id))))
  (DELETE "/records/:rec-id" [rec-id]
          (do (db/delete-record rec-id)
              (resp/redirect "/journal")))
  (GET "/glossary" [] g/glossary-page)
  (not-found "<h1>Sorry, page not found!</h1>"))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") (:port env)))]
    (run-jetty (wrap-defaults app site-defaults) {:port port})))
