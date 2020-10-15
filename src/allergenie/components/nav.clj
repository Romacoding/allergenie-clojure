(ns allergenie.components.nav
  (:require [hiccup.page :refer :all]
            [hiccup.core :refer :all]))

(defn nav-bar 
  []
  [:nav {:class "level"}
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/"} "Home"]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/journal"} "Journal"]]
   [:p {:class "level-item has-text-centered"}
    [:img {:src "/img/genie.svg" :alt "AllegGenie logo" :style {:size "20px"}}]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/glossary"} "Glossary"]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/about"} "About"]]])