FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/naming-server-0.0.1.jar naming-server.jar
ENTRYPOINT ["java", "-jar", "/naming-server.jar"]