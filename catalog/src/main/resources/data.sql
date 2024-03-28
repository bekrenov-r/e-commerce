INSERT INTO category(name, img_name, enum_value, id) VALUES
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

INSERT INTO subcategory(name, category_id, id) VALUES
                            ('JEANS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '27e1d04b-1b23-4b29-aabd-228b98d7ef1f'),
                            ('JOGGERS', 'a719d81d-e219-4e2e-b2d4-f5432198e783', '081f2b12-9b23-42e8-b1ab-28db7821cbef'),
                            ('SPORT', 'a719d81d-e219-4e2e-b2d4-f5432198e783', 'f12c3b45-2b19-48e2-b9ad-2987b1234faf'),
                            ('SANDALS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'b45e1231-9dfa-4f2b-a21e-234c8927ebf9'),
                            ('SNEAKERS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', '3e19c2b2-12f9-42ab-b42e-278db421cef2'),
                            ('BOOTS', 'e09c3121-b2df-4f32-a87e-218b9127e00f', 'a12b34df-b8df-42ab-b9ba-2837b1432faf');

insert into size(size_value, type, id) values  ('XS', 'CLOTHES', random_uuid()), ('S', 'CLOTHES', random_uuid()), ('M', 'CLOTHES', random_uuid()), ('L', 'CLOTHES', random_uuid()), ('XL', 'CLOTHES', random_uuid()), ('2XL', 'CLOTHES', random_uuid()), ('3XL', 'CLOTHES', random_uuid()), ('4XL', 'CLOTHES', random_uuid()),
                         ('36', 'SHOES', random_uuid()), ('37', 'SHOES', random_uuid()), ('38', 'SHOES', random_uuid()), ('39', 'SHOES', random_uuid()), ('40', 'SHOES', random_uuid()), ('41', 'SHOES', random_uuid()), ('42', 'SHOES', random_uuid()), ('43', 'SHOES', random_uuid()), ('44', 'SHOES', random_uuid()), ('45', 'SHOES', random_uuid());

insert into brand(name, id) values
                      ('SoleCraft Co.', random_uuid()),
                      ('LegLoom', random_uuid()),
                      ('CozyKnits', random_uuid()),
                      ('Headline', random_uuid()),
                      ('ArtistryThreads', random_uuid());