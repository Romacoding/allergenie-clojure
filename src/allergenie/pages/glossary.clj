(ns allergenie.pages.glossary
  (:require [hiccup.page :refer [html5]]
            [allergenie.components.head :as h]
            [allergenie.components.nav :as n]))

(defn glossary-page
  [_]
  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (n/nav-bar)
           [:h3 {:class "title is-3 has-text-centered m-6"} "Glossary and terms"]
           [:div {:class "box m-4"}
            [:p {:class "title m-4"} "Pollen Index Levels"]
             [:p {:class "m-2"} "[0.0 - 2.4] Very few individuals among the allergy-suffering public are affected."]
             [:p {:class "m-2"} "[2.5 - 4.8] Individuals who are extremely sensitive to the predominant pollen may start to be affected."]
             [:p {:class "m-2"} "[4.9 - 7.2] These pollen levels will likely cause symptoms for many allergy-sufferers of the seasonal predominant pollen types."]
             [:p {:class "m-2"} "[7.3 - 9.6] A large number of people who suffer from the seasonal pollen types may be affected."]
             [:p {:class "m-2"} "[9.7 - 12.0] Most individuals who are affected by the seasonal pollen types may suffer symptoms which are more severe on days with high pollen levels."]]

           [:div {:class "box m-4"}
            [:p {:class "title m-4"} "Air Quality Index"]
            [:p {:class "m-2"} "[0 - 50] Good. Air quality is satisfactory, and air pollution poses little or no risk."]
            [:p {:class "m-2"} "[51 - 100] Air quality is acceptable. However, there may be a risk for some people, particularly those who are unusually sensitive to air pollution."]
            [:p {:class "m-2"} "[101 - 150] Members of sensitive groups may experience health effects. The general public is less likely to be affected."]
            [:p {:class "m-2"} "[151 - 200] Some members of the general public may experience health effects; members of sensitive groups may experience more serious health effects."]
            [:p {:class "m-2"} "[201 - 300] Health alert: The risk of health effects is increased for everyone."]
            [:p {:class "m-2"} "[301 - higher] Health warning of emergency conditions: everyone is more likely to be affected."]]

           [:div {:class "box m-4"}
            [:p {:class "title m-4"} "Air Pollutants"]
            [:p {:class "m-2"} "[ PM2.5 ] PM2.5 are tiny particles that are invisible to the naked eye. Their diameter is smaller than 2.5 microns - about 24 times thinner than a human hair! They include dust, dirt, soot, smoke, car exhaust and industrial pollution. These tiny particles can reach deep into your lungs and can cause inflammation in your lower airways."]
            [:p {:class "m-2"} "[ PM10 ] Coarse particulate matter (PM10) is a mixture of solid and liquid particles. They include dust, dirt, soot, smoke, car exhaust and industrial pollution. PM10 is larger than its finer relative PM2.5 but still inhalable. These particles can enter the upper airways such as your nose and throat and cause irritation."]
            [:p {:class "m-2"} "[ 03 ] Ozone (03) is pretty useful in the upper atmosphere but unhealthy at ground levels. Ozone at ground level is generated through complex photochemical reactions with chemicals from emissions and it's the main ingredient of smog. It is one of the pollutants that directly impacts your respiratory system. It can increase your vulnerability to infections and intensify breathing problems."]
            [:p {:class "m-2"} "[ CO ] Carbon monoxide (CO) is an odorless and colorless gas produced when fuels such as gas, oil, coal and wood do not burn fully. Car exhaust emissions are also a primary source of CO."]
            [:p {:class "m-2"} "[ NO2 ] Nitrogen dioxide (NO2) is a red-brown gas with a pungent, acrid smell. It mainly comes from emissions associated with for example transportation, industry and coal-fired power plants. Raised levels may increase the likelihood of respiratory problems."]
            [:p {:class "m-2"} "[ SO2 ] Sulfur dioxide (SO2) is a colorless gas with a sharp and irritating smell. Primarily, it comes from industrial activity that processes materials containing sulfur, for example the generation of electricity from coal, oil or gas. It irritates the nose, throat and airways when breathed in, causing symptoms such as coughing and wheezing."]]]]))
