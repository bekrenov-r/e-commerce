create database items_data
    with
    owner = postgres
    template = template0
    encoding = 'UTF8'
    LC_COLLATE = 'pl_PL.UTF-8'
    LC_CTYPE = 'pl_PL.UTF-8'
    tablespace = pg_default
    connection limit = -1;

\c items_data

