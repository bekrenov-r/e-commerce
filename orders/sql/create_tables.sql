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
  id uuid PRIMARY KEY
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

insert into "order" values ('38f6eb91-a979-43d3-8d50-48d8ed476c6c', 'number', 'ACCEPTED', 100.0, 100.0, '1d9b56c8-dc5a-47b7-977c-dbf932757bce', current_timestamp, current_timestamp, '4c17c3c9-97a2-45ba-aaf4-d0d1fff56078');
insert into item_entry values ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '8594cc60-3807-4cdd-bc5b-7e722ae4fbc0', 'Shoes', 2, 0.0, 50.0, 50.0, 50.0, 50.0);

