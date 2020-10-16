(ns allergenie.components.about
  (:require [hiccup.page :refer :all]
            [hiccup.core :refer :all]
            [allergenie.components.nav :refer [nav-bar]]))

(defn about-page
  [_]
  (html5 {:lang "en"}
         [:head [:title "AllerGenie"]
          (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
          [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]]
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (nav-bar)
           [:h3 {:class "title is-3 has-text-centered m-6"} "About the project"]
           [:p "AllerGenie provides daily air quality, pollen and weather forecasts.\n 
                Our mission is to improve the quality of life through timely and accurate information intended to assist all allergy sufferers."]]]))
