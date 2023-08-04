CREATE TABLE customer (
  first_name text NOT NULL,
  last_name text NOT NULL,
  email text NOT NULL,
  phone_number_prefix text,
  phone_number text,
  is_registered boolean NOT NULL,
  created_at timestamp NOT NULL,
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

CREATE TABLE review (
  customer_id text NOT NULL,
  item_id text NOT NULL,
  rating smallint NOT NULL,
  content text NOT NULL,
  created_at timestamp NOT NULL,
  updated_at timestamp NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  id text not null PRIMARY KEY
);