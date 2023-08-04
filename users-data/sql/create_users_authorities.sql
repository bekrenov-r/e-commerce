CREATE TABLE users (
  username VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL,
  enabled BOOLEAN NOT NULL,
  id serial PRIMARY KEY
);

CREATE TABLE authorities (
  user_id int not null,
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (user_id) REFERENCES users (id)
);