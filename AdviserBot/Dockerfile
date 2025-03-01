FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/AdviserBot.jar AdviserBot.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "AdviserBot.jar"]