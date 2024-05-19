FROM openjdk:8-jre-alpine

ADD ./target/offline-message-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/offline-message-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081