(ns allergenie.components.input)
(defn input
  []
  [:div {:class "columns is-centered m-4"}
   [:div {:class "column is-half"}
    [:form {:action "/info"}
     [:div {:class "field"}
      [:label {:for "zip" :class "ml-5"} "Please, provide us your location in the US"]
      [:div {:class "control m-4"}
       [:input {:class "input m-2" :name "zip" :type "tel" :id "zip" :placeholder "enter your zip code" :required ""}]
       [:input {:class "button is-info m-2" :type "submit" :value "Submit"}]]]]]])
