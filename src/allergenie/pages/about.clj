(ns allergenie.pages.about
  (:require [hiccup.page :refer [html5]]
            [allergenie.components.head :as h]
            [allergenie.components.nav :as n]))

(defn about-page
  [_]
  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (n/nav-bar)
           [:h3 {:class "title is-3 has-text-centered m-6"} "About the project"]
           [:div {:class "m-4"}
            [:p "AllerGenie provides daily air quality, pollen and weather forecasts.\n 
                Our mission is to improve the quality of life through timely and accurate information intended to assist all allergy sufferers."]]]]))
