DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS user_client;
DROP TABLE IF EXISTS visit;
DROP TABLE IF EXISTS referral;
DROP TABLE IF EXISTS discharge;
DROP TABLE IF EXISTS consultation;
DROP TABLE IF EXISTS assessment;

CREATE TABLE client (
  client_id INTEGER PRIMARY KEY,
  first_name VARCHAR,
  last_name VARCHAR,
  city VARCHAR,
  county VARCHAR
);

CREATE TABLE user_client (
  user_id INTEGER REFERENCES user(user_id),
  client_id INTEGER REFERENCES client(client_id),
  PRIMARY KEY (user_id, client_id)
);

CREATE TABLE visit (
  user_id INTEGER REFERENCES user(user_id),
  client_id INTEGER REFERENCES client(client_id),
  visit_id INTEGER,
  visit_date DATETIME,
  visit_duration INTEGER,
  PRIMARY KEY (user_id, client_id, visit_id)
);

CREATE TABLE referral (
  user_id INTEGER REFERENCES user(user_id),
  client_id INTEGER REFERENCES client(client_id),
  referral_id INTEGER,
  referral_date DATE,
  ifsp_start_date DATE,
  frequency INTEGER,
  initial_visit_date DATE,
  PRIMARY KEY (user_id, client_id, referral_id)
);

CREATE TABLE discharge (
  user_id INTEGER REFERENCES user(user_id),
  client_id INTEGER REFERENCES client(client_id),
  discharge_id INTEGER,
  discharge_date DATE,
  age_appr VARCHAR,
  delayed INTEGER,
  reason VARCHAR,
  placement VARCHAR,
  PRIMARY KEY (user_id, client_id, discharge_id)
);

CREATE TABLE consultation (
  user_id INTEGER REFERENCES user(user_id),
  client_id INTEGER REFERENCES client(client_id),
  consultation_id INTEGER,
  meeting_type VARCHAR,
  meeting_date DATETIME,
  signed_off INTEGER,
  doc_approp_teids DATE,
  comments VARCHAR,
  PRIMARY KEY (user_id, client_id, consultation_id)
);

CREATE TABLE assessment (
  user_id INTEGER REFERENCES user(user_id),
  client_id INTEGER REFERENCES client(client_id),
  assessment_id INTEGER,
  due_date DATE,
  meeting_type VARCHAR,
  submitted_date DATETIME,
  teids_entry_date DATE,
  compliance INTEGER,
  comments VARCHAR,
  PRIMARY KEY (user_id, client_id, assessment_id)
);
