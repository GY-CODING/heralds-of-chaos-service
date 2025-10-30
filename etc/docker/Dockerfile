# syntax=docker/dockerfile:1
FROM eclipse-temurin:23-jdk AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app

ARG SPRING_PROFILE
ARG SPRING_PORT
ARG ALLOWED_API_KEY
ARG MONGO_URI
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

ENV SPRING_PROFILE=${SPRING_PROFILE}
ENV SPRING_PORT=${SPRING_PORT}
ENV ALLOWED_API_KEY=${ALLOWED_API_KEY}
ENV MONGO_URI=${MONGO_URI}
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

# Rutas relativas al contexto de build (raíz del proyecto)
COPY src/main/resources/settings.xml /root/.m2/settings.xml
COPY pom.xml .
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml

RUN ls -la /root/.m2/ && cat /root/.m2/settings.xml

COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:23-jdk

WORKDIR /app

ARG SPRING_PROFILE
ARG SPRING_PORT
ARG ALLOWED_API_KEY
ARG MONGO_URI
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

ENV SPRING_PROFILE=${SPRING_PROFILE}
ENV SPRING_PORT=${SPRING_PORT}
ENV ALLOWED_API_KEY=${ALLOWED_API_KEY}
ENV MONGO_URI=${MONGO_URI}
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

COPY src/main/resources/settings.xml /root/.m2/settings.xml
RUN ls -la /root/.m2/ && cat /root/.m2/settings.xml

COPY --from=build /app/target/*.jar ./heralds-of-chaos.jar
RUN chmod 755 ./heralds-of-chaos.jar

EXPOSE 8003

CMD ["java", "-jar", "heralds-of-chaos.jar"]