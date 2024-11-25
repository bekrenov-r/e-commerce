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
       ('bd9cf9c9-55fc-4972-a848-a93ba4fedf35', 'bekrenov.s@gmail.com', '20241026-9234573', 'CANCELLED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp),
       ('f72d6693-75a3-470c-b017-628a61fc6f1c', 'jane.doe@example.com', '20241026-4548974', 'COMPLETED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp),
       ('e551fd4f-6111-4140-9f29-abe03ae3cdfb', 'cr@example.com', '20241026-5630127', 'COMPLETED', 100.0, 100.0, gen_random_uuid(), current_timestamp, current_timestamp);

insert into item_entry(id, item_id, item_size, quantity)
values ('72d755e3-3372-4015-b0dd-652b6b7cc916', '2c2f2478-b665-473b-a2e7-d876e385028b', '41', 2),
       ('1d49d4d5-2e10-4b66-abb3-4a57492ab1e8', '60c126f7-76b7-43ab-b4a1-1147546fb7e7', '41', 2),
       ('a66ee450-956c-4e02-aca3-1774d27145ef', 'd4f5ebcb-a66f-48fc-8c7a-009aa8ea33c1', 'XS', 1),
       ('634e286d-6170-4e90-bd4b-730f4d88ac94', '6d819e32-afff-4a1b-94cf-8c10d0382c91', '32', 2),
       ('ea31ac3b-5601-41f1-822c-c8bacce31d9b', 'ea7cb002-3fea-4051-9f8d-c86ca4d4a696', 'XXL', 1),
       ('10a19850-7bc1-42f6-8dd2-3fa24764e408', '4eca2602-1495-49e8-9ffe-123a65c9df2c', 'L', 1),
       ('36986509-cf30-443e-82c1-7fe176fedbba', 'df694bcf-117b-446e-ba68-c5eab425f1d7', 'S', 1),
       ('d5618e53-756d-487c-b878-8e73e07c46ef', 'd41d3ab8-4236-49c8-b769-2c4f68eeb590', 'XL', 2),
       ('ff808fe6-9c98-4998-8a5a-0cae0036dcbe', '9f4840f2-7891-4019-930b-5502726d5f89', 'M', 1),
       ('df4406ff-365d-4878-9f30-15ec157ed4dd', '8cad22b4-9396-4365-83d5-d749fb5f84a0', 'XXL', 1),
       ('6d3653f7-aeeb-41b1-90cd-a2ff2e0c9be2', 'd41d3ab8-4236-49c8-b769-2c4f68eeb590', 'L', 2),
       ('f55eb01b-d658-4cb4-954e-f68a124ba4c3', 'd41d3ab8-4236-49c8-b769-2c4f68eeb590', 'XS', 1);

insert into order_item_entry(order_id, item_entry_id)
values ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '72d755e3-3372-4015-b0dd-652b6b7cc916'),
       ('4c17c3c9-97a2-45ba-aaf4-d0d1fff56078', '1d49d4d5-2e10-4b66-abb3-4a57492ab1e8'),
       ('0a55358f-005f-4ed2-8d12-6ba0ad907909', 'a66ee450-956c-4e02-aca3-1774d27145ef'),
       ('0b8108eb-dd9b-47f8-aa67-3fd34f8a5a6b', '634e286d-6170-4e90-bd4b-730f4d88ac94'),
       ('36848d2a-22ac-4f46-a9ec-5f4f7e457bdc', 'ea31ac3b-5601-41f1-822c-c8bacce31d9b'),
       ('bd9cf9c9-55fc-4972-a848-a93ba4fedf35', '10a19850-7bc1-42f6-8dd2-3fa24764e408'),
       ('f72d6693-75a3-470c-b017-628a61fc6f1c', '36986509-cf30-443e-82c1-7fe176fedbba'),
       ('f72d6693-75a3-470c-b017-628a61fc6f1c', 'd5618e53-756d-487c-b878-8e73e07c46ef'),
       ('f72d6693-75a3-470c-b017-628a61fc6f1c', 'ff808fe6-9c98-4998-8a5a-0cae0036dcbe'),
       ('f72d6693-75a3-470c-b017-628a61fc6f1c', 'df4406ff-365d-4878-9f30-15ec157ed4dd'),
       ('36848d2a-22ac-4f46-a9ec-5f4f7e457bdc', '6d3653f7-aeeb-41b1-90cd-a2ff2e0c9be2'),
       ('e551fd4f-6111-4140-9f29-abe03ae3cdfb', 'f55eb01b-d658-4cb4-954e-f68a124ba4c3');
