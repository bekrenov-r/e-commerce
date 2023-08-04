FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/items-data-0.0.1.jar items-data.jar
ENTRYPOINT ["java", "-jar", "/items-data.jar"]