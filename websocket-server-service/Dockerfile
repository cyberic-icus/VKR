FROM openjdk:8-jre-alpine

ENV TimeZone=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

ADD ./target/websocket-server-service-0.0.1-SNAPSHOT.jar /app/
CMD ["java", "-Xmx256m", "-jar", "/app/websocket-server-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8282