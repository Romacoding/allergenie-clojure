(ns allergenie.components
  (:require [allergenie.state :as state]
            [hiccup.page :refer [include-css]]))

(defn head
  []
  [:head [:title "AllerGenie"]
   (include-css "https://cdn.jsdelivr.net/npm/bulma@0.9.1/css/bulma.min.css")
   [:link {:rel "apple-touch-icon" :sizes "57x57" :href "/favicon/apple-icon-57x57.png"}]
   [:link {:rel "apple-touch-icon" :sizes "60x60" :href "/favicon/apple-icon-60x60.png"}]
   [:link {:rel "apple-touch-icon" :sizes "72x72" :href "/favicon/apple-icon-72x72.png"}]
   [:link {:rel "apple-touch-icon" :sizes "76x76" :href "/favicon/apple-icon-76x76.png"}]
   [:link {:rel "apple-touch-icon" :sizes "114x114" :href "/favicon/apple-icon-114x114.png"}]
   [:link {:rel "apple-touch-icon" :sizes "120x120" :href "/favicon/apple-icon-120x120.png"}]
   [:link {:rel "apple-touch-icon" :sizes "144x144" :href "/favicon/apple-icon-144x144.png"}]
   [:link {:rel "apple-touch-icon" :sizes "152x152" :href "/favicon/apple-icon-152x152.png"}]
   [:link {:rel "apple-touch-icon" :sizes "180x180" :href "/favicon/apple-icon-180x180.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "192x192" :href "/favicon/android-icon-192x192.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "32x32" :href "/favicon/favicon-32x32.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "96x96" :href "/favicon/favicon-96x96.png"}]
   [:link {:rel "icon" :type "image/png" :sizes "16x16" :href "/favicon/favicon-16x16.png"}]
   [:link {:rel "manifest" :href "/favicon/manifest.json"}]
   [:meta {:name "msapplication-TileColor" :content "#ffffff"}]
   [:meta {:name "msapplication-TileImage" :content "/favicon/ms-icon-144x144.png"}]
   [:meta {:name "theme-color" :content "#ffffff"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]])

(defn header
  []
  [:h1 {:class "title is-1 has-text-centered m-4"} "AllerGenie"])

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

(defn input
  []
  [:div {:class "columns is-centered m-4"}
   [:div {:class "column is-half"}
    [:form {:action "/info"}
     [:div {:class "field"}
      [:label {:for "zip" :class "ml-5"} "Provide us your location in the US"]
      [:div {:class "control m-4"}
       [:input {:class "input m-2" :name "zip" :type "tel" :minlength "5" :maxlength "5" :id "zip" :pattern #"^\d*$" :placeholder "Enter your zip code. 5 digits only" :required ""}]
       [:p {:class "help ml-2" :id "status"} ""]
       [:input {:class "button is-info is-normal m-2" :type "submit" :value "Submit"}]]]]]])

(defn air
  []
  [:div {:class "column card m-4"}
   [:h3 {:class "title is-3 has-text-centered"} "Air Quality"]
   [:p {:class "is-small has-text-justified m-2"} "The air you breathe matters. Besides pollen, there's all sorts of other stuff in the air that can have an effect on your breathing and allergy symptoms."]
   [:div {:class "m-4"}
    (let [pollutans @state/air-info]
      (for [pollutant pollutans]
        (let [air-color (cond
                          (> (:aqi pollutant) 200) "is-danger"
                          (> (:aqi pollutant) 100) "is-warning"
                          :else "is-success")]
          [:div {:class "m-3"}
           [:p (str "Pollutant: " (:name pollutant))]
           [:p (str "Air Quality Index: " (:aqi pollutant) ", " (:level pollutant))]
           [:progress {:class (str "progress " air-color) :value (:aqi pollutant) :max "500"} (:aqi pollutant)]])))]])

(defn pollen
  []
  [:div {:class "column card m-4"}
   [:h3 {:class "title is-3 has-text-centered"} "Pollen"]
   [:p {:class "is-small has-text-justified m-2"} "What the pollen levels feel like differs from person to person. Pollen can affect you as soon as you are exposed to it. So tracking the pollen levels every day can help you plan appropriate activities."]
   [:p {:class "m-4"} (str "Pollen index for today is: " (:index @state/pollen-info) ", " (:level @state/pollen-info))]
   [:div {:class "m-5"}
    [:progress {:class (str "progress " (:level-color @state/pollen-info)) :value (:index @state/pollen-info) :max "12"} (:index @state/pollen-info)]
    [:p (str "Main allergens:")]
    (let [triggers (:triggers @state/pollen-info)]
      (for [trigger triggers]
        [:p (:Name trigger)]))]])

(defn weather
  []
  [:div {:class "column card m-4"}
   [:h3 {:class "title is-3 has-text-centered"} "Weather"]
   [:p {:class "is-small has-text-justified m-2"} "Weather conditions can affect how you experience your allergy symptoms. For example, dry, windy weather means that pollen levels go up, which might influence how you feel that day."]
   [:div {:class "m-5"}
    [:p
     [:img {:src (str "/img/icons/" (:icon @state/weather-info) ".svg") :style "height: 10rem" :alt "Weather logo"}]]
    [:p {:class "m-2"} (str (:description @state/weather-info))]
    [:p {:class "m-2"} (str "Temperature: " (:temperature @state/weather-info) "°F")]
    [:p {:class "m-2"} (str "Humidity: ") (:humidity @state/weather-info) "%"]
    [:p {:class "m-2"} (str "Wind speed: ") (:wind-speed @state/weather-info) "Mph"]
    [:p {:class "m-2"} (str "Wind direction: ") (:wind-dir @state/weather-info)]]])

(defn footer
  []
  [:footer {:class "m-6"}
   [:div {:class "content has-text-centered"}
    [:p [:strong "AllerGenie© 2020"]]
    [:p "Developed with &#x1F5A4; by " [:a {:href "https://ostash.dev" :target "_blank"} "Roman Ostash"]]
    [:p "Information is provided by "
     [:a {:href "https://www.pollen.com/" :target "_blank" :rel "noopener noreferrer"} "IQVIA, "]
     [:a {:href "https://openweathermap.org/" :target "_blank" :rel "noopener noreferrer"} "OpenWeather, "]
     [:a {:href "https://www.airnow.gov/" :target "_blank" :rel "noopener noreferrer"} "AirNow"]]
    [:p "Weather icons by " [:a {:href "https://www.amcharts.com/free-animated-svg-weather-icons/" :target "_blank" :rel "noopener noreferrer"} "amCharts"]]]])
