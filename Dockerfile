# Use a base image with Java and Maven installed
FROM maven:3.8.4-openjdk-17-slim AS build

# Set the working directory
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

# Copy the settings.xml file to the Maven configuration directory
COPY src/main/resources/settings.xml /root/.m2/settings.xml

# Copy the pom.xml file to the working directory
COPY pom.xml .

# Download the project dependencies
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml

# Verify the settings.xml file exists and print its content
RUN ls -la /root/.m2/ && cat /root/.m2/settings.xml

# Copy the source code to the working directory
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a lightweight base image with Java installed
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

ARG SPRING_PROFILE
ARG SPRING_PORT
ARG ALLOWED_API_KEY
ARG HOC_MONGO_URI
ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

ENV SPRING_PROFILE=${SPRING_PROFILE}
ENV SPRING_PORT=${SPRING_PORT}
ENV ALLOWED_API_KEY=${ALLOWED_API_KEY}
ENV MONGO_URI=${MONGO_URI}
ENV GITHUB_USERNAME=${GITHUB_USERNAME}
ENV GITHUB_TOKEN=${GITHUB_TOKEN}

# Copy the settings.xml file to the Maven configuration directory
COPY src/main/resources/settings.xml /root/.m2/settings.xml

# Verify the settings.xml file exists and print its content
RUN ls -la /root/.m2/ && cat /root/.m2/settings.xml

# Copy the JAR file from the build stage to the current directory
COPY --from=build /app/target/*.jar ./heralds-of-chaos.jar

# Ensure the JAR file has the correct permissions
RUN chmod 777 /app/heralds-of-chaos.jar

# Expose the port on which the application will run
EXPOSE 8003

# Set the command to run the application
CMD ["java", "-jar", "heralds-of-chaos.jar"]
