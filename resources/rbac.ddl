DROP TABLE IF EXISTS operation;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS membership;

CREATE TABLE operation (
  operation_id INTEGER PRIMARY KEY,
  app_function VARCHAR
);

CREATE TABLE role (
  role_id INTEGER PRIMARY KEY,
  role_name VARCHAR
);

CREATE TABLE permission (
  role_id INTEGER REFERENCES role(role_id),
  operation_id INTEGER REFERENCES operation(operation_id),
  PRIMARY KEY (role_id, operation_id)
);

CREATE TABLE user (
  user_id INTEGER PRIMARY KEY,
  username VARCHAR UNIQUE,
  password_hash VARCHAR,
  email_address VARCHAR
);

CREATE TABLE session (
  session_id VARCHAR PRIMARY KEY,
  user_id INTEGER REFERENCES user(user_id)
);

CREATE TABLE membership (
  user_id INTEGER REFERENCES user(user_id),
  role_id INTEGER REFERENCES role(role_id),
  PRIMARY KEY (user_id, role_id)
);
