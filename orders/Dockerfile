FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/orders-data-0.0.1.jar orders-data.jar
ENTRYPOINT ["java", "-jar", "/orders-data.jar"]