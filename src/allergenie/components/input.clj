(ns allergenie.components.input)
(defn input
  []
  [:div {:class "columns is-centered m-4"}
   [:div {:class "column is-half"}
    [:form {:action "/info"}
     [:div {:class "field"}
      [:label {:for "zip" :class "ml-5"} "Provide us your location in the US"]
      [:div {:class "control m-4"}
       [:input {:class "input m-2" :name "zip" :type "tel" :minlength "5" :maxlength "5" :id "zip" :pattern #"^[0-9]$" :placeholder "Enter your zip code. 5 digits only" :required ""}]
       [:input {:class "button is-info m-2" :type "submit" :value "Submit"}]]]]]])