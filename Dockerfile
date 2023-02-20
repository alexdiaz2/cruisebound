FROM openjdk:17-jdk-slim

MAINTAINER cruisebound.com

ENV JAVA_OPTS " -Xms512 -Xmx512m"

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} assessment.jar

ENTRYPOINT ["java", "-jar", "/assessment.jar"]