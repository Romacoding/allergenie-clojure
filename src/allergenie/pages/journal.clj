(ns allergenie.pages.journal
  (:require [hiccup.page :refer [html5]]
            [allergenie.components.head :as h]
            [allergenie.components.nav :as n]))

(defn journal-page
  [_]
  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (n/nav-bar)
           [:h2 {:class "title is-3 has-text-centered m-6"} "Your allergy journal"]
           [:h3 {:class "subtitle has-text-centered m-6"}  "Symptoms you are experiencing. Check all that apply."]
           [:div {:class "box m-4"} (let [pollutans ["Coughing" "Wheezing" "Shortness of Breath" "Fatigue" "Sore Throat" "Nasal Congestion" "Sneezing" "Itchy Eyes" "Runny Nose" "Red Eyes" "Headache" "Watery Eyes"]]
                                  (for [pollutant pollutans]
                                    [:div {:class "m-2"}
                                     [:label {:class "checkbox"}
                                      [:input {:type "checkbox" :class "m-2"} pollutant]]]))]]]))
