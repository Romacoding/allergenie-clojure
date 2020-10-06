(ns allergenie.pollen
  (:require
   [clj-http.client :as client]
   [clojure.data.json :as json]))

(defn get-pollen [zip]
  (let [resp (client/get (str "https://www.pollen.com/api/forecast/current/pollen/" zip) {:headers {:User-Agent (str "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36"), :Referer (str "https://www.pollen.com/api/forecast/current/pollen/08865")}})
        body (json/read-str (resp :body)
                            :key-fn keyword)]
    (swap! pollen-info assoc :index (:Index (nth (:periods (:Location body)) 1)))
    (swap! pollen-info assoc :triggers (first (:Triggers (nth (:periods (:Location body)) 1))))
    (println "Pollen information")
    (println (str "Current polen index: " (:Index (nth (:periods (:Location body)) 1))))))