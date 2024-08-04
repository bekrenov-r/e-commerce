drop schema public cascade;
create schema public;

CREATE TABLE customer (
  first_name text,
  last_name text,
  email text,
  is_registered boolean,
  created_at timestamp,
  id uuid NOT NULL PRIMARY KEY,
  UNIQUE (email)
);

CREATE TABLE wish_list (
  customer_id uuid NOT NULL,
  item_id uuid NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  PRIMARY KEY (customer_id, item_id)
);

CREATE TABLE cart (
  customer_id uuid NOT NULL,
  item_id uuid NOT NULL,
  quantity int NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  PRIMARY KEY (customer_id, item_id)
);