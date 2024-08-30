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

CREATE TABLE customer_wish_list (
  customer_id uuid,
  item_id uuid
);

CREATE TABLE cart (
  customer_id uuid NOT NULL,
  item_id uuid NOT NULL,
  quantity int NOT NULL,
  FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
  PRIMARY KEY (customer_id, item_id)
);

insert into customer(first_name, last_name, email, is_registered, created_at, id) values ('Jane', 'Doe', 'jane.doe@example.com', true, current_timestamp, '5018177b-b2cb-4d07-aba9-3132583fe7af');
insert into customer_wish_list(customer_id, item_id)
values ('5018177b-b2cb-4d07-aba9-3132583fe7af', '947341d0-2998-4daf-b4f3-60066d31b23d'),
       ('5018177b-b2cb-4d07-aba9-3132583fe7af', '484abbe4-4d57-4f25-a02c-96b860d2746a');