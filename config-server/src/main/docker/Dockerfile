FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/configserver
ADD  @project.build.finalName@.jar /usr/local/configserver/
CMD echo "Starting License Server"
CMD java -jar /usr/local/configserver/config-server-0.0.1-SNAPSHOT.jar