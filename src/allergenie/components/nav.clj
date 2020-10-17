(ns allergenie.components.nav)

(defn nav-bar
  []
  [:nav {:class "level"}
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/"} "Home"]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/journal"} "Journal"]]
   [:p {:class "level-item has-text-centered"}
     [:img {:src "/img/genie.svg" :alt "AllegGenie logo" :style "height: 8rem"}]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/glossary"} "Glossary"]]
   [:p {:class "level-item has-text-centered"}
    [:a {:class "link is-info" :href "/about"} "About"]]])
