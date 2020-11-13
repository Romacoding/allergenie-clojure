(ns allergenie.pages.record
  (:require [hiccup.page :refer [html5]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [allergenie.components.head :as h]
            [allergenie.components.nav :as n]
            [markdown.core :as md]))

(defn record
  [a]
  (html5 {:lang "en"}
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (n/nav-bar)
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
         (h/head)
         [:body
          [:div {:class "container"}
           [:h1 {:class "title is-1 has-text-centered"} "AllerGenie"]
           (n/nav-bar)
           [:div {:class "m-4"}
            (form/form-to
              [:post (if a (str "/records/" (:_id a)) "/records")]
              [:div {:class "field"}
               (form/label {:class "label"} "title" "Title")
               (form/text-field {:required ""} "title" (:title a))]
              [:div {:class "field"}
               (form/label {:class "label"} "body" "Body")
               (form/text-area {:class "textarea" :required ""} "body" (:body a))]
              (anti-forgery-field)
              (form/submit-button {:class "button is-info"} "Save"))]]]))
