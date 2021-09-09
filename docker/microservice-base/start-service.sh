#!/bin/bash
java $JAVA_OPTS -Dspring.profiles.active=$PROFILE -jar /app.jar
