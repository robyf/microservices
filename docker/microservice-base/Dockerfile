FROM openjdk:11-jdk
ADD start-service.sh /usr/local/bin
ENV JAVA_OPTS=-Xmx256m
EXPOSE 8080 8081
CMD [ "start-service.sh" ]
