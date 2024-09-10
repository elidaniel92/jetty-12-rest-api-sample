# Use an official Maven image as the base image for building
FROM maven:3.9.9-amazoncorretto-17-debian AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the source code into the container
COPY pom.xml .
COPY src ./src

# Package the application (run 'mvn package' inside the container)
RUN mvn clean package

# Use an official OpenJDK runtime as the base image for running the application
FROM openjdk:17-jdk-alpine

# Set the working directory for the runtime
WORKDIR /app

# Copy the libs
COPY --from=build /app/target/lib ./lib
# Copy the built JAR file from the 'build' stage
COPY --from=build /app/target/jetty-12-rest-api-sample-1.0-SNAPSHOT.jar ./jetty-12-rest-api-sample.jar

# Expose the application's port (if needed)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "jetty-12-rest-api-sample.jar"]
