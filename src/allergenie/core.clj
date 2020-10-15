(ns allergenie.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [config.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [allergenie.util :refer [get-pollen calc-wind-dir calc-pollen-level get-air get-weather]]
            [allergenie.components.glossary :refer [glossary-page]]
            [allergenie.components.about :refer [about-page]]
            [allergenie.components.journal :refer [journal-page]]
            [allergenie.components.nav :refer [nav-bar]])
  (:gen-class))

(def pollen-info (atom {}))
(def zip-info (atom {:zip "11223"}))
(def air-info (atom []))
(def weather-info (atom {}))

(reset! air-info (get-air (:zip @zip-info)))

(reset! weather-info (get-weather (:zip @zip-info)))

(reset! pollen-info (get-pollen (:zip @zip-info)))

(swap! pollen-info assoc :level (calc-pollen-level (:index @pollen-info)))

(swap! weather-info assoc :wind-dir (calc-wind-dir (num (:wind-deg @weather-info))))

(defn main-page [_]
  (html5 {:lang "en"}
         [:head [:title "AllerGenie"]
          (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           
           (nav-bar)

           [:div {:class "box"}
            [:form {:action "/info"}
             [:label {:for "zip"} "Change your location"] [:br]
             [:input {:class "input m-4" :name "zip" :type "tel" :id "zip" :placeholder "enter your zip code" :required ""}]
             [:input {:class "button is-info m-4" :type "submit" :value "Submit"}]]]

           [:footer {:class "footer"}
            [:div {:class "content has-text-centered"}
             [:p [:strong "AllerGenie"] " by " [:a {:href "https://ostash.dev"} "© Roman Ostash 2020"]]]]]]))


(defn forecast-page [req]
  (reset! zip-info {:zip (:zip (:params req))})
  (reset! air-info (get-air (:zip @zip-info)))
  (reset! weather-info (get-weather (:zip @zip-info)))
  (reset! pollen-info (get-pollen (:zip @zip-info)))
  (swap! pollen-info assoc :level (calc-pollen-level (:index @pollen-info)))
  (swap! weather-info assoc :wind-dir (calc-wind-dir (num (:wind-deg @weather-info))))

  (html5 {:lang "en"}
         [:head [:title "AllerGenie"]
          (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
          
           (nav-bar)

           [:div {:class "box"}
            [:form {:action "/info"}
             [:label {:for "zip"} "Change your location"] [:br]
             [:input {:class "input m-4" :name "zip" :type "tel" :id "zip" :placeholder "enter your zip code" :required ""}]
             [:input {:class "button is-info m-4" :type "submit" :value "Submit"}]]]

           [:h3 {:class "title is-3 has-text-centered m-6"} (str "Information for: " (:location @pollen-info) " " (:zip @zip-info))]

           [:div {:class "box columns is-8"}
            [:div {:class "column card m-4"}
             [:h3 {:class "title is-3"} "Air quality info"]
             (let [pollutans @air-info]
               (for [pollutant pollutans]
                 [:div {:class "m-2"}
                  [:p (str "Pollutant: " (:name pollutant))]
                  [:p (str "Air Quality Index: " (:aqi pollutant) ", " (:level pollutant))]]))]

            [:div {:class "column card m-4"}
             [:h3 {:class "title is-3"} "Pollen info"]
             [:p {:class "m-2"} (str "Pollen index for today is: " (:index @pollen-info) ", " (:level @pollen-info))]
             [:progress {:class "progress is-success" :value (str (:index @pollen-info)) :max "12"} (:index @pollen-info)]
             [:p (str "Main allergens: " (:triggers @pollen-info))]]

            [:div {:class "column card m-4"}
             [:h3 {:class "title is-3"} "Weather info"]
             [:p
              [:img {:src (str "http://openweathermap.org/img/wn/" (:icon @weather-info) "@2x.png")}]]
             [:p {:class "m-2"} (str (:description @weather-info))]
             [:p {:class "m-2"} (str "Temperature: " (:temperature @weather-info) "°F")]
             [:p {:class "m-2"} (str "Humidity: ") (:humidity @weather-info) "%"]
             [:p {:class "m-2"} (str "Wind speed: ") (:wind-speed @weather-info) "Mph"]
             [:p {:class "m-2"} (str "Wind direction: ") (:wind-dir @weather-info)]]]]]
          [:footer {:class "footer"}
           [:div {:class "content has-text-centered"}
            [:p [:strong "AllerGenie"] " by " [:a {:href "https://ostash.dev"} "© Roman Ostash 2020"]]]]))

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