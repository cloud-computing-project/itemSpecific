FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./api/target/itemSpecific-api-1.0.0-SNAPSHOT.jar /app

EXPOSE 8084

CMD ["java", "-jar", "itemSpecific-api-1.0.0-SNAPSHOT.jar"]