FROM openjdk:8u151-jre-alpine

RUN mkdir -p /app
ADD ./target/bee-web-0.0.1-SNAPSHOT.jar /app

WORKDIR /app
ENTRYPOINT ["sh", "-c", "java -jar bee-web-0.0.1-SNAPSHOT.jar"]