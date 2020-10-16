(ns allergenie.components.nav)

(defn nav-bar 
  []
  [:nav {:class "level is-mobile"}
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/"} "Home"]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/journal"} "Journal"]]
    [:figure {:class "image is-128x128 has-text-centered"}
    [:img {:src "/img/genie.svg" :alt "AllegGenie logo"}]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/glossary"} "Glossary"]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/about"} "About"]]])