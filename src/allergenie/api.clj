(ns allergenie.api
  (:require [clojure.data.json :as json]
            [clj-http.client :as client]
            [config.core :refer [env]]
            [clojure.string :as str]
            [allergenie.util :as util]))

(defn get-pollen 
  "Function to call API endpoint. Returns a response map with pollen info"
  [zip]
  (let [resp (client/get (str "https://www.pollen.com/api/forecast/current/pollen/" zip)
                         {:headers
                          {:User-Agent (str "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")
                           :Referer (str "https://www.pollen.com/api/forecast/current/pollen/" zip)}})
        body (json/read-str (resp :body)
                            :key-fn keyword)
        index (get-in body [:Location :periods 1 :Index])
        triggers (get-in body [:Location :periods 1 :Triggers])]

    (assoc {} :location (get-in body [:Location :DisplayLocation]) :triggers (if (empty? triggers) [{:Name "None. Enjoy outdoor activities!"}] triggers) :index (get-in body [:Location :periods 1 :Index]) :level (util/calc-pollen-level index) :level-color (util/calc-pollen-color index))))

(defn get-air 
  "Function to call API endpoint. Returns a response vector with air quality info"
  [zip]
  (let [airkey (or (System/getenv "AIRKEY") (:airkey env))
        resp (client/get (str "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/json&zipCode=" zip "&API_KEY=" airkey))
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    (reduce (fn [acc it]
              (let [{:keys [AQI ParameterName]} it
                    level (get-in it [:Category :Name])]
                (conj acc {:aqi AQI :name ParameterName :level level}))) [] body)))

(defn get-weather 
  "Function to call API endpoint. Returns a response map with weather info"
  [zip]
  (let [weatherkey (or (System/getenv "WEATHERKEY") (:weatherkey env))
        resp (client/get (str "http://api.openweathermap.org/data/2.5/weather?zip=" zip ",us&appid=" weatherkey))
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    (assoc {} :description (str/capitalize (:description (first
                                                          (:weather body))))
           :temperature (int (Math/floor (- (* 1.8 (:temp (:main body))) 459.67)))
           :humidity (:humidity (:main body))
           :icon (:icon (first (:weather body)))
           :wind-speed (int (Math/floor (* 2.236937 (:speed (:wind body)))))
           :wind-deg (:deg (:wind body))
           :wind-dir (util/calc-wind-dir (:deg (:wind body))))))
