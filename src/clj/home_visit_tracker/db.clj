(ns home-visit-tracker.db
  (:require [clojure.java.jdbc :as jdbc]))

(def db-sqlite
  {:dbtype "sqlite"
   :dbname "resources/development.db"})


(defn create-client!
  [first_name last_name city county]
  (jdbc/insert! db-sqlite :client {:first_name first_name
                                   :last_name last_name
                                   :city city
                                   :county county}))

(defn get-client
  [client_id]
  (first (jdbc/query db-sqlite ["select * from client where client_id = ?" client_id])))

(defn get-all-clients []
  (jdbc/query db-sqlite ["select * from client"]))

(defn update-client!
  [client_id first_name last_name city county]
  (jdbc/update! db-sqlite :client {:first_name first_name
                                   :last_name last_name
                                   :city city
                                   :county county}
                ["client_id = ?" client_id]))

(defn delete-client!
  [client_id]
  (jdbc/delete! db-sqlite :client ["client_id = ?" client_id]))

(defn delete-all-clients! []
  (jdbc/delete! db-sqlite :client))
