(ns allergenie.components.footer)
(defn footer
  []
  [:footer {:class "footer"}
   [:div {:class "content has-text-centered"}
    [:p [:strong "AllerGenie"] " by " [:a {:href "https://ostash.dev"} "© Roman Ostash 2020"]]]])
