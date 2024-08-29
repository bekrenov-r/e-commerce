drop schema public cascade;
create schema public;

CREATE TABLE "order" (
  customer_email text,
  number text,
  status text,
  total_price numeric,
  total_price_after_discount numeric,
  delivery_id uuid,
  created_at timestamp,
  last_updated_at timestamp,
  id uuid PRIMARY KEY
);

create table item_entry(
    id uuid,
    item_id uuid,
    item_name text,
    item_size text,
    quantity int,
    discount numeric,
    item_price numeric,
    item_price_after_discount numeric,
    total_price numeric,
    total_price_after_discount numeric
);

create table order_item_entry(
    order_id uuid,
    item_entry_id uuid
);

create table cart(
    id uuid,
    customer_email text
);

create table cart_item_entry(
    cart_id uuid,
    item_entry_id uuid
);

CREATE TABLE delivery (
  method text,
  order_id uuid,
  customer_id text,
  company text,
  address_id text,
  is_paid boolean,
  shipment_date date,
  delivery_date date,
  status text,
  number text,
  id uuid PRIMARY KEY,
  FOREIGN KEY (order_id) REFERENCES "order"(id)
);

insert into "order"(customer_email, number, status, total_price, total_price_after_discount, delivery_id, created_at, last_updated_at, id)
values ('romabeta627@gmail.com', 'number', 'ACCEPTED', 100.0, 100.0, '1d9b56c8-dc5a-47b7-977c-dbf932757bce', current_timestamp, current_timestamp, '4c17c3c9-97a2-45ba-aaf4-d0d1fff56078');

insert into item_entry(id, item_id, item_name, item_size, quantity, discount, item_price, item_price_after_discount, total_price, total_price_after_discount)
values ('72d755e3-3372-4015-b0dd-652b6b7cc916', '8594cc60-3807-4cdd-bc5b-7e722ae4fbc0', 'Shoes', '41', 2, 0.0, 50.0, 50.0, 50.0, 50.0),
       ('1d49d4d5-2e10-4b66-abb3-4a57492ab1e8', '80e9e95f-b187-48b4-8ed4-5898ec25ae2c', 'Shoes', '41', 2, 0.0, 50.0, 50.0, 50.0, 50.0);

insert into order_item_entry(order_id, item_entry_id)
values ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '72d755e3-3372-4015-b0dd-652b6b7cc916'),
       ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '1d49d4d5-2e10-4b66-abb3-4a57492ab1e8');

