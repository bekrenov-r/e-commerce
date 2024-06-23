FROM postgres
WORKDIR docker-entrypoint-initdb.d
COPY create_db.sql create_db.sql
COPY create_tables.sql create_tables.sql
COPY postgres-passwd /run/secrets/postgres-passwd
EXPOSE 5432