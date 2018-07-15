(ns home-visit-tracker.nav
  (:require [hiccup.core :refer :all]
            [hiccup.page :refer :all]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(defn navbar []
  [:nav.navbar.is-primary
   [:div.navbar-brand
    [:a.navbar-item {:href "/admin/clients"}
     [:i.fa.fa-home.fa-2x]]]])