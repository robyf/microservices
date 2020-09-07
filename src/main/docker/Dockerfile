FROM openjdk:11-jdk
ADD libs/app.jar app.jar
EXPOSE 8080 8081
CMD [ "java", "-jar", "app.jar", "-Xmx256m" ]
