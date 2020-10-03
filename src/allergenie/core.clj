(ns allergenie.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [rum.core :refer [defc render-static-markup]]
            [config.core :refer [env]]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.pprint]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]]
            [clojure.string :as str])
  (:gen-class))

(def pollen-info (atom {}))
(def zip-info (atom []))
(def air-info (atom []))
(def weather-info(atom {}))
(def input-info(atom 0))

(defc main-page []
  [:div {:class "main-page-div"}
   [:h1 {:style {:text-align "center"}} "AllerGenie"]
   [:h3 (str "Your zip code is: " (:zip (first @zip-info)))]
   [:input {:type      "text"
            :allow-full-screen true
            :id        "comment"
            :class     ["input_active"]
            :style     {:background-color "#EEE"
                        :margin-left      42}
            :on-change (fn [e]
                         (swap! input-info assoc (.. e -target -value))
                         (println (.. e -target -value))
                         (println @input-info))}]
   [:p (str "Air quality info: " (:zip (first @air-info)))]
   [:p (str "Pollen index for today is: " (:Index @pollen-info))]
   [:p (str "Weather info: " (:description @weather-info) ". " "Temperature: " (:temperature @weather-info) "°F")]])

(defc about-page []
  [:div {:class "about-page-div"}
   [:h1 "About the project"]
   [:p "AllerGenie provides pollen and weather forecasts you can use everyday.\n
Our mission is to improve the quality of life through timely and accurate information intended to assist all allergy sufferers."]])

;; (defn adddata [keyfield valuefield]
;;   (swap! pollen-info conj {:keyfield keyfield :valuefield valuefield}))
;; (adddata "zip" "08865")

(defn return-json [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @air-info))})

(defn request-example [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->>
             (clojure.pprint/pprint req)
             (str "Request Object: " req))})

(defn simple-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

(defn pollen-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->
             (clojure.pprint/pprint req)
             (str "Pollen " (:name (:params req))))})

(defroutes app
  (GET "/" [] (render-static-markup (main-page)))
  (GET "/about" [] (render-static-markup (about-page)))
  (GET "/request" [] request-example)
  (GET "/page" [] simple-page)
  (GET "/pollen" [] pollen-page)
  (GET "/json" [] return-json)
  (not-found "<h1>Sorry, page not found!</h1>"))

; (defmacro fmt [^String string]
;   (let [-re #"#\{(.*?)\}"
;         fstr (str/replace string -re "%s")
;         fargs (map #(read-string (second %)) (re-seq -re string))]
;     `(format ~fstr ~@fargs)))


(defn get-zip []
  (let [resp (client/get "http://ip-api.com/json/?fields=zip")
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    (swap! zip-info conj {:zip (:zip body)}))
  (println (str "Your zip code is: "(:zip (first @zip-info))))
  )
(get-zip)

(defn get-air []
  (let [resp (client/get (str "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/json&zipCode=" (:zip (first @zip-info)) "&API_KEY=" (:airkey env)))
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    ; (reduce (fn [acc {:keys [Name] :as it}]
    ;           (assoc acc Name it)) {} (:data body)) 
    ;(clojure.pprint/pprint body)
    (println "Air pollution information")
    ;(println (str (:ParameterName (nth body 1)) " index is: " (:AQI (nth body 1)) ", " (:Name (:Category (nth body 1)))))
    (println (str (:ParameterName (first body)) " index is: " (:AQI (first body)) ", " (:Name (:Category (first body)))))
    (swap! air-info conj {:AQI (:AQI (first body)) :Name (:ParameterName (first body))})))

(get-air)

;; (format "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/json&zipCode=%s%s%s" zip-code small api-key))

(defn get-weather []
  (let [resp (client/get (str "http://api.openweathermap.org/data/2.5/weather?zip=" (:zip (first @zip-info)) ",us&appid=" (:weatherkey env)))
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    ; (reduce (fn [acc {:keys [Name] :as it}]
    ;           (assoc acc Name it)) {} (:data body)) 
    ; (clojure.pprint/pprint body)
    (swap! weather-info assoc :description (str/capitalize (:description (first
                                                                               (:weather body))))
           :temperature (int (Math/floor (- (* 1.8 (:temp (:main body))) 459.67))))
    (println "Weather information")
    (println (str/capitalize (:description (first
                                                       (:weather body)))))
    (println (str "Temperature: " (int (Math/floor (- (* 1.8 (:temp (:main body))) 459.67))) " F"))))

(get-weather)

(defn get-pollen []
  (let [resp (client/get (str "https://www.pollen.com/api/forecast/current/pollen/" (:zip (first @zip-info))) {:headers {:User-Agent (str "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"), :Referer (str "https://www.pollen.com/api/forecast/current/pollen/08865")}})
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    (swap! pollen-info assoc :Index (:Index (nth (:periods (:Location body)) 1)))
    (println "Pollen information")
    ;(println @pollen-info)
    (println (str "Current polen index: " (:Index (nth (:periods (:Location body)) 1))))))

(get-pollen)

;; (client/get
;;  "http://yoursite.com/some-url"
;;  {:query-params {"zip" ["08865"]}
;;   :debug true})

(defn -main
  [& args]
  (println (str "Running webserver at http:/127.0.0.1:" (:port env)  "/"))
  (run-jetty (wrap-defaults app site-defaults) {:port (:port env)}))