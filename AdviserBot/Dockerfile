FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/AdviserBot-1.0.0.jar AdviserBot.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "AdviserBot.jar"]