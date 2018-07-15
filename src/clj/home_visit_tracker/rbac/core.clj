(ns home-visit-tracker.rbac.core
  (:require [crypto.password.bcrypt :as bcrypt]
            [home-visit-tracker.rbac.db :as db]))

(defn- in?
  "Returns true if coll contains elm, otherwise returns false"
  [coll elm]
  (some #(= elm %) coll))

(defn- generate-session-id
  "Generate a session-id via UUID"
  []
  (str (java.util.UUID/randomUUID)))

(defn- days->seconds
  "Convert a number of days to number of seconds"
  [days]
  (* 60 60 24 days))

(defn- create-init-session-cookie
  "Create a cookie that stores a user's session-id"
  [session-id]
  {:value session-id
   :max-age (days->seconds 14)
   ;; :secure true  ;; Re-enable once HTTPS is setup
   :http-only true})

(defn- create-destroy-session-cookie
  "Create a cookie that removes a user's session-id"
  []
  {:value ""
   :max-age 0
   ;; :secure true  ;; Re-enable once HTTPS is setup
   :http-only true})

(defn- get-user-by-username-and-password
  "Return the user that matches on both username and password otherwise nil"
  [username password]
  (when-let [user (db/get-user-by-username username)]
    (when (bcrypt/check password (:password_hash user))
      user)))

(defn authenticate
  "Attempt to authenticate a user with the given username and password.
   Always returns a response map, though the keys will vary depending on whether
     the attempt succeeds or fails.

   If success:
     {:status :ok
      :user-id <user-id>
      :session-id <session-id>
      :init-session-cookie <init-session-cookie>}

   If failed:
     {:status :error
      :reason <reason>}
    where <reason> is one of #{ :invalid-username, :invalid-password }"
  [username password]
  (if-let [user (get-user-by-username-and-password username password)]
    (let [user-id (:user_id user)
          session-id (generate-session-id)
          init-session-cookie (create-init-session-cookie session-id)]
      (do
        (db/create-session! session-id user-id)
        {:status :ok
         :user-id user-id
         :session-id session-id
         :init-session-cookie init-session-cookie}))
    {:status :error
     :reason :todo-invalid-username-or-password}))

(defn deauthenticate
  "Deauthenticate an individual session user by invalidating their current session token.
   Always returns a response map, though the keys will vary depending on whether
     the attempt succeeds or fails.

   If success:
     {:status :ok
      :user-id <user-id>
      :session-id <session-id>
      :destroy-session-cookie <destroy-session-cookie>}

   If failed:
     {:status :error
      :reason <reason>}
    where <reason> is one of #{ :todo-not-sure-yet }"
  [session-id]
  (let [user-id (:user_id (db/get-user-by-session-id session-id))
        destroy-session-cookie (create-destroy-session-cookie)]
    (if user-id
      (do
        (db/delete-session! session-id)
        {:status :ok
         :user-id user-id
         :session-id session-id
         :destroy-session-cookie destroy-session-cookie})
      {:status :error
       :reason :todo-not-sure-yet})))

(defn deauthenticate-user
  "Deauthenticate a user by invalidating all of their currently active session tokens.
   Always returns a response map, though the keys will vary depending on whether
     the attempt succeeds or fails.

   If success:
     {:status :ok
      :user-id <user-id>
      :session-ids [<session-id>]
      :destroy-session-cookie <destroy-session-cookie>}

   If failed:
     {:status :error
      :error <reason>}
    where <reason> is one of #{ :todo-not-sure-yet }"
  [session-id]
  (let [user-id (:user_id (db/get-user-by-session-id session-id))
        session-ids (map :session_id (db/get-sessions-by-user-id user-id))
        destroy-session-cookie (create-destroy-session-cookie)]
    (if user-id
      (do
        (db/delete-sessions-by-user-id! user-id)
        {:status :ok
         :user-id user-id
         :session-ids session-ids
         :destroy-session-cookie destroy-session-cookie})
      {:status :error
       :reason :todo-not-sure-yet})))

(defn is-authenticated?
  "Check whether a session-id is active and valid.
   Returns true if the session is authenticated, otherwise returns false."
  [session-id]
  (db/session-exists? session-id))

(defn is-authorized?
  "Check whether a session-id is allowed to perform a particular operation.
   Returns true if the session is allowed to perform the operation, otherwise returns false."
  [session-id operation]
  (let [user-id (:user_id (db/get-user-by-session-id session-id))
        operations (map :app_function (db/get-operations-by-user-id user-id))]
    (in? operations operation)))
