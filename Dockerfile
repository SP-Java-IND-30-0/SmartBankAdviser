FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/SmartBankAdviser.jar SmartBankAdviser.jar

COPY src/main/resources/db/database.mv.db /data/database.mv.db

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "SmartBankAdviser.jar"]