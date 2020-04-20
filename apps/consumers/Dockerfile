FROM adoptopenjdk:11-jre-hotspot
MAINTAINER ogo
ADD ./build/libs/consumers-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-Xmx200m", "-jar", "/app.jar"]
