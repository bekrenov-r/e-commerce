FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/api-gateway-0.0.1.jar api-gateway.jar
ENTRYPOINT ["java", "-jar", "/api-gateway.jar"]