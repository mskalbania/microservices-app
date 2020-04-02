FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd
RUN mkdir -p /usr/local/eurekaserver
ADD  @project.build.finalName@.jar /usr/local/eurekaserver/
CMD echo "Starting Eureka Server"
CMD java -jar /usr/local/eurekaserver/@project.build.finalName@.jar