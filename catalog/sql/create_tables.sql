-- \c items_data

drop schema public cascade;
create schema public;

create table category (
  name text,
  img_name text,
  id uuid primary key
);

create table subcategory (
  name text,
  category_id uuid,
  id uuid primary key,
  foreign key (category_id) references category(id)
);

create table size (
    value text,
    type text,
    id uuid primary key
);

create table color (
    value text,
    id uuid primary key
);

create table brand(
    name text,
    id uuid primary key
);

CREATE TABLE item (
  name text,
  description text,
  price numeric,
  discount numeric,
  price_after_discount numeric,
  category_id uuid,
  subcategory_id uuid,
  brand_id uuid,
  gender text,
  collection text,
  material text,
  season text,
  rating numeric,
  item_code text,
  id uuid PRIMARY KEY,
  foreign key (category_id) references category(id),
  foreign key (subcategory_id) references subcategory(id)
);

create table item_size (
  item_id uuid,
  size_id uuid,
  foreign key (item_id) references item(id),
  foreign key (size_id) references size(id)
);

create table item_color (
    item_id uuid,
    color_id uuid,
    foreign key (item_id) references item(id),
    foreign key (color_id) references color(id)
);

create table image (
    item_id uuid,
    color_id uuid,
    path text,
    id uuid primary key,
    foreign key (item_id) references item(id)
);

CREATE TABLE item_details (
  item_id uuid,
  orders_count_total bigint,
  orders_count_last_month int,
  quantity int,
  created_at timestamp,
  updated_at timestamp,
  creating_employee_id uuid,
  updating_employee_id uuid,
  FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE unique_item (
  item_id uuid,
  size_id uuid,
  color_id int,
  weight_kg numeric,
  bar_code text,
  quantity int,
  restock_quantity int,
  reorder_quantity int,
  id uuid PRIMARY KEY,
  FOREIGN KEY (item_id) REFERENCES item (id)
);

create table landing_page_item(
    item_id uuid,
    foreign key (item_id) references item(id)
);

INSERT INTO category VALUES
                         ('T-Shirts', 't_shirts.png', 'c893d218-7b2d-4d8b-a41e-9e42de9cfc9f'),
                         ('Shirts', 'shirts.png', '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d'),
                         ('Trousers', 'trousers.png', 'a719d81d-e219-4e2e-b2d4-f5432198e783'),
                         ('Shorts', 'shorts.png', 'fac4b21e-339d-478d-8bbc-242b1898b89e'),
                         ('Hoodies & Sweatshirts', 'hoodies_and_sweatshirts.png', 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92'),
                         ('Sweaters', 'sweaters.png', '8fcc212f-6789-4e7e-a987-234b12c9bdfd'),
                         ('Coats', 'coats.png', '52b4d09b-18d2-4b80-b5ab-dfe2c978e2f1'),
                         ('Jackets', 'jackets.png', '1b324b12-d919-4adf-88b9-238e8721c4bf'),
                         ('Shoes', 'shoes.png', 'e09c3121-b2df-4f32-a87e-218b9127e00f'),
                         ('Underwear', 'underwear.png', '9c2b1dbd-daf2-4e0d-b12b-219e8234c189'),
                         ('Socks', 'socks.png', '481d2b21-1e2f-43ab-b9d1-298db72348f2'),
                         ('Accessories', 'accessories.png', '0b34c1df-b922-4e28-b122-273db129e98f');

INSERT INTO subcategory VALUES
                            ('JEANS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '27e1d04b-1b23-4b29-aabd-228b98d7ef1f'),
                            ('JOGGERS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '081f2b12-9b23-42e8-b1ab-28db7821cbef'),
                            ('SPORT', 'a719d81d-e219-4e2e-b2d4-f5432198e783', 'f12c3b45-2b19-48e2-b9ad-2987b1234faf'),
                            ('SANDALS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'b45e1231-9dfa-4f2b-a21e-234c8927ebf9'),
                            ('SNEAKERS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2'),
                            ('BOOTS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'a12b34df-b8df-42ab-b9ba-2837b1432faf');

insert into size values  ('XS', 'CLOTHES', gen_random_uuid()), ('S', 'CLOTHES', gen_random_uuid()), ('M', 'CLOTHES', gen_random_uuid()), ('L', 'CLOTHES', gen_random_uuid()), ('XL', 'CLOTHES', gen_random_uuid()), ('2XL', 'CLOTHES', gen_random_uuid()), ('3XL', 'CLOTHES', gen_random_uuid()), ('4XL', 'CLOTHES', gen_random_uuid()),
                       ('36', 'SHOES', gen_random_uuid()), ('37', 'SHOES', gen_random_uuid()), ('38', 'SHOES', gen_random_uuid()), ('39', 'SHOES', gen_random_uuid()), ('40', 'SHOES', gen_random_uuid()), ('41', 'SHOES', gen_random_uuid()), ('42', 'SHOES', gen_random_uuid()), ('43', 'SHOES', gen_random_uuid()), ('44', 'SHOES', gen_random_uuid()), ('45', 'SHOES', gen_random_uuid());

insert into brand values
    ('Louis Vuitton', gen_random_uuid()),
    ('Gucci', gen_random_uuid()),
    ('Balenciaga', gen_random_uuid()),
    ('Dior Homme', gen_random_uuid()),
    ('Prada', gen_random_uuid()),
    ('Salvatore Ferragamo', gen_random_uuid()),
    ('Chanel', gen_random_uuid()),
    ('Armani', gen_random_uuid()),
    ('Yves Saint Laurent', gen_random_uuid()),
    ('Burberry', gen_random_uuid()),
    ('Hermès', gen_random_uuid()),
    ('Lululemon', gen_random_uuid()),
    ('Zara', gen_random_uuid()),
    ('UNIQLO', gen_random_uuid()),
    ('H&M', gen_random_uuid()),
    ('Cartier', gen_random_uuid()),
    ('Tiffany & Co.', gen_random_uuid()),
    ('Moncler', gen_random_uuid()),
    ('Rolex', gen_random_uuid()),
    ('Patek Philippe', gen_random_uuid());