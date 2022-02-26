# This is a docker make file for building this Maven app

FROM maven:3.8-jdk-11 as imagebuilder
RUN apt-get update

WORKDIR /app
COPY pom.xml .
COPY AdvancedHttpFunction ./AdvancedHttpFunction
COPY NoSQLSpanner ./NoSQLSpanner
COPY SendHttpRequest ./SendHttpRequest
COPY SimpleHttpFunction ./SimpleHttpFunction

RUN mvn package 
