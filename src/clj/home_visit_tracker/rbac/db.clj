(ns home-visit-tracker.rbac.db
  (:require [clojure.java.jdbc :as jdbc]
            [home-visit-tracker.db :refer [db-sqlite]]))


;; Helpers

(defn get-roles-by-user-id
  "Get all roles that the given user_id belongs to"
  [user-id]
  (jdbc/query db-sqlite
              ["select r.*
                from membership m
                inner join role r
                  on r.role_id = m.role_id
                where m.user_id = ?" user-id]))

(defn get-operations-by-user-id
  "Get all allowed operations for a given user_id"
  [user-id]
  (jdbc/query db-sqlite
              ["select op.*
                from user u
                inner join membership m
                  on m.user_id = u.user_id
                inner join permission p
                  on p.role_id = m.role_id
                inner join operation op
                  on op.operation_id = p.operation_id
                where u.user_id = ?" user-id]))


;; Users

(defn create-user!
  [username password-hash email-address]
  (jdbc/insert! db-sqlite :user {:username username
                                 :password_hash password-hash
                                 :email_address email-address}))

(defn get-user-by-id
  [user-id]
  (first (jdbc/query db-sqlite ["select * from user where user_id = ?" user-id])))

(defn get-user-by-username
  [username]
  (first (jdbc/query db-sqlite ["select * from user where username = ?" username])))

(defn delete-all-users!
  []
  (jdbc/delete! db-sqlite :user []))


;; Sessions

(defn create-session!
  [session-id user-id]
  (jdbc/insert! db-sqlite :session {:session_id session-id
                                    :user_id user-id}))

(defn get-session-by-id
  [session-id]
  (first (jdbc/query db-sqlite ["select * from session where session_id = ?" session-id])))

(defn get-user-by-session-id
  [session-id]
  (first (jdbc/query db-sqlite ["select u.*
                                 from session s
                                 inner join user u
                                   on u.user_id = s.user_id
                                 where session_id = ?" session-id])))

(defn get-sessions-by-user-id
  [user-id]
  (jdbc/query db-sqlite ["select *
                          from session
                          where user_id = ?" user-id]))

(defn session-exists?
  [session-id]
  (not (nil? (get-session-by-id session-id))))

(defn delete-session!
  [session-id]
  (jdbc/delete! db-sqlite :session ["session_id = ?" session-id]))

(defn delete-sessions-by-user-id!
  [user-id]
  (jdbc/delete! db-sqlite :session ["user_id = ?" user-id]))
