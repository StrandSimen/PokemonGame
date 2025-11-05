# Use full OpenJDK 22 as base image
FROM openjdk:22-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file into the container (rename for flexibility)
COPY target/loaddb-0.0.1-SNAPSHOT.jar app.jar

# Run the application with ENTRYPOINT for fixed execution
ENTRYPOINT ["java", "-jar", "app.jar"]