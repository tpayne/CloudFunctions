# This is a docker make file for building this Maven app

FROM maven:3.8-jdk-11 as imagebuilder

WORKDIR /app
COPY pom.xml .
COPY AdvancedHttpFunction ./AdvancedHttpFunction
COPY MySQLFunction ./MySQLFunction
COPY SendHttpRequest ./SendHttpRequest
COPY SimpleHttpFunction ./SimpleHttpFunction

RUN mvn package 
