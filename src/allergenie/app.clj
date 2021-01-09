(ns allergenie.app
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.util.response :as resp]
            [compojure.core :refer [defroutes GET POST DELETE routes wrap-routes]]
            [compojure.route :refer [not-found]]
            [hiccup.page :refer [html5 include-js]]
            [config.core :refer [env]]
            [allergenie.util :as util]
            [allergenie.db :as db]
            [allergenie.state :as state]
            [allergenie.pages :as pages]
            [allergenie.components :as components]
            [allergenie.admin :refer [check-login]])
  (:gen-class))

(defn main-page [_]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:div {:class "container"}
           (components/header)
           (components/nav-bar)
           (components/input)
           (components/footer)]
          (include-js "/script.js")]))

(defn forecast-page [req]
  (let [zip (or (:zip (:params req)) "12345")]
    (reset! state/air-info (util/get-air zip))
    (reset! state/weather-info (util/get-weather zip))
    (reset! state/pollen-info (util/get-pollen zip))

    (html5 {:lang "en"}
           (components/head)
           [:body
            [:div {:class "container"}
             (components/header)
             (components/nav-bar)
             (components/input)
             [:h3 {:class "title is-3 has-text-centered m-6"} (str "Information for " (:location @state/pollen-info) " " zip)]
             [:div {:class "columns is-8"}
              (components/air)
              (components/pollen)
              (components/weather)]]]
           (components/footer))))

(defroutes app
           (GET "/" [] main-page)
           (GET "/about" [] pages/about-page)
           (GET "/info" [] forecast-page)
           (GET "/glossary" [] pages/glossary-page)
           (GET "/admin/login" [:as {session :session}]
             (if (:admin session)
               (resp/redirect "/journal")
               (pages/login-page)))
           (GET "/admin/logout" []
             (-> (resp/redirect "/")
                 (assoc-in [:session :admin] false)))
           (POST "/admin/login" [login password]
             (if (check-login login password)
               (-> (resp/redirect "/journal")
                   (assoc-in [:session :admin] true))
               (pages/login-page "Invalid username or password")))
           (not-found "<h1>Sorry, page not found!</h1>"))

(defroutes admin-routes
           (GET "/journal" [] pages/journal-page)
           (GET "/records/new" [] (pages/edit-record nil))
           (GET "/records/:rec-id" [rec-id] (pages/record (db/get-record-by-id rec-id)))
           (GET "/records/:rec-id/edit" [rec-id] (pages/edit-record (db/get-record-by-id rec-id)))
           (POST "/records" [title body]
             (let [pollen-index (:index @state/pollen-info)
                   triggers (:triggers (first @state/pollen-info))
                   weather (:description @state/weather-info)
                   air-index (:aqi (first @state/air-info))]
               (do (db/create-record title body pollen-index weather air-index triggers)
                   (resp/redirect "/journal"))))
           (POST "/records/:rec-id" [rec-id title body]
             (do (db/update-record rec-id title body))
             (resp/redirect (str "/records/" rec-id)))
           (DELETE "/records/:rec-id" [rec-id]
             (do (db/delete-record rec-id))
             (resp/redirect "/journal")))

(defn wrap-admin-only
  "Middleware function to check in admin session is true"
  [handler]
  (fn [request]
    (if (-> request :session :admin)
      (handler request)
      (resp/redirect "/admin/login"))))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") (:port env)))]
    (run-jetty (-> (routes (wrap-routes admin-routes wrap-admin-only)
                           app)
                   (wrap-defaults site-defaults)
                   wrap-session) {:port port})))
