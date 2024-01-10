drop schema public cascade;
create schema public;

CREATE TABLE "order" (
  customer_id text not null,
  number text not null,
  status text not null,
  items_price numeric not null,
  items_price_after_discount numeric not null,
  delivery_price numeric not null,
  tax numeric not null,
  total_price numeric not null,
  id text PRIMARY KEY
);

CREATE TABLE address (
  country text not null,
  country_code text not null,
  city text not null,
  street text not null,
  building_number text not null,
  flat_number text,
  postal_code text not null,
  id text primary key
);

CREATE TABLE delivery (
  method text not null,
  order_id text not null,
  customer_id text not null,
  company text not null,
  address_id text,
  is_paid boolean default false,
  shipment_date date not null,
  delivery_date date not null,
  status text not null,
  number text not null,
  id text PRIMARY KEY,
  FOREIGN KEY (order_id) REFERENCES "order"(id),
  FOREIGN KEY (address_id) REFERENCES address (id)
);

CREATE TABLE order_details (
  order_id text not null,
  delivery_id text not null,
  receipt_url text not null,
  created_at timestamp not null,
  last_updated_at timestamp,
  returning_deadline date not null,
  FOREIGN KEY (order_id) REFERENCES "order"(id),
  FOREIGN KEY (delivery_id) REFERENCES delivery (id)
);

CREATE TABLE order_item (
  order_id text not null,
  item_id text not null,
  quantity int not null,
  price numeric not null,
  price_after_discount numeric not null,
  FOREIGN KEY (order_id) REFERENCES "order"(id)
);

