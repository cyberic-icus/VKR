FROM openjdk:8-jre-alpine

ENV TimeZone=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

ADD ./target/message-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx256m", "-jar", "/app/message-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081