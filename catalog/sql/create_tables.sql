\c items_data

drop schema public cascade;
create schema public;

create table category
(
    name       text,
    img_name   text,
    enum_value text,
    id         uuid primary key
);

create table subcategory
(
    name        text,
    category_id uuid,
    id          uuid primary key,
    foreign key (category_id) references category (id)
);

create table brand
(
    name text,
    id   uuid primary key
);

CREATE TABLE item
(
    name                 text,
    description          text,
    price                float,
    discount             float,
    price_after_discount float,
    category_id          uuid,
    subcategory_id       uuid,
    brand_id             uuid,
    color                text,
    gender               text,
    collection           text,
    material             text,
    season               text,
    rating               float,
    item_code            text,
    id                   uuid PRIMARY KEY,
    foreign key (category_id) references category (id),
    foreign key (subcategory_id) references subcategory (id)
);

create table image
(
    item_id uuid,
    path    text,
    id      uuid primary key,
    foreign key (item_id) references item (id)
);

CREATE TABLE item_details
(
    item_id                 uuid,
    orders_count_total      int,
    orders_count_last_month int,
    created_at              timestamp,
    creating_employee       text,
    FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE unique_item
(
    item_id  uuid,
    size     text,
    quantity int,
    id       uuid PRIMARY KEY,
    FOREIGN KEY (item_id) REFERENCES item (id)
);

create table landing_page_item
(
    item_id uuid,
    foreign key (item_id) references item (id)
);

INSERT INTO category VALUES
                         ('T-Shirts', 't_shirts.png', 'T_SHIRTS', 'c893d218-7b2d-4d8b-a41e-9e42de9cfc9f'),
                         ('Shirts', 'shirts.png', 'SHIRTS', '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d'),
                         ('Trousers', 'trousers.png', 'TROUSERS', 'a719d81d-e219-4e2e-b2d4-f5432198e783'),
                         ('Shorts', 'shorts.png', 'SHORTS', 'fac4b21e-339d-478d-8bbc-242b1898b89e'),
                         ('Hoodies & Sweatshirts', 'hoodies_and_sweatshirts.png', 'HOODIES_AND_SWEATSHIRTS', 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92'),
                         ('Sweaters', 'sweaters.png', 'SWEATERS', '8fcc212f-6789-4e7e-a987-234b12c9bdfd'),
                         ('Coats', 'coats.png', 'COATS', '52b4d09b-18d2-4b80-b5ab-dfe2c978e2f1'),
                         ('Jackets', 'jackets.png', 'JACKETS', '1b324b12-d919-4adf-88b9-238e8721c4bf'),
                         ('Shoes', 'shoes.png', 'SHOES', 'e09c3121-b2df-4f32-a87e-218b9127e00f'),
                         ('Underwear', 'underwear.png', 'UNDERWEAR', '9c2b1dbd-daf2-4e0d-b12b-219e8234c189'),
                         ('Socks', 'socks.png', 'SOCKS', '481d2b21-1e2f-43ab-b9d1-298db72348f2'),
                         ('Accessories', 'accessories.png', 'ACCESSORIES', '0b34c1df-b922-4e28-b122-273db129e98f');

INSERT INTO subcategory(name, category_id, id)
VALUES ('JEANS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '27e1d04b-1b23-4b29-aabd-228b98d7ef1f'),
       ('JOGGERS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '081f2b12-9b23-42e8-b1ab-28db7821cbef'),
       ('SPORT', 'a719d81d-e219-4e2e-b2d4-f5432198e783', 'f12c3b45-2b19-48e2-b9ad-2987b1234faf'),
       ('TROUSERS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '79d99c8e-a97a-4c23-bcdf-ecb000f560c1'),
       ('LOWNECK', '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d', '7340c05a-4637-4982-889b-d130ea28dad2'),
       ('HOODIES', 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92', '17666dec-42f7-4e6f-b43c-059313c2d083'),
       ('SWEATSHIRTS', 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92', 'cb595f2c-cc93-4858-b701-6c8380d63ebe'),
       ('POLO', '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d', '3a5427b4-766f-482d-a39d-ac8aca2da3f0'),
       ('SANDALS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'b45e1231-9dfa-4f2b-a21e-234c8927ebf9'),
       ('SNEAKERS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2'),
       ('BOOTS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'a12b34df-b8df-42ab-b9ba-2837b1432faf'),
       ('PURSES', '0b34c1df-b922-4e28-b122-273db129e98f', 'acf98bbe-8c12-47f4-b085-87f05dd70ee8'),
       ('BACKPACKS', '0b34c1df-b922-4e28-b122-273db129e98f', 'ffbb77ae-05c7-42ee-8db1-a1d79a6bab50'),
       ('WALLETS', '0b34c1df-b922-4e28-b122-273db129e98f', '919fc452-af7b-40a4-9b8d-5292267ea28a');

insert into brand values
    ('SoleCraft Co.', '9e527ff4-af9a-4c0e-88be-12bff827c877'),
    ('LegLoom', '26f7679f-2e79-4d62-bdfd-601534e312e1'),
    ('CozyKnits', '8a5e1bb2-60a2-4c88-ac03-388285b4ca38'),
    ('Headline', '3e0cb534-958c-405b-837d-951167589173'),
    ('ArtistryThreads', '401552c6-0a78-4d82-9917-9711f88b71c3');