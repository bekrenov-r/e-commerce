drop schema public cascade;
create schema public;

create table users(
    username text primary key,
    password text,
    enabled boolean
);

create table roles(
    username text,
    role text
);

create table activation_token(
    username text,
    token text
);

insert into users values
                      ('john.doe@example.com', '$2a$12$YmIpJldQP6XHAxV5IBsI..oPu95XQaKEImM0OO89rroc.5rMBtGY.', true), --password
                      ('jane.doe@example.com', '$2a$12$YmIpJldQP6XHAxV5IBsI..oPu95XQaKEImM0OO89rroc.5rMBtGY.', false);

insert into roles values
                            ('john.doe@example.com', 'EMPLOYEE'),
                            ('john.doe@example.com', 'ADMIN'),
                            ('jane.doe@example.com', 'CUSTOMER');