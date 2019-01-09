FROM java:8

MAINTAINER linyi

EXPOSE 8888

VOLUME /tmp

RUN mkdir /app

RUN mkdir /app/logs

ADD target/product-collector-0.0.1-SNAPSHOT.jar /app/product-collector-0.0.1-SNAPSHOT.jar

RUN bash -c 'touch /app/product-collector-0.0.1-SNAPSHOT.jar'

# ENTRYPOINT java -jar /app/product-collector-0.0.1-SNAPSHOT.jar