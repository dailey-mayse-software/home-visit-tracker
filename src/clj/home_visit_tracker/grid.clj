(ns home-visit-tracker.grid
  (:require [hiccup.core :refer :all]
            [hiccup.page :refer :all]))

(defn create-table [table_properties field_definitions endpoints]
  {
   :table_properties table_properties
   :field_definitions field_definitions
   :endpoints endpoints
   })

(defn create-table-properties [display_name]
  {
   :display_name display_name
   })

(defn define-field [symbol symbol_text max_size is_required display_name is_key]
  {
   :symbol symbol
   :symbol_text symbol_text
   :max_size max_size
   :is_required is_required
   :display_name display_name
   :is_key is_key
   })

(defn get-key-field [table]
  (first (filter #(:is_key %) (:field_definitions table))))

(defn get-key-symbol [table]
  (:symbol (get-key-field table)))

(defn define-endpoints [get_all get_by_id edit_form update create_form create delete]
  {
   :get_all get_all
   :get_by_id get_by_id
   :edit_form edit_form
   :update update
   :create_form create_form
   :create create
   :delete delete
   })


(def clients
  (create-table (create-table-properties "All Clients")
  [
     (define-field :client_id "client_id" 20 true "" true)
     (define-field :first_name "first_name" 20 true "First Name" false)
     (define-field :last_name "last_name" 20 true "Last Name" false)
     (define-field :city "city" 20 true "City" false)
     (define-field :county "county" 20 true "County" false)
    ]
  (define-endpoints "/admin/clients" "/admin/clients/?" "/admin/clients/edit/?" "/admin/clients/update/?" "/admin/clients/form" "/admin/clients/new" "/admin/clients/delete/?")))