(ns allergenie.components.pollen)

(defn pollen
  [state]
  [:div {:class "column card m-4"}
   [:h3 {:class "title is-3 has-text-centered"} "Pollen"]
   [:p {:class "m-3"} (str "Pollen index for today is: " (:index state) ", " (:level state))]
   [:div {:class "m-5"}
    [:progress {:class (str "progress " (:level-color state)) :value (:index state) :max "12"} (:index state)]
    [:p (str "Main allergens: " (:triggers state))]]])
