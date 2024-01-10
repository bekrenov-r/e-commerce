FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/spring-cloud-config-server-0.0.1.jar spring-cloud-config-server.jar
ENTRYPOINT ["java", "-jar", "/spring-cloud-config-server.jar"]