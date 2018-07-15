(ns home-visit-tracker.handler
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.util.response :refer [response redirect]]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [crypto.password.bcrypt :as bcrypt]
            [home-visit-tracker.db :as db]
            [home-visit-tracker.grid :as grid]
            [home-visit-tracker.rbac.core :as rbac]
            [home-visit-tracker.rbac.config :as rbac-config]
            [home-visit-tracker.utils :refer [reflect-fn-name]]
            [home-visit-tracker.views :as v])
  (:gen-class))

(defn post-client
  [first_name last_name city county]
  (db/create-client! first_name last_name city county)
  (redirect "/admin/clients"))

(defn delete-client
  [client_id]
  (db/delete-client! client_id)
  (redirect "/admin/clients"))

(defn update-client
  [client_id first_name last_name city county]
  (db/update-client! client_id first_name last_name city county)
  (redirect "/admin/clients"))

(defn response-unauthorized
  [body]
  {:status 401
   :body body
   :headers {"Content-Type" "text/text"}})

(defn post-login
  "If the creds are valid, initialize a session and return a session-id cookie"
  [{{username "username" password "password"} :form-params cookies :cookies :as req}]
  (let [auth-response (rbac/authenticate username password)]
    (if (= :ok (:status auth-response))
      (let [user-id (:user-id auth-response)
            session-id (:session-id auth-response)
            expiration (:expiration auth-response)
            init-session-cookie (:init-session-cookie auth-response)]
        (assoc (redirect "/admin")
               :cookies (assoc cookies "session-id" init-session-cookie)))
      ;; TODO Better feedback that their info was incorrect for some reason
      (let [reason (:reason auth-response)]
        (println (str "Auth failed due to reason: " reason))
        (redirect "/login")))))

(defn get-logout
  "Destroy a session and remove the session-id cookie"
  [{cookies :cookies :as req}]
  (let [session-id (get-in cookies ["session-id" :value])
        auth-response (rbac/deauthenticate session-id)
        destroy-session-cookie (:destroy-session-cookie auth-response)]
    (println "Logging out user: " (:user-id auth-response))
    ;; Remove the cookie and redirect
    (assoc (redirect "/login")
           :cookies (assoc cookies "session-id" destroy-session-cookie))))

(defn wrap-authenticated?
  "Check if a valid session-id cookie is present in the request"
  [handler]
  (fn [request]
    (let [session-id (get-in request [:cookies "session-id" :value])]
      (if (rbac/is-authenticated? session-id)
        (handler request)
        (response-unauthorized "Unauthorized.")))))

(defn wrap-authorized?
  "Given an operation and a request, determine if the user associated with this session
    has permission to perform the desired operation"
  [operation request]
  (let [session-id (get-in request [:cookies "session-id" :value])
        operation-name (reflect-fn-name operation)]
    (if (rbac/is-authorized? session-id operation-name)
      (operation request)
      (response-unauthorized "You don't have permission to do this!"))))

(defn show-roles
  [request]
  "Here is where the roles would be!")

(defn show-operations
  [request]
  "Here is where the operations would be!")

(defroutes protected-routes
  (GET "/" [] "You are able to view the admin page!<br><a href=\"/logout\">Logout</a>")

  (GET "/clients/form" [] (v/render-page-with-nav (v/create-form grid/clients)))
  (GET "/clients/edit/:client_id" [client_id] (v/render-page-with-nav (v/update-form grid/clients (db/get-client client_id))))
  (GET "/clients" [] (v/render-page-with-nav (v/grid grid/clients (db/get-all-clients))))
  (POST "/clients/new" [first_name last_name city county]
    (post-client first_name last_name city county))
  (POST "/clients/update/:client_id" [client_id first_name last_name city county]
    (update-client client_id first_name last_name city county))
  (POST "/clients/delete/:client_id" [client_id]
    (delete-client client_id))

  (GET "/" [] "You are able to view the admin page!<br><a href=\"/logout\">Logout</a>")
  (GET "/roles" [] #(wrap-authorized? #'show-roles %))
  (GET "/operations" [] #(wrap-authorized? #'show-operations %)))

(defroutes app-routes
  (context "/admin" []
    (wrap-authenticated? protected-routes))

  (GET  "/" [] (v/render-page [:h1 "Home Visit Tracker"]))

  (GET  "/login" [] (v/render-page (v/login-form)))
  (POST "/login" [] post-login)
  (GET "/logout" [] get-logout)

  (GET "/cljs_test" [] (v/render-page (v/cljs-test)))

  (route/not-found
   (response "Page not found")))

(def app
  (-> app-routes
      (wrap-reload)
      (wrap-defaults site-defaults)))

(defn -main [& args]
  (rbac-config/delete-all-users!)
  (rbac-config/create-user! "admin" "admin")
  (rbac-config/create-user! "user" "user")
  (jetty/run-jetty app {:port 3000}))
