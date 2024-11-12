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

create table token(
    username text,
    value text,
    type text
);

insert into users(username, password, enabled, first_name)
values ('john.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true, 'John'), --password
       ('jane.doe@example.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true, 'Jane'),
       ('bekrenov.s@gmail.com', 'XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=', true, 'Mathew');

insert into roles(username, role)
values ('john.doe@example.com', 'EMPLOYEE'),
       ('john.doe@example.com', 'ADMIN'),
       ('jane.doe@example.com', 'CUSTOMER'),
       ('bekrenov.s@gmail.com', 'CUSTOMER');