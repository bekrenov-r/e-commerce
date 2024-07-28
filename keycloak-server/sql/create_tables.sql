drop table if exists users;
create table users(
    username text primary key,
    password text
);

drop table if exists roles;
create table roles(
    username text,
    role text
);

insert into users values
                      ('john.doe@example.com', '$2a$12$YmIpJldQP6XHAxV5IBsI..oPu95XQaKEImM0OO89rroc.5rMBtGY.'), --password
                      ('jane.doe@example.com', '$2a$12$YmIpJldQP6XHAxV5IBsI..oPu95XQaKEImM0OO89rroc.5rMBtGY.');

insert into roles values
                            ('john.doe@example.com', 'EMPLOYEE'),
                            ('john.doe@example.com', 'ADMIN'),
                            ('jane.doe@example.com', 'CUSTOMER');