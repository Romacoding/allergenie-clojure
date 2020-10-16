(ns allergenie.components.input)
(defn input
  []
  [:div {:class "box"}
   [:form {:action "/info"}
    [:label {:for "zip"} "Change your location"] [:br]
    [:input {:class "input m-4" :name "zip" :type "tel" :id "zip" :placeholder "enter your zip code" :required ""}]
    [:input {:class "button is-info m-4" :type "submit" :value "Submit"}]]])
