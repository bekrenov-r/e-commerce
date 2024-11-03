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
    item_size text,
    quantity int
);

create table order_item_entry(
    order_id uuid,
    item_entry_id uuid,
    unique(item_entry_id)
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

insert into "order"(id, customer_email, number, status, total_price, total_price_after_discount, delivery_id, created_at, last_updated_at)
values ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', 'bekrenov.s@gmail.com', '20241030-8402745', 'ACCEPTED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp),
       ('0a55358f-005f-4ed2-8d12-6ba0ad907909', 'bekrenov.s@gmail.com', '20241029-3474833', 'SHIPPING', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp),
       ('0b8108eb-dd9b-47f8-aa67-3fd34f8a5a6b', 'bekrenov.s@gmail.com', '20241028-8987359', 'DELIVERED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp),
       ('36848d2a-22ac-4f46-a9ec-5f4f7e457bdc', 'bekrenov.s@gmail.com', '20241027-0912893', 'COMPLETED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp),
       ('bd9cf9c9-55fc-4972-a848-a93ba4fedf35', 'bekrenov.s@gmail.com', '20241026-9234573', 'CANCELLED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp);

insert into item_entry(id, item_id, item_size, quantity)
values ('72d755e3-3372-4015-b0dd-652b6b7cc916', '8594cc60-3807-4cdd-bc5b-7e722ae4fbc0', '41', 2),
       ('1d49d4d5-2e10-4b66-abb3-4a57492ab1e8', '80e9e95f-b187-48b4-8ed4-5898ec25ae2c', '41', 2),
       ('a66ee450-956c-4e02-aca3-1774d27145ef', 'd4f5ebcb-a66f-48fc-8c7a-009aa8ea33c1', 'XS', 1),
       ('634e286d-6170-4e90-bd4b-730f4d88ac94', '6d819e32-afff-4a1b-94cf-8c10d0382c91', '32', 2),
       ('ea31ac3b-5601-41f1-822c-c8bacce31d9b', 'ea7cb002-3fea-4051-9f8d-c86ca4d4a696', 'XXL', 1),
       ('10a19850-7bc1-42f6-8dd2-3fa24764e408', '4eca2602-1495-49e8-9ffe-123a65c9df2c', 'L', 1);

insert into order_item_entry(order_id, item_entry_id)
values ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '72d755e3-3372-4015-b0dd-652b6b7cc916'),
       ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '1d49d4d5-2e10-4b66-abb3-4a57492ab1e8'),
       ('0a55358f-005f-4ed2-8d12-6ba0ad907909', 'a66ee450-956c-4e02-aca3-1774d27145ef'),
       ('0b8108eb-dd9b-47f8-aa67-3fd34f8a5a6b', '634e286d-6170-4e90-bd4b-730f4d88ac94'),
       ('36848d2a-22ac-4f46-a9ec-5f4f7e457bdc', 'ea31ac3b-5601-41f1-822c-c8bacce31d9b'),
       ('bd9cf9c9-55fc-4972-a848-a93ba4fedf35', '10a19850-7bc1-42f6-8dd2-3fa24764e408');
