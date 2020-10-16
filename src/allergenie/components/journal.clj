(ns allergenie.components.journal
  (:require [hiccup.page :refer :all]
            [hiccup.core :refer :all]
            [allergenie.components.nav :refer [nav-bar]]))

(defn journal-page
  [_]
  (html5 {:lang "en"}
         [:head [:title "AllerGenie"]
          (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (nav-bar)
           [:h2 {:class "title is-3 has-text-centered m-6"} "Your allergy journal"]
           [:h3 {:class "subtitle has-text-centered m-6"}  "Symptoms you are experiencing. Check all that apply."]
           [:div {:class "box m-4"} (let [pollutans ["Coughing" "Wheezing" "Shortness of Breath" "Fatigue" "Sore Throat" "Nasal Congestion" "Sneezing" "Itchy Eyes" "Runny Nose" "Red Eyes" "Headache" "Watery Eyes"]]
                                  (for [pollutant pollutans]
                                    [:div {:class "m-2"}
                                     [:label {:class "checkbox"}
                                      [:input {:type "checkbox" :class "m-2"} pollutant]]]))]]]))
