FROM openjdk:8-jre-alpine

ADD ./target/group-message-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx256m", "-jar", "/app/group-message-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081