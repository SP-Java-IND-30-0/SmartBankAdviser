FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/SmartBankAdviser-1.0.0.jar SmartBankAdviser.jar

COPY src/main/resources/db/transaction.mv.db /data/transaction.mv.db

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "SmartBankAdviser.jar"]