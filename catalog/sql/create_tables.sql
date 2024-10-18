\c items_data

drop schema public cascade;
create schema public;

create table category (
  name text,
  img_name text,
  enum_value text,
  id uuid primary key
);

create table subcategory (
  name text,
  category_id uuid,
  id uuid primary key,
  foreign key (category_id) references category(id)
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
  color text,
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

create table image (
    item_id uuid,
    path text,
    id uuid primary key,
    foreign key (item_id) references item(id)
);

CREATE TABLE item_details (
  item_id uuid,
  orders_count_total bigint,
  orders_count_last_month int,
  created_at timestamp,
  creating_employee text,
  FOREIGN KEY (item_id) REFERENCES item (id)
);

CREATE TABLE unique_item (
  item_id uuid,
  size text,
  quantity int,
  id uuid PRIMARY KEY,
  FOREIGN KEY (item_id) REFERENCES item (id)
);

create table landing_page_item(
    item_id uuid,
    foreign key (item_id) references item(id)
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

insert into item (id, name, description, price, discount, price_after_discount, category_id, subcategory_id, brand_id, color, gender, collection, material, season, rating, item_code)
values
    ('a543d3b8-9c84-4e68-913f-bb0c74cf64b1', 'T-Shirts 101', 'Lorem ipsum dolor sit amet', 19.99, 0.0, 19.99, 'c893d218-7b2d-4d8b-a41e-9e42de9cfc9f', NULL, '9e527ff4-af9a-4c0e-88be-12bff827c877', 'RED', 'MEN', '', 'COTTON', 'SUMMER', 4.5, 'JRY78293'),
    ('9f6c7d77-35b4-44e8-8b67-1cce4a25ff35', 'Shirts 202', 'Consectetur adipiscing elit', 35.50, 0.0, 35.50, '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d', '7340c05a-4637-4982-889b-d130ea28dad2', '9e527ff4-af9a-4c0e-88be-12bff827c877', 'BLUE', 'WOMEN', '', 'DENIM', 'SPRING', 3.9, 'HFK98123'),
    ('ed4a7b67-d54d-48da-9b2c-0337d66da9cb', 'Trousers 303', 'Sed do eiusmod tempor incididunt', 45.00, 0.0, 45.00, 'a719d81d-e219-4e2e-b2d4-f5432198e783', '27e1d04b-1b23-4b29-aabd-228b98d7ef1f', '9e527ff4-af9a-4c0e-88be-12bff827c877', 'BLACK', 'MEN', '', 'LEATHER', 'WINTER', 4.1, 'LKQ43219'),
    ('63db07b2-4ae6-4625-9f4f-f1f8a0e69dcb', 'Sneakers 404', 'Ut labore et dolore magna aliqua', 85.99, 0.0, 85.99, 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2', '9e527ff4-af9a-4c0e-88be-12bff827c877', 'WHITE', 'MEN', '', 'SYNTHETICS', 'AUTUMN', 4.8, 'PVJ35627'),
    ('e9f4b6a9-5d8e-49d3-bfc9-e9f47b8d1d1f', 'Hoodies 505', 'Quis nostrud exercitation ullamco', 50.00, 0.0, 50.00, 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92', '17666dec-42f7-4e6f-b43c-059313c2d083', '9e527ff4-af9a-4c0e-88be-12bff827c877', 'GREY', 'WOMEN', '', 'WOOL', 'WINTER', 4.3, 'XVU57689'),
    ('f302c1f2-5d6a-4f9d-8ef3-1a2e83f20f69', 'T-Shirts 102', 'Lorem ipsum dolor sit amet', 22.99, 0.0, 22.99, 'c893d218-7b2d-4d8b-a41e-9e42de9cfc9f', NULL, '9e527ff4-af9a-4c0e-88be-12bff827c877', 'GREEN', 'MEN', '', 'COTTON', 'SUMMER', 4.0, 'KFC89234'),
    ('1d378cf7-b7ac-485b-8d22-dbf228f5d378', 'Hoodies 506', 'Consectetur adipiscing elit', 55.00, 0.0, 55.00, 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92', '17666dec-42f7-4e6f-b43c-059313c2d083', '26f7679f-2e79-4d62-bdfd-601534e312e1', 'BLACK', 'WOMEN', '', 'WOOL', 'AUTUMN', 4.7, 'PLR23945'),
    ('72b6c1cf-b6df-4e61-8a1d-b8bc1b41797c', 'Shirts 203', 'Sed do eiusmod tempor incididunt', 40.99, 0.0, 40.99, '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d', '3a5427b4-766f-482d-a39d-ac8aca2da3f0', '26f7679f-2e79-4d62-bdfd-601534e312e1', 'WHITE', 'MEN', '', 'DENIM', 'WINTER', 3.5, 'QWE76321'),
    ('7d8c2bf9-5285-4e7c-a8de-0287edc05ba7', 'Trousers 304', 'Ut labore et dolore magna aliqua', 60.50, 0.0, 60.50, 'a719d81d-e219-4e2e-b2d4-f5432198e783', '79d99c8e-a97a-4c23-bcdf-ecb000f560c1', '26f7679f-2e79-4d62-bdfd-601534e312e1', 'GREY', 'WOMEN', '', 'LEATHER', 'SPRING', 4.2, 'ZXC59012'),
    ('2346a8be-7489-4c0b-82b5-6f7c647fc97c', 'Sneakers 405', 'Quis nostrud exercitation ullamco', 89.99, 0.0, 89.99, 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2', '26f7679f-2e79-4d62-bdfd-601534e312e1', 'RED', 'MEN', '', 'SYNTHETICS', 'MULTISEASON', 4.9, 'UIO49235'),
    ('9a5f6a27-e346-4269-9b0f-7b7f02e67b76', 'Sweaters 606', 'Excepteur sint occaecat cupidatat non proident', 75.00, 0.0, 75.00, '8fcc212f-6789-4e7e-a987-234b12c9bdfd', NULL, '26f7679f-2e79-4d62-bdfd-601534e312e1', 'VIOLET', 'WOMEN', '', 'WOOL', 'WINTER', 3.8, 'POI80392'),
    ('34207cbf-5a1f-45a4-929c-b32ff2312a79', 'Trousers 305', 'Duis aute irure dolor in reprehenderit', 49.99, 0.0, 49.99, 'a719d81d-e219-4e2e-b2d4-f5432198e783', '081f2b12-9b23-42e8-b1ab-28db7821cbef', '26f7679f-2e79-4d62-bdfd-601534e312e1', 'BLUE', 'MEN', '', 'LEATHER', 'SUMMER', 4.1, 'KLI10924'),
    ('0f4d8d6c-b2e8-4b4e-84bb-66ff921f9f28', 'Boots 506', 'Velit esse cillum dolore eu fugiat', 120.00, 0.0, 120.00, 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'a12b34df-b8df-42ab-b9ba-2837b1432faf', '8a5e1bb2-60a2-4c88-ac03-388285b4ca38', 'BLACK', 'MEN', '', 'LEATHER', 'WINTER', 4.9, 'POQ09234'),
    ('52b1bc5e-3159-4229-a172-b1fd12b25dce', 'Shirts 204', 'Excepteur sint occaecat cupidatat non proident', 39.99, 0.0, 39.99, '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d', '7340c05a-4637-4982-889b-d130ea28dad2', '8a5e1bb2-60a2-4c88-ac03-388285b4ca38', 'YELLOW', 'WOMEN', '', 'COTTON', 'AUTUMN', 3.6, 'OPM87123'),
    ('765ae59e-c853-4b0e-8747-6fa947e56b9b', 'Jackets 706', 'Ut enim ad minim veniam', 89.50, 0.0, 89.50, '1b324b12-d919-4adf-88b9-238e8721c4bf', NULL, '8a5e1bb2-60a2-4c88-ac03-388285b4ca38', 'BLACK', 'WOMEN', '', 'LEATHER', 'MULTISEASON', 4.5, 'LKQ93471'),
    ('fd295d22-3e71-4f35-8a5b-9097d238b9f3', 'T-Shirts 101', 'Lorem ipsum dolor sit amet.', 29.99, 0.0, 29.99, 'c893d218-7b2d-4d8b-a41e-9e42de9cfc9f', null, '8a5e1bb2-60a2-4c88-ac03-388285b4ca38', 'BLACK', 'MEN', '', 'COTTON', 'SUMMER', 4, 'AQT12345'),
    ('fa946d03-407b-462a-9345-52bcbf222c44', 'Shoes 202', 'Consectetur adipiscing elit.', 89.99, 0.0, 89.99, 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2', '8a5e1bb2-60a2-4c88-ac03-388285b4ca38', 'WHITE', 'WOMEN', '', 'LEATHER', 'WINTER', 5, 'TYR67891'),
    ('41cd85b7-90da-47cf-858d-bb334cfb0a0f', 'Trousers 150', 'Ut enim ad minim veniam.', 45.50, 0.0, 45.50, 'a719d81d-e219-4e2e-b2d4-f5432198e783', '27e1d04b-1b23-4b29-aabd-228b98d7ef1f', '8a5e1bb2-60a2-4c88-ac03-388285b4ca38', 'BLUE', 'MEN', '', 'DENIM', 'AUTUMN', 3, 'IUY15930'),
    ('f95c4a4d-ecca-4d7a-8aa7-64a8ed8a735a', 'Sweaters 235', 'Duis aute irure dolor in reprehenderit.', 60.00, 0.0, 60.00, '8fcc212f-6789-4e7e-a987-234b12c9bdfd', null, '3e0cb534-958c-405b-837d-951167589173', 'GREY', 'WOMEN', '', 'WOOL', 'WINTER', 4, 'LSO24353'),
    ('b03f2b29-5c36-4e45-92d5-7f1a8701bc61', 'Coats 312', 'Excepteur sint occaecat cupidatat.', 150.00, 0.0, 150.00, '52b4d09b-18d2-4b80-b5ab-dfe2c978e2f1', null, '3e0cb534-958c-405b-837d-951167589173', 'RED', 'MEN', '', 'LEATHER', 'WINTER', 5, 'POW12012'),
    ('86d96712-d8f9-4de3-8d8e-8f3b16c7bdf5', 'Sweaters 421', 'Lorem ipsum dolor sit amet.', 59.99, 0.0, 59.99, '8fcc212f-6789-4e7e-a987-234b12c9bdfd', null, '3e0cb534-958c-405b-837d-951167589173', 'RED', 'WOMEN', '', 'WOOL', 'WINTER', 5, 'QRS12549'),
    ('eb83e8a9-492a-423e-8b20-9f6b23f7e235', 'Jackets 314', 'Consectetur adipiscing elit.', 120.00, 0.0, 120.00, '1b324b12-d919-4adf-88b9-238e8721c4bf', null, '3e0cb534-958c-405b-837d-951167589173', 'GREEN', 'MEN', '', 'LEATHER', 'AUTUMN', 4, 'BGT94623'),
    ('9c8e057e-0044-4504-9750-0469f29ff143', 'Hoodies 522', 'Ut enim ad minim veniam.', 75.00, 0.0, 75.00, 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92', '17666dec-42f7-4e6f-b43c-059313c2d083', '3e0cb534-958c-405b-837d-951167589173', 'BLACK', 'WOMEN', '', 'COTTON', 'SPRING', 3, 'JKL37465'),
    ('fa9b4a5f-2644-4958-9369-02efc2ab8a38', 'Trousers 205', 'Duis aute irure dolor in reprehenderit.', 55.00, 0.0, 55.00, 'a719d81d-e219-4e2e-b2d4-f5432198e783', 'f12c3b45-2b19-48e2-b9ad-2987b1234faf', '3e0cb534-958c-405b-837d-951167589173', 'GREY', 'MEN', '', 'DENIM', 'AUTUMN', 4, 'VFR48395'),
    ('ad3ebcc7-4d71-44f5-b5fd-3b58a9d3ec25', 'Shoes 725', 'Excepteur sint occaecat cupidatat.', 95.99, 0.0, 95.99, 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2', '401552c6-0a78-4d82-9917-9711f88b71c3', 'WHITE', 'WOMEN', '', 'LEATHER', 'SUMMER', 5, 'TGB94357'),
    ('28b4e579-bd91-48f9-9b65-937aaab712e6', 'Shirts 849', 'Lorem ipsum dolor sit amet.', 39.99, 0.0, 39.99, '7e4f0df0-d3f2-4b92-84e2-9b389b4ca29d', '7340c05a-4637-4982-889b-d130ea28dad2', '401552c6-0a78-4d82-9917-9711f88b71c3', 'VIOLET', 'MEN', '', 'COTTON', 'SUMMER', 4, 'BYN57236'),
    ('53e9ec9b-d282-422e-b3cf-5e2183b77e45', 'Hoodies 903', 'Consectetur adipiscing elit.', 68.00, 0.0, 68.00, 'd21c5b20-abfb-4e29-8f3e-2facd9e29b92', 'cb595f2c-cc93-4858-b701-6c8380d63ebe', '401552c6-0a78-4d82-9917-9711f88b71c3', 'MULTI', 'WOMEN', '', 'SYNTHETICS', 'WINTER', 3, 'KJU65182'),
    ('bf57fdc3-c839-485f-bb0f-59f21e231f02', 'Shorts 115', 'Ut enim ad minim veniam.', 29.99, 0.0, 29.99, 'fac4b21e-339d-478d-8bbc-242b1898b89e', null, '401552c6-0a78-4d82-9917-9711f88b71c3', 'YELLOW', 'MEN', '', 'COTTON', 'SUMMER', 5, 'NMB83629'),
    ('ae19c7c5-3d17-4b49-a53a-39a6fdcf7192', 'Jackets 652', 'Duis aute irure dolor in reprehenderit.', 150.00, 0.0, 150.00, '1b324b12-d919-4adf-88b9-238e8721c4bf', null, '401552c6-0a78-4d82-9917-9711f88b71c3', 'BLUE', 'MEN', '', 'LEATHER', 'SPRING', 5, 'OPL46297'),
    ('d5b352d9-967b-4e2d-b3c9-63b13cbfe937', 'Shoes 512', 'Excepteur sint occaecat cupidatat.', 110.00, 0.0, 110.00, 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'b45e1231-9dfa-4f2b-a21e-234c8927ebf9', '401552c6-0a78-4d82-9917-9711f88b71c3', 'BLACK', 'WOMEN', '', 'LEATHER', 'AUTUMN', 5, 'RFT98235');

insert into item_details (item_id, orders_count_total, orders_count_last_month, created_at, creating_employee)
values
    ('a543d3b8-9c84-4e68-913f-bb0c74cf64b1', 150, 30, current_timestamp, 'john.doe@example.com'),
    ('9f6c7d77-35b4-44e8-8b67-1cce4a25ff35', 230, 50, current_timestamp, 'john.doe@example.com'),
    ('ed4a7b67-d54d-48da-9b2c-0337d66da9cb', 85, 20, current_timestamp, 'john.doe@example.com'),
    ('63db07b2-4ae6-4625-9f4f-f1f8a0e69dcb', 350, 90, current_timestamp, 'john.doe@example.com'),
    ('e9f4b6a9-5d8e-49d3-bfc9-e9f47b8d1d1f', 120, 35, current_timestamp, 'john.doe@example.com'),
    ('f302c1f2-5d6a-4f9d-8ef3-1a2e83f20f69', 110, 40, current_timestamp, 'john.doe@example.com'),
    ('1d378cf7-b7ac-485b-8d22-dbf228f5d378', 240, 70, current_timestamp, 'john.doe@example.com'),
    ('72b6c1cf-b6df-4e61-8a1d-b8bc1b41797c', 175, 50, current_timestamp, 'john.doe@example.com'),
    ('7d8c2bf9-5285-4e7c-a8de-0287edc05ba7', 300, 85, current_timestamp, 'john.doe@example.com'),
    ('2346a8be-7489-4c0b-82b5-6f7c647fc97c', 450, 100, current_timestamp, 'john.doe@example.com'),
    ('9a5f6a27-e346-4269-9b0f-7b7f02e67b76', 210, 60, current_timestamp, 'john.doe@example.com'),
    ('34207cbf-5a1f-45a4-929c-b32ff2312a79', 260, 95, current_timestamp, 'john.doe@example.com'),
    ('0f4d8d6c-b2e8-4b4e-84bb-66ff921f9f28', 170, 55, current_timestamp, 'john.doe@example.com'),
    ('52b1bc5e-3159-4229-a172-b1fd12b25dce', 320, 120, current_timestamp, 'john.doe@example.com'),
    ('765ae59e-c853-4b0e-8747-6fa947e56b9b', 190, 65, current_timestamp, 'john.doe@example.com'),
    ('fd295d22-3e71-4f35-8a5b-9097d238b9f3', 120, 30, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('fa946d03-407b-462a-9345-52bcbf222c44', 200, 50, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('41cd85b7-90da-47cf-858d-bb334cfb0a0f', 90, 15, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('f95c4a4d-ecca-4d7a-8aa7-64a8ed8a735a', 140, 40, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('b03f2b29-5c36-4e45-92d5-7f1a8701bc61', 85, 25, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('86d96712-d8f9-4de3-8d8e-8f3b16c7bdf5', 150, 30, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('eb83e8a9-492a-423e-8b20-9f6b23f7e235', 220, 60, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('9c8e057e-0044-4504-9750-0469f29ff143', 110, 20, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('fa9b4a5f-2644-4958-9369-02efc2ab8a38', 95, 10, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('ad3ebcc7-4d71-44f5-b5fd-3b58a9d3ec25', 175, 50, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('28b4e579-bd91-48f9-9b65-937aaab712e6', 160, 35, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('53e9ec9b-d282-422e-b3cf-5e2183b77e45', 130, 40, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('bf57fdc3-c839-485f-bb0f-59f21e231f02', 180, 25, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('ae19c7c5-3d17-4b49-a53a-39a6fdcf7192', 240, 70, CURRENT_TIMESTAMP, 'john.doe@example.com'),
    ('d5b352d9-967b-4e2d-b3c9-63b13cbfe937', 190, 55, CURRENT_TIMESTAMP, 'john.doe@example.com');

insert into unique_item (id, item_id, size, quantity)
values
    ('edc4b5b1-6af8-4128-95bb-2db1f46db0d6', 'a543d3b8-9c84-4e68-913f-bb0c74cf64b1', 'M', 10),
    ('17c4b67f-b93c-46a6-9f9e-2cf846af4a1b', '9f6c7d77-35b4-44e8-8b67-1cce4a25ff35', 'L', 15),
    ('fb2a129e-9e8f-4cf5-8cf7-6a6dcba9afc8', 'ed4a7b67-d54d-48da-9b2c-0337d66da9cb', 'S', 20),
    ('c18f8a9d-73af-4b4e-b9e1-2e4fd96bb19b', '63db07b2-4ae6-4625-9f4f-f1f8a0e69dcb', '42', 25),
    ('b4517a89-cb7b-4ecb-93a6-b120d2bdf139', 'e9f4b6a9-5d8e-49d3-bfc9-e9f47b8d1d1f', 'XL', 8),
    ('98a6ef07-ea8f-4978-82a3-8fb132efc192', 'f302c1f2-5d6a-4f9d-8ef3-1a2e83f20f69', 'L', 12),
    ('3427b659-4737-4c32-a712-5e5e68a64c9f', '1d378cf7-b7ac-485b-8d22-dbf228f5d378', 'M', 20),
    ('bfa2ad09-885a-4e12-8f21-2b0a491aa1fb', '72b6c1cf-b6df-4e61-8a1d-b8bc1b41797c', 'S', 15),
    ('c3d1af8a-931d-4e0b-a8b1-8b7b7f6f13e7', '7d8c2bf9-5285-4e7c-a8de-0287edc05ba7', 'L', 18),
    ('f569af07-6c71-4a92-b827-cbfb90e8e462', '2346a8be-7489-4c0b-82b5-6f7c647fc97c', '42', 22),
    ('f9215e60-5e4f-4732-b0c6-d983e90f1e69', '9a5f6a27-e346-4269-9b0f-7b7f02e67b76', 'XL', 10),
    ('341b55f4-e60e-4f1b-ae6c-ec96ff097d63', '34207cbf-5a1f-45a4-929c-b32ff2312a79', 'M', 16),
    ('12920b6e-3c2d-4bbd-bc5c-b1f2b9d7a10a', '0f4d8d6c-b2e8-4b4e-84bb-66ff921f9f28', '41', 30),
    ('ed12967c-57b2-4cb3-8acb-3dff2cb81d3a', '52b1bc5e-3159-4229-a172-b1fd12b25dce', 'L', 28),
    ('c3b2e687-d487-4b7f-b2eb-c1a92ec927d5', '765ae59e-c853-4b0e-8747-6fa947e56b9b', 'S', 20),
    ('5c2c9f18-5452-4b45-9a4e-57f3d9e1f4cb', 'fd295d22-3e71-4f35-8a5b-9097d238b9f3', 'M', 50),
    ('b57c03a7-8576-4cb3-89d9-1e8d821e857f', 'fa946d03-407b-462a-9345-52bcbf222c44', '42', 20),
    ('6cfe49e2-4641-4702-8b95-20e86ef55cb8', '41cd85b7-90da-47cf-858d-bb334cfb0a0f', 'L', 30),
    ('2d16f150-28cf-466a-bf5a-67b74873a987', 'f95c4a4d-ecca-4d7a-8aa7-64a8ed8a735a', 'S', 25),
    ('771bd519-c1a7-46cb-923a-1ec9fbb27578', 'b03f2b29-5c36-4e45-92d5-7f1a8701bc61', 'XL', 10),
    ('a61c6b2f-c320-47e0-a278-cf27d8e3d7c5', '86d96712-d8f9-4de3-8d8e-8f3b16c7bdf5', 'L', 40),
    ('22edb163-1f1c-4b8c-b1a0-7a9e3e028ae1', 'eb83e8a9-492a-423e-8b20-9f6b23f7e235', 'XL', 25),
    ('e5037d9b-6168-460b-9455-5b6d3542f648', '9c8e057e-0044-4504-9750-0469f29ff143', 'M', 30),
    ('f9d8b13e-6b23-4d54-84c1-04f72a517b9e', 'fa9b4a5f-2644-4958-9369-02efc2ab8a38', 'S', 20),
    ('df56a47d-66ef-43a2-b4de-3b0bfe3e5998', 'ad3ebcc7-4d71-44f5-b5fd-3b58a9d3ec25', '39', 30),
    ('7b0d18e4-1f2d-40a9-a3c6-bf27150ea8cb', '28b4e579-bd91-48f9-9b65-937aaab712e6', 'M', 40),
    ('0d0c1608-71f3-4dd7-9f22-32d4c4bb2268', '53e9ec9b-d282-422e-b3cf-5e2183b77e45', 'L', 35),
    ('b7b43d3f-d15c-4a9b-9e36-689d50e223e5', 'bf57fdc3-c839-485f-bb0f-59f21e231f02', 'XL', 50),
    ('e1f519e4-18bb-4c98-b00e-1d6b90a1327a', 'ae19c7c5-3d17-4b49-a53a-39a6fdcf7192', 'L', 25),
    ('fb26c87d-b6c4-4f23-bfc6-b023827f85c3', 'd5b352d9-967b-4e2d-b3c9-63b13cbfe937', '40', 45);

insert into image (id, item_id, path)
values
    ('50f7d89e-6e7c-4f6d-8f0d-b1c57f50d7fb', 'a543d3b8-9c84-4e68-913f-bb0c74cf64b1', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('21c5a7b1-6b3c-4f6e-b1b2-92bcf3e8d07c', '9f6c7d77-35b4-44e8-8b67-1cce4a25ff35', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('37f6d95e-3b2e-4d1e-a2b7-83b5d70b9f7d', 'ed4a7b67-d54d-48da-9b2c-0337d66da9cb', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('914f34b1-4c12-4f5b-905f-ec5d0f9dbfbd', '63db07b2-4ae6-4625-9f4f-f1f8a0e69dcb', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('74f6d9be-23a3-4f4e-92f2-7c3a32b4f639', 'e9f4b6a9-5d8e-49d3-bfc9-e9f47b8d1d1f', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('bdf4728c-7df9-4a97-a987-2f2c6a71c7a1', 'f302c1f2-5d6a-4f9d-8ef3-1a2e83f20f69', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('a4a89f8e-bf99-4916-a826-b7b4eaf4e027', '1d378cf7-b7ac-485b-8d22-dbf228f5d378', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('b1b95f3b-8a07-4a24-b18f-abc71283c671', '72b6c1cf-b6df-4e61-8a1d-b8bc1b41797c', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('3bcd1f9d-6213-4a9d-8a34-c5c79f512cfe', '7d8c2bf9-5285-4e7c-a8de-0287edc05ba7', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('9e0fb967-c1ab-45f7-9b81-e1a78cb89e8f', '2346a8be-7489-4c0b-82b5-6f7c647fc97c', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('d64baf2e-8562-4341-86c6-5a8265cf2a87', '9a5f6a27-e346-4269-9b0f-7b7f02e67b76', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('e0b2c58d-56d6-481b-8449-013ad9e44e64', '34207cbf-5a1f-45a4-929c-b32ff2312a79', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('6fd7a63f-4938-4d73-b3d4-35f7be9b7b73', '0f4d8d6c-b2e8-4b4e-84bb-66ff921f9f28', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('b029e52b-37a2-47f1-98a7-f7b2d1f541a7', '52b1bc5e-3159-4229-a172-b1fd12b25dce', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('9d6b3d12-68b2-4d93-831f-5db98b7d587f', '765ae59e-c853-4b0e-8747-6fa947e56b9b', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('7f43ed48-5355-4a10-a839-b208d842c1a4', 'fd295d22-3e71-4f35-8a5b-9097d238b9f3', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('0f6baf6d-df17-4ef4-9cf1-5ba6a3b7acdb', 'fa946d03-407b-462a-9345-52bcbf222c44', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('ba8cfa4e-95e7-448e-90d7-f4411d46e446', '41cd85b7-90da-47cf-858d-bb334cfb0a0f', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('1f1c402b-57f7-4f72-82a2-615ac9f6897a', 'f95c4a4d-ecca-4d7a-8aa7-64a8ed8a735a', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('726d762b-8787-4c78-bd5e-501a35b6d3fc', 'b03f2b29-5c36-4e45-92d5-7f1a8701bc61', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('9241d8cf-3db6-4df6-b432-7b50ab53fef4', '86d96712-d8f9-4de3-8d8e-8f3b16c7bdf5', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('1ad0a4cf-ea06-47fd-9466-23d0e93dc7c6', 'eb83e8a9-492a-423e-8b20-9f6b23f7e235', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('5f0e0c4e-16f7-4c25-83e9-14aef0c7d192', '9c8e057e-0044-4504-9750-0469f29ff143', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('a5ad5d8f-3a56-465d-b312-e9630738f340', 'fa9b4a5f-2644-4958-9369-02efc2ab8a38', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('dfd28f36-ef25-44fa-a230-e0265b496f83', 'ad3ebcc7-4d71-44f5-b5fd-3b58a9d3ec25', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('f421fd0a-9937-43d5-8225-0519a2b89f71', '28b4e579-bd91-48f9-9b65-937aaab712e6', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('0b9290c5-e859-4429-b53c-0dcda1747c30', '53e9ec9b-d282-422e-b3cf-5e2183b77e45', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('b29f7f8f-2406-403a-a0a0-9b2b5ad42df6', 'bf57fdc3-c839-485f-bb0f-59f21e231f02', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('fd1c0546-8cb3-4a9d-87a5-8f85dd278fa1', 'ae19c7c5-3d17-4b49-a53a-39a6fdcf7192', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg'),
    ('bb243f9c-c678-4bcf-8b2e-516d32538372', 'd5b352d9-967b-4e2d-b3c9-63b13cbfe937', 'catalog/src/main/resources/images/iStock-1280562095_63a051a70dbff.jpg');