FROM openjdk:21-jdk

LABEL version="1.0", description="Wikimedia Changes Producer Application"
WORKDIR /app

COPY WikimediaChangeHandler.java ./WikimediaChangeHandler.java
COPY WikimediaChangesProducer.java ./WikimediaChangesProducer.java

RUN javac WikimediaChangesProducer.java

CMD ["java","WikimediaChangesProducer"]