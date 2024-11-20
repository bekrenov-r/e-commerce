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

insert into customer(first_name, last_name, email, is_registered, created_at, id)
values
    ('Jane', 'Doe', 'jane.doe@example.com', true, current_timestamp, '5018177b-b2cb-4d07-aba9-3132583fe7af'),
    ('Mathew', 'Smith', 'bekrenov.s@gmail.com', true, current_timestamp, '37678e03-9aa8-4788-9486-e531aab2a855'),
    ('Roman', 'Bekrenov', 'roman.bekrenov@interia.pl', false, current_timestamp, 'ef96381d-4682-45e9-94db-63a3d06b0b84'),
    ('Clarke', 'Rosenberg', 'cr@example.com', true, current_timestamp, 'ac067e0a-207d-4685-96d3-683aa9a3bc37');

insert into customer_wish_list(customer_id, item_id)
values ('5018177b-b2cb-4d07-aba9-3132583fe7af', 'fa5f648e-b2b8-4f8a-a0d7-d65a4547f8d6'),
       ('5018177b-b2cb-4d07-aba9-3132583fe7af', 'b867560e-a92f-44a3-8096-a0f088034460'),
       ('37678e03-9aa8-4788-9486-e531aab2a855', 'af535b75-4050-4db3-bcde-5da06ddb2ae2'),
       ('37678e03-9aa8-4788-9486-e531aab2a855', 'bfa39013-6d85-4d11-99af-78619cdea942');