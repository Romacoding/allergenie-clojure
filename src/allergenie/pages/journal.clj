(ns allergenie.pages.journal
  (:require [hiccup.page :refer [html5]]
            [markdown.core :as md]
            [hiccup.form :as form]
            [allergenie.components.head :as h]
            [allergenie.components.nav :as n]
            [allergenie.db :as db]))

;(def preview-len 270)
;
;(defn- cut-body [body]
;  (if (> (.length body) preview-len)
;    (subs body 0 preview-len)
;    body))

(defn journal-page
  [_]
  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (n/nav-bar)
           [:h2 {:class "title is-3 has-text-centered m-6"} "Your allergy journal"]
           [:h3 {:class "subtitle has-text-centered m-6"} "Symptoms you are experiencing. Check all that apply."]
           [:div {:class "box m-4"} (let [symptoms ["Coughing" "Wheezing" "Shortness of Breath" "Fatigue" "Sore Throat" "Nasal Congestion" "Sneezing" "Itchy Eyes" "Runny Nose" "Red Eyes" "Headache" "Watery Eyes"]]
                                      (for [symptom symptoms]
                                        [:div {:class "m-2"}
                                         [:label {:class "checkbox"}
                                          [:input {:type "checkbox" :class "m-2"} symptom]]]))]
           [:div {:class "box m-4"}
            [:a {:href "/records/new" :class "button is-success m-2"} "New record"]
            ;(->> (db/list-records)
            ;     (map #(str "<h3>" (:title %) "</h3>")))
            (for [a (db/list-records)]
              [:div {:class "m-2"}
               [:h2 [:a {:class "button is-link" :href (str "/records/" (:_id a))} (:title a)]]
               [:p (-> a :body md/md-to-html-string)]])]]]))
