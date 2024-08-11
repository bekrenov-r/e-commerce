drop schema public cascade;
create schema public;

CREATE TABLE "order" (
  customer_id uuid,
  number text,
  status text,
  total_price numeric,
  total_price_after_discount numeric,
  delivery_id uuid,
  created_at timestamp,
  last_updated_at timestamp,
  id text PRIMARY KEY
);

create table item_entry(
    order_id uuid,
    item_id uuid,
    item_name text,
    quantity int,
    discount numeric,
    item_price numeric,
    item_price_after_discount numeric,
    total_price numeric,
    total_price_after_discount numeric,
    unique (order_id, item_id)
);

CREATE TABLE delivery (
  method text,
  order_id text,
  customer_id text,
  company text,
  address_id text,
  is_paid boolean,
  shipment_date date,
  delivery_date date,
  status text,
  number text,
  id text PRIMARY KEY,
  FOREIGN KEY (order_id) REFERENCES "order"(id)
);

