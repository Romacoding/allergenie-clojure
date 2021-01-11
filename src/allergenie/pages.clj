(ns allergenie.pages
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [markdown.core :as md]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [allergenie.components :as components]
            [allergenie.db :as db]))

(def pollen-levels-info
  ["[0.0 - 2.4] Very few individuals among the allergy-suffering public are affected."
   "[2.5 - 4.8] Individuals who are extremely sensitive to the predominant pollen may start to be affected."
   "[4.9 - 7.2] These pollen levels will likely cause symptoms for many allergy-sufferers of the seasonal predominant pollen types."
   "[7.3 - 9.6] A large number of people who suffer from the seasonal pollen types may be affected."
   "[9.7 - 12.0] Most individuals who are affected by the seasonal pollen types may suffer symptoms which are more severe on days with high pollen levels."])

(def air-quality-info
  ["[0 - 50] Good. Air quality is satisfactory, and air pollution poses little or no risk."
   "[51 - 100] Air quality is acceptable. However, there may be a risk for some people, particularly those who are unusually sensitive to air pollution."
   "[101 - 150] Members of sensitive groups may experience health effects. The general public is less likely to be affected."
   "[151 - 200] Some members of the general public may experience health effects; members of sensitive groups may experience more serious health effects."
   "[201 - 300] Health alert: The risk of health effects is increased for everyone."
   "[301 - higher] Health warning of emergency conditions: everyone is more likely to be affected."])

(def air-pollutants-info
  ["[ PM2.5 ] PM2.5 are tiny particles that are invisible to the naked eye. Their diameter is smaller than 2.5 microns - about 24 times thinner than a human hair! They include dust, dirt, soot, smoke, car exhaust and industrial pollution. These tiny particles can reach deep into your lungs and can cause inflammation in your lower airways."
   "[ PM10 ] Coarse particulate matter (PM10) is a mixture of solid and liquid particles. They include dust, dirt, soot, smoke, car exhaust and industrial pollution. PM10 is larger than its finer relative PM2.5 but still inhalable. These particles can enter the upper airways such as your nose and throat and cause irritation."
   "[ 03 ] Ozone (03) is pretty useful in the upper atmosphere but unhealthy at ground levels. Ozone at ground level is generated through complex photochemical reactions with chemicals from emissions and it's the main ingredient of smog. It is one of the pollutants that directly impacts your respiratory system. It can increase your vulnerability to infections and intensify breathing problems."
   "[ CO ] Carbon monoxide (CO) is an odorless and colorless gas produced when fuels such as gas, oil, coal and wood do not burn fully. Car exhaust emissions are also a primary source of CO."
   "[ NO2 ] Nitrogen dioxide (NO2) is a red-brown gas with a pungent, acrid smell. It mainly comes from emissions associated with for example transportation, industry and coal-fired power plants. Raised levels may increase the likelihood of respiratory problems."
   "[ SO2 ] Sulfur dioxide (SO2) is a colorless gas with a sharp and irritating smell. Primarily, it comes from industrial activity that processes materials containing sulfur, for example the generation of electricity from coal, oil or gas. It irritates the nose, throat and airways when breathed in, causing symptoms such as coughing and wheezing."])

(defn about-page
  [_]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (components/nav-bar)
           [:h3 {:class "title is-3 has-text-centered m-6"} "About the project"]
           [:div {:class "m-4"}
            [:p "AllerGenie provides daily air quality, pollen and weather forecasts.\n
                Our mission is to improve the quality of life through timely and accurate information intended to assist all allergy sufferers."]]]]))

(defn glossary-page
  [_]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (components/nav-bar)
           [:h3 {:class "title is-3 has-text-centered m-6"} "Glossary and terms"]
           [:div {:class "m-6"}
            [:p {:class "title m-4"} "Pollen Index Levels"]
            (for [level pollen-levels-info]
              [:p {:class "m-2"} level])]
           [:div {:class "m-6"}
            [:p {:class "title m-4"} "Air Quality Index"]
            (for [quality air-quality-info]
              [:p {:class "m-2"} quality])]
           [:div {:class "m-6"}
            [:p {:class "title m-4"} "Air Pollutants"]
            (for [pollutants air-pollutants-info]
              [:p {:class "m-2"} pollutants])]]]))

(defn journal-page
  [_]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (components/nav-bar)
           [:h2 {:class "title is-3 has-text-centered m-6"} "Your allergy journal"]
           [:h3 {:class "subtitle has-text-centered m-6"} "Symptoms you are experiencing. Check all that apply."]
           [:div {:class "box m-4"} (let [symptoms ["Coughing" "Wheezing" "Shortness of Breath" "Fatigue" "Sore Throat" "Nasal Congestion" "Sneezing" "Itchy Eyes" "Runny Nose" "Red Eyes" "Headache" "Watery Eyes"]]
                                      (for [symptom symptoms]
                                        [:div {:class "m-2"}
                                         [:label {:class "checkbox"}
                                          [:input {:type "checkbox" :class "m-2"} symptom]]]))]
           [:div {:class "box m-4"}
            [:a {:href "/records/new" :class "button is-success m-2"} "New record"]
            [:a {:href "/admin/logout" :class "button is-warning m-2"} "Log out"]
            ;(->> (db/list-records)
            ;     (map #(str "<h3>" (:title %) "</h3>")))
            (for [a (db/list-records)]
              [:div {:class "m-2"}
               [:h2 [:a {:class "button is-link" :href (str "/records/" (:_id a))} (:title a)]]
               [:p (-> a :body md/md-to-html-string)]
               [:p (str "Location: " (:location a))]
               [:p (str "Weather: " (:weather a))]
               [:p (str "Air Quality Index: " (:air-index a))]
               [:p (str "Pollen index: " (:pollen-index a))]
               [:p (str "Triggers: " (:triggers a))]])]]]))

(defn record
  [a]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (components/nav-bar)
           [:div {:class "m-4"}
            (form/form-to
             [:delete (str "/records/" (:_id a))]
             (anti-forgery-field)
             [:div {:class "field is-grouped"}
              [:p {:class "control"} [:a {:class "button is-primary" :href (str "/records/" (:_id a) "/edit")} "Edit"]]
              [:p {:class "control"} (form/submit-button {:class "button is-danger"} "Delete")]])
            [:small (:created a)]
            [:h3 {:class "title"} (:title a)]
            [:p (-> a :body md/md-to-html-string)]]]]))

(defn edit-record [a]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (components/nav-bar)
           [:div {:class "m-4"}
            (form/form-to
             [:post (if a (str "/records/" (:_id a)) "/records")]
             [:div {:class "field"}
              (form/label {:class "label"} "title" "Title")
              (form/text-field {:class "input is-small" :required "" :placeholder "Record title"} "title" (:title a))]
             [:div {:class "field"}
              (form/label {:class "label"} "body" "Body")
              (form/text-area {:class "textarea is-medium" :required "" :placeholder "Record your symptoms here"} "body" (:body a))]
             (anti-forgery-field)
             (form/submit-button {:class "button is-info"} "Save"))]]]))

(defn login-page [& [msg]]
  (html5 {:lang "en"}
         (components/head)
         [:body
          [:section {:class "hero is-fullheight"}
           [:div {:class "hero-body"}
            [:div {:class "container has-text-centered"}
             [:h1 {:class "title is-1"} "AllerGenie"]
             (components/nav-bar)
             [:div {:class "column is-4 is-offset-4"}
              [:h3 {:class "title is-3 has-text-centered m-6"} "Journal"]
              [:hr {:class "login-hr"}]
              [:p {:class "subtitle has-text-black"} "Please login to proceed"]
              [:div {:class "box"}
               (form/form-to
                [:post "/admin/login"]
                [:div {:class "field"}
                 [:div {:class "control"}
                  (form/label {:class "label is-medium"} "login" "Login")
                  (form/text-field {:class "input is-normal" :required "" :placeholder "Your Login"} "login")]]
                [:div {:class "field"}
                 [:div {:class "control"}
                  (form/label {:class "label is-medium"} "password" "Password")
                  (form/password-field {:class "input is-normal" :required "" :placeholder "Your Password"} "password")]]
                (anti-forgery-field)
                (when msg
                  [:p {:class "has-text-danger m-5"} msg])
                [:p {:class "m-5"} ""]
                (form/submit-button {:class "button is-block is-info is-normal is-fullwidth"} "Login"))]
              [:p {:class "has-text-grey"}
               [:a {:href "mailto:roman@ostash.dev"} "Need Help?"]]]]]]]))
