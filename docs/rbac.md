# Role-Based Access Control
General user-management for Clojure web applications.

## Concepts
**User** - Any person with an account who logs in and uses the application.  
**Operation** - Any (and every) action that is able to be performed within the application  
**Role** - Collections of operations able to be performed by a given group of users  
**Session** - Persistent authenticated state given to valid and active users

## Use Cases
TODO

## API
```bash
# Core
authenticate: username, password -> auth-response
deauthenticate: session-id -> auth-response
deauthenticate-user: session-id -> auth-response
is-authenticated?: session-id -> bool
is-authorized?: session-id, operation -> bool

# Config
TODO
```

## Tables
**User**
 
Name | Type | PK | Foreign Key | Description
--- | --- | --- | --- | ---
user_id | integer | Yes | | Unique identifier for each application user
username | varchar | | | Unique username used to identify a user
password | varchar | | | Password hash used for authentication
email_address | varchar | | | Email address needed for password resets

**Operation**

Name | Type | PK | Foreign Key | Description
--- | --- | --- | --- | ---
operation_id | integer | Yes | | Unique identifier for each operation
app_function | varchar | | | Callable function within the application

**Role**

Name | Type | PK | Foreign Key | Description
--- | --- | --- | --- | ---
role_id | integer | Yes | | Unique identifier for each role
role_name | varchar | | | Name of the role (such as "admin" or "employee")

**Session**

Name | Type | PK | Foreign Key | Description
--- | --- | --- | --- | ---
session_id | integer | Yes| | Unique identifier for each session
user_id | integer | | user(user_id) | User associated with the session
expiration_date | datetime | | | Date when the session expires

**Membership**  
Enables the many-to-many relationship between users and roles.

Name | Type | PK | Foreign Key | Description
--- | --- | --- | --- | ---
user_id | integer | Yes | user(user_id) | Dictates which users...
role_id | integer | Yes | role(role_id) | Belong to which roles

**Permission**  
Enables the many-to-many relationship between roles and operations.

Name | Type | PK | Foreign Key | Description
--- | --- | --- | --- | ---
role_id | integer | Yes | role(role_id) | Dictates which roles...
operation_id | integer | Yes | operation(operation_id) | Can perform which operations
