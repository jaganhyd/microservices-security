#Java runtime
FROM openjdk:17-jdk-slim

#Information around who maintains the image
MAINTAINER jbmicroservices.com

#Add app jar to image
COPY target/accounts_1-0.0.1-SNAPSHOT.jar accounts_1-0.0.1-SNAPSHOT.jar

#start app from docker image
ENTRYPOINT ["java", "-jar", "accounts_1-0.0.1-SNAPSHOT.jar"]