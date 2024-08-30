drop schema public cascade;
create schema public;

create table users(
    username text primary key,
    password text,
    enabled boolean,
    first_name text
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
                      ('john.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true, 'John'), --password
                      ('jane.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true, 'Jane');

insert into roles values
                            ('john.doe@example.com', 'EMPLOYEE'),
                            ('john.doe@example.com', 'ADMIN'),
                            ('jane.doe@example.com', 'CUSTOMER');