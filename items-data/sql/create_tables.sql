-- \c items_data

drop schema public cascade;
create schema public;

create table category (
  name text,
  img_name text,
  id text primary key
);

create table subcategory (
  name text,
  category_id text,
  id serial primary key,
  foreign key (category_id) references category(id)
);

create table size (
    value text,
    type text,
    id serial primary key
);

create table color (
    value text,
    id serial primary key
);

create table brand(
    name text,
    id serial primary key
);

CREATE TABLE item (
  name text,
  description text,
  price numeric,
  discount numeric,
  price_after_discount numeric,
  category_id text,
  subcategory_id int,
  brand_id text,
  gender text,
  age_group text,
  collection text,
  material text,
  season text,
  rating numeric,
  item_code text,
  id serial PRIMARY KEY,
  foreign key (category_id) references category(id),
  foreign key (subcategory_id) references subcategory(id)
);

create table item_size (
  item_id bigint,
  size_id int,
  foreign key (item_id) references item(id),
  foreign key (size_id) references size(id)
);

create table item_color (
    item_id bigint,
    color_id int,
    foreign key (item_id) references item(id),
    foreign key (color_id) references color(id)
);

create table image (
    item_id bigint,
    color_id int,
    path text,
    id serial primary key,
    foreign key (item_id) references item(id)
);

CREATE TABLE item_details (
  item_id int,
  orders_count_total bigint,
  orders_count_last_month int,
  quantity int,
  created_at timestamp,
  updated_at timestamp,
  creating_employee_id int,
  updating_employee_id int,
  FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE unique_item (
  item_id int,
  size_id int,
  color_id int,
  weight_kg numeric,
  bar_code text,
  quantity int,
  restock_quantity int,
  reorder_quantity int,
  id serial PRIMARY KEY,
  FOREIGN KEY (item_id) REFERENCES item (id)
);

create table review (
    item_id int,
    customer_id int,
    created_at timestamp,
    content text,
    rating smallint,
    likes_count int,
    dislikes_count int,
    id serial primary key
);

insert into category values
                         ('T-Shirts', 't_shirts.png', 't-shirts'),
                         ('Shirts', 'shirts.png', 'shirts'),
                         ('Trousers', 'trousers.png', 'trousers'),
                         ('Shorts', 'shorts.png', 'shorts'),
                         ('Hoodies & Sweatshirts', 'hoodies_and_sweatshirts.png', 'hoodies-and-sweatshirts'),
                         ('Sweaters', 'sweaters.png', 'sweaters'),
                         ('Coats', 'coats.png', 'coats'),
                         ('Jackets', 'jackets.png', 'jackets'),
                         ('Shoes', 'shoes.png', 'shoes'),
                         ('Underwear', 'underwear.png', 'underwear'),
                         ('Socks', 'socks.png', 'socks'),
                         ('Accessories', 'accessories.png', 'accessories');

insert into subcategory values
                            ('JEANS', 'trousers'),
                            ('JOGGERS', 'trousers'),
                            ('SPORT', 'trousers'),
                            ('SANDALS', 'shoes'),
                            ('SNEAKERS', 'shoes'),
                            ('BOOTS', 'shoes');

insert into size values  ('XS', 'CLOTHES'), ('S', 'CLOTHES'), ('M', 'CLOTHES'), ('L', 'CLOTHES'), ('XL', 'CLOTHES'), ('2XL', 'CLOTHES'), ('3XL', 'CLOTHES'), ('4XL', 'CLOTHES'),
                       ('36', 'SHOES'), ('37', 'SHOES'), ('38', 'SHOES'), ('39', 'SHOES'), ('40', 'SHOES'), ('41', 'SHOES'), ('42', 'SHOES'), ('43', 'SHOES'), ('44', 'SHOES'), ('45', 'SHOES');

insert into color values ('BLACK'), ('WHITE'), ('RED'), ('YELLOW'), ('GREEN'), ('BLUE'), ('VIOLET'), ('GREY'), ('MULTI');

insert into brand values
    ('Louis Vuitton'),
    ('Gucci'),
    ('Balenciaga'),
    ('Dior Homme'),
    ('Prada'),
    ('Salvatore Ferragamo'),
    ('Chanel'),
    ('Armani'),
    ('Yves Saint Laurent'),
    ('Burberry'),
    ('Hermès'),
    ('Lululemon'),
    ('Zara'),
    ('UNIQLO'),
    ('H&M'),
    ('Cartier'),
    ('Tiffany & Co.'),
    ('Moncler'),
    ('Rolex'),
    ('Patek Philippe');