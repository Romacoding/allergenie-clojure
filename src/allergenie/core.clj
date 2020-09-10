(ns allergenie.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [rum.core :refer [defc render-static-markup]]
            [config.core :refer [env]]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.pprint]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session-timeout :refer [wrap-idle-session-timeout]]
            [clojure.string])
  (:gen-class))

(def pollen-info (atom []))
(def air-info (atom []))

;; (defn adddata [keyfield valuefield]
;;   (swap! pollen-info conj {:keyfield keyfield :valuefield valuefield}))
;; (adddata "zip" "08865")

(defn return-json [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (str (json/write-str @pollen-info))})

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
  (GET "/" [] "<h1>Test header</h1>")
  (GET "/about" [] "<h1>About me</h1>")
  (GET "/request" [] request-example)
  (GET "/page" [] simple-page)
  (GET "/pollen" [] pollen-page)
  (GET "/json" [] return-json)
  (route/not-found "Error, page not found!"))

; (defmacro fmt [^String string]
;   (let [-re #"#\{(.*?)\}"
;         fstr (clojure.string/replace string -re "%s")
;         fargs (map #(read-string (second %)) (re-seq -re string))]
;     `(format ~fstr ~@fargs)))


(defn get-zip []
  (let [resp (client/get "http://ip-api.com/json/?fields=zip")
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    ;(println resp)
    (swap! air-info conj {:zip (:zip body)}))
  (println @air-info))

(println (get-zip))

(defn get-air []
  (let [resp (client/get (str "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/json&zipCode=" (:zip (first @air-info)) "&API_KEY=" (:airkey env)))
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    ; (reduce (fn [acc {:keys [Name] :as it}]
    ;           (assoc acc Name it)) {} (:data body)) 
    ; (clojure.pprint/pprint body)
    ; (println (first body))
    (swap! air-info conj {:AQI (map :AQI body) :ParameterName (map :ParameterName body)})))

(println (get-air))

;; (format "http://www.airnowapi.org/aq/observation/zipCode/current/?format=application/json&zipCode=%s%s%s" zip-code small api-key))

(defn get-weather []
  (let [resp (client/get (str "http://api.openweathermap.org/data/2.5/weather?zip=" (:zip (first @air-info)) ",us&appid=" (:weatherkey env)))
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    ; (reduce (fn [acc {:keys [Name] :as it}]
    ;           (assoc acc Name it)) {} (:data body)) 
    ; (clojure.pprint/pprint body)
    (println "Weather information")
    (println (int (Math/floor (- (* 1.8 (:temp (:main body))) 459.67))))
    (println (clojure.string/capitalize (:description (first
                                                       (:weather body)))))))


(get-weather)


(defn -main
  [& args]
  (println (str "Running webserver at http:/127.0.0.1:" (:port env)  "/"))
  (run-jetty (wrap-defaults app site-defaults) {:port (:port env)}))