FROM java:8

MAINTAINER linyi

EXPOSE 8888

VOLUME /tmp

RUN mkdir /app

RUN mkdir /app/logs

ADD target/product-collector-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT java  -Dspring.profiles.active=prod -jar /app/app.jar