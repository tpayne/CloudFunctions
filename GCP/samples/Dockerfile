# This is a docker make file for building this Maven app

FROM maven:3.9.9-eclipse-temurin-21-jammy as imagebuilder
RUN apt-get update

WORKDIR /build
COPY pom.xml .
COPY AdvancedHttpFunction ./AdvancedHttpFunction
COPY NoSQLSpanner ./NoSQLSpanner
COPY SendHttpRequest ./SendHttpRequest
COPY SimpleHttpFunction ./SimpleHttpFunction

RUN mvn -q clean package 

FROM us-central1-docker.pkg.dev/serverless-runtimes/google-22-full/runtimes/java21 as runner

WORKDIR /app
COPY --from=imagebuilder /build/AdvancedHttpFunction/target/*.jar ./AdvancedHttpFunction/
COPY --from=imagebuilder /build/NoSQLSpanner/target/*.jar ./NoSQLSpanner/
COPY --from=imagebuilder /build/SimpleHttpFunction/target/*.jar ./SimpleHttpFunction/
COPY --from=imagebuilder /build/SendHttpRequest/target/*.jar ./SendHttpRequest/
