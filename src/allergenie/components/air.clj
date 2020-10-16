(ns allergenie.components.air)

(defn air
  [state]
  [:div {:class "column card m-4"}
   [:h3 {:class "title is-3 has-text-centered"} "Air Quality"]
   [:p {:class "is-small m-2"} "The air you breathe matters. Besides pollen, there's all sorts of other stuff in the air that can have an effect on your breathing and allergy symptoms."]
   [:div ]
   (let [pollutans state]
     (for [pollutant pollutans]
       [:div {:class "m-2"}
        [:p (str "Pollutant: " (:name pollutant))]
        [:p (str "Air Quality Index: " (:aqi pollutant) ", " (:level pollutant))]]))])
