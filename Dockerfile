FROM gradle:8.14.3-jdk21-noble AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build bootjar

FROM eclipse-temurin:21-jdk-alpine AS run

WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/scriptutils-*-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java","-jar","./app.jar"]

EXPOSE 8080
