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
                      ('john.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true), --password
                      ('jane.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true);

insert into roles values
                            ('john.doe@example.com', 'EMPLOYEE'),
                            ('john.doe@example.com', 'ADMIN'),
                            ('jane.doe@example.com', 'CUSTOMER');