drop schema public cascade;
create schema public;

CREATE TABLE address
(
    country_code    text,
    city            text,
    street          text,
    building_number text,
    flat_number     text,
    zip_code     text,
    id              text primary key
);

CREATE TABLE customer (
  first_name text,
  last_name text,
  email text,
  phone_number text,
  is_registered boolean,
  created_at timestamp,
  id text NOT NULL PRIMARY KEY,
  UNIQUE (email)
);

CREATE TABLE wish_list (
  customer_id text NOT NULL,
  item_id text NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  PRIMARY KEY (customer_id, item_id)
);

CREATE TABLE cart (
  customer_id text NOT NULL,
  item_id text NOT NULL,
  quantity int NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  PRIMARY KEY (customer_id, item_id)
);

CREATE TABLE users
(
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    enabled  BOOLEAN     NOT NULL,
    id       serial PRIMARY KEY
);

CREATE TABLE authorities
(
    user_id   int         not null,
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (user_id) REFERENCES users (id)
);