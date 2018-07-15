(ns home-visit-tracker.views
  (:require [clojure.string :as str]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [home-visit-tracker.nav :as nav]
            [home-visit-tracker.grid :as grid]))

(defn bulma-header [title]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:title title]
   (include-css "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css")
   (include-css "https://cdnjs.cloudflare.com/ajax/libs/bulma/0.7.1/css/bulma.min.css")])

(defn cljs-test []
  [:div
   [:div#cat-name "Name"]
   [:div#status "Status"]
   [:button#button1 "Goodbye"]
   [:button#button2 "Log some stuff"]
   [:button#button3 "Big Text"]
   (include-js "/js/main.js")
   [:script {:type "text/javascript"} "home_visit_tracker.core.init()"]])

(defn render-page
  [content]
  (html5
   (bulma-header "Home Visit Tracker")
   [:body content]))

(defn render-nav-onto-content
  [content]
  [:div
   (nav/navbar)
   content])

(defn render-page-with-nav
  [content]
  (-> content
      render-nav-onto-content
      render-page))

(defn text-field-with-content
  [name label content]
  [:div.field
   [:label.label label]
   [:div.control
    [:input.input {:type "text" :value content :name name}]]])

(defn text-field
  [name label placeholder]
  [:div.field
   [:label.label label]
   [:div.control
    [:input.input {:type "text" :placeholder placeholder :name name}]]])

(defn submit-button []
  [:div.field
   [:div.control
    [:button.button.is-link {:type "submit"} "Submit"]]])

(defn cancel-button [target]
  [:div.field
   [:div.control
    [:a.button.is-danger {:href target}
     "Cancel"]]])

(defn username-field []
  [:div.field
   [:div.control
    [:p.control.has-icons-left
     [:input.input.is-large {:type "text" :placeholder "Username" :name "username"}]
     [:span.icon.is-small.is-left
      [:i.fa.fa-user]]]]])

(defn password-field []
  [:div.field
   [:div.control
    [:p.control.has-icons-left
     [:input.input.is-large {:type "password" :placeholder "Password" :name "password"}]
     [:span.icon.is-small.is-left
      [:i.fa.fa-lock]]]]])

(defn get-add-input-for-field [field_definition]
  (if (not (:is_key field_definition))
    (text-field (:symbol_text field_definition) (:display_name field_definition) "Text field")))

(defn get-update-input-for-field [field_definition element]
  (if (not (:is_key field_definition))
    (text-field-with-content
      (:symbol_text field_definition)
      (:display_name field_definition)
      ((:symbol field_definition) element))))

(defn submit-and-cancel [cancel_endpoint]
  [:table.table
   [:tr
    [:td (submit-button)]
    [:td (cancel-button cancel_endpoint)]]])

(defn create-form [table]
  [:section.section
   [:div.container
    [:form {:action (:create (:endpoints table)) :method "post"}
     (anti-forgery-field)
     (map get-add-input-for-field (:field_definitions table))
     (submit-and-cancel (:get_all (:endpoints table)))]]])

(defn update-form [table element]
  [:section.section
   [:div.container
    [:form {:action (str/replace (:update (:endpoints table)) "?" (str ((grid/get-key-symbol table) element))) :method "post"}
     (anti-forgery-field)
     (map #(get-update-input-for-field % element) (:field_definitions table))
     (submit-and-cancel (:get_all (:endpoints table)))]]])

(defn row-data [field element]
  [:td ((:symbol field) element)])

(defn add-row [table element]
  [:tr {:onclick (str "window.location.href='" (str/replace (:edit_form (:endpoints table)) "?" (str ((grid/get-key-symbol table) element))) "'") :method "post"}
   (map #(row-data % element) (:field_definitions table))
   [:td
    [:form {:action (str/replace (:delete (:endpoints table)) "?" (str ((grid/get-key-symbol table) element))) :method "post"}
     (anti-forgery-field)
     [:button.button.is-danger
      {:type "submit"}
      [:i.fa.fa-minus]]]]])

(defn get-header [field]
  [:th (:display_name field)])

(defn headers [table]
  [:thead
   [:tr
    (map get-header (:field_definitions table))
    [:th "Delete"]]])

(defn footer [table]
  [:tfoot
   [:tr
    [:td
     [:a.button.is-primary
      {:href (:create_form (:endpoints table))}
      [:i.fa.fa-plus]]]]])

(defn grid [table elements]
  [:section.section
   [:h2.title (:display_name (:table_properties table))]
   [:table.table.is-fullwidth.is-hoverable
    (headers table)
    (footer table)
    [:tbody
     (map #(add-row table %) elements)]]])

(defn login-form []
  [:section.hero.is-light.is-fullheight
   [:div.hero-body
    [:div.container.has-text-centered
     [:div.column.is-4.is-offset-4
      [:h3.title.has-text-grey "Login"]
      [:p.subtitle.has-text-grey "Please login to proceed."]
      [:div.box
       [:form {:action "/login" :method "post"}
        (anti-forgery-field)
        (username-field)
        (password-field)
        [:button.button.is-block.is-info.is-large.is-fullwidth "Login"]]]]]]])
