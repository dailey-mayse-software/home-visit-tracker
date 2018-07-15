(ns home-visit-tracker.rbac.config
  (:require [crypto.password.bcrypt :as bcrypt]
            [home-visit-tracker.rbac.db :as db]))

(defn create-user!
  "Create a new user by hashing their password and storing it in the DB"
  [username password]
  (let [password-hash (bcrypt/encrypt password)]
    (db/create-user! username password-hash "todo@email.com")))

(defn delete-all-users! []
  "Delete all existing users (only for dev purposes)"
  (db/delete-all-users!))

(defn get-roles-by-user
  "Get all roles that a given user-id belongs to"
  [user-id]
  (str (db/get-roles-by-user-id user-id)))

(defn get-operations-by-user
  "Get all operations that a given user-id is allowed to perform"
  [user-id]
  (str (db/get-operations-by-user-id user-id)))
