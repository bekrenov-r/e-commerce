FROM openjdk:24-oraclelinux8
COPY /sql/postgres-passwd /run/secrets/postgres-passwd
COPY ./target/*.jar app.jar
EXPOSE 8000