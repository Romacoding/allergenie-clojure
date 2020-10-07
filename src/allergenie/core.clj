(ns allergenie.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [rum.core :refer [defc render-static-markup]]
            [config.core :refer [env]]
            [clojure.data.json :as json]
            [clojure.pprint :refer [pprint]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [allergenie.util :refer [get-pollen calc-wind-dir calc-pollen-level get-air get-weather]])
  (:gen-class))

(def pollen-info (atom {}))
(def zip-info (atom {:zip "11223"}))
(def air-info (atom []))
(def weather-info (atom {}))

(reset! air-info (get-air (:zip @zip-info)))

(swap! weather-info merge (get-weather (:zip @zip-info)))

(pprint (get-air (:zip @zip-info)))

(swap! pollen-info merge (get-pollen (:zip @zip-info)))

(swap! pollen-info assoc :level (calc-pollen-level (:index @pollen-info)))
(swap! weather-info assoc :wind-dir (calc-wind-dir (num (:wind-deg @weather-info))))

(defc main-page []
  [:body {:style {:background-color "#fef9c7"}}
   [:div {:class "main-page-div"}
    [:h1 {:style {:text-align "center"}} "AllerGenie"]
    [:div
     [:a {:href "/" :style {:padding "10px"}} "Home page"]
     [:a {:href "/json" :style {:padding "10px"}} "JSON response"]
     [:a {:href "/about" :style {:padding "10px"}} "About page"]]
    [:h3 {:style {:text-align "center"}} "Request form"
     [:form {:style {:padding "5px"} :action "/pollen"}
      [:label {:style {:padding "5px"} :for "zip"} "Change your location"] [:br]
      [:input {:name "zip" :type "text" :id "zip" :placeholder "enter your zip code" :required ""}] [:br]
      [:input {:style {:padding "5px"} :type "submit" :value "Submit"}]]]
    [:h3 (str "Information for: " (:location @pollen-info) " " (:zip @zip-info))]
    [:h3 "Air quality info"]
    [:p (str "Pollutant: " (:name (first @air-info)))]
    [:p (str "Air Quality Index: " (:aqi (first @air-info)) ", " (:level (first @air-info)))]
    [:p (str "Pollutant: " (:name (nth @air-info 1)))]
    [:p (str "Air Quality Index: " (:aqi (nth @air-info 1)) ", " (:level (nth @air-info 1)))]
    [:h3 "Pollen info"]
    [:p (str "Pollen index for today is: " (:index @pollen-info) ", " (:level @pollen-info))]
    [:p (str "Main triggers: " (:triggers @pollen-info))]
    [:h3 "Weather info"]
    [:p
     [:img {:src (str "http://openweathermap.org/img/wn/" (:icon @weather-info) "@2x.png")}]]
    [:p (str (:description @weather-info) ". " "Temperature: " (:temperature @weather-info) "°F")]
    [:p (str "Humidity: ") (:humidity @weather-info) "%"]
    [:p (str "Wind speed: ") (:wind-speed @weather-info) "Mph"]
    [:p (str "Wind direction: ") (:wind-dir @weather-info)]
    ]]
  )

(defc about-page []
  [:div {:class "about-page-div"}
   [:h1 "About the project"]
   [:p "AllerGenie provides pollen and weather forecasts you can use everyday.\n
Our mission is to improve the quality of life through timely and accurate information intended to assist all allergy sufferers."]
   [:a {:href "/"} "Home page"]])

(defn return-json [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @pollen-info))})

(defn pollen-page [req]
  (swap! zip-info assoc :zip (:zip (:params req)))
  (swap! pollen-info merge (get-pollen (:zip @zip-info)))
  (get-air (:zip @zip-info))
  (get-weather (:zip @zip-info))
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (str "Zip updated to " (:zip @zip-info) "<br/><a href='/'>Home page</>" )})

(defroutes app
  (GET "/" [] (render-static-markup (main-page)))
  (GET "/about" [] (render-static-markup (about-page)))
  (GET "/pollen" [] pollen-page)
  (GET "/json" [] return-json)
  (not-found "<h1>Sorry, page not found!</h1>"))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") (:port env)))]
    (run-jetty (wrap-defaults app site-defaults) {:port port})))