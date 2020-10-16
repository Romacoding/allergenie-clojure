(ns allergenie.components.pollen)

(defn pollen
  [state]
  [:div {:class "column card m-4"}
   [:h3 {:class "title is-3 has-text-centered"} "Pollen"]
   [:p {:class "m-2"} (str "Pollen index for today is: " (:index state) ", " (:level state))]
   [:progress {:class "progress is-success" :value (str (:index state)) :max "12"} (:index state)]
   [:p (str "Main allergens: " (:triggers state))]])