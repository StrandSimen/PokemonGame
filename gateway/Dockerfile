# Use OpenJDK 22 slim image
FROM openjdk:22-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file from the Maven target directory
COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (Gateway's port)
EXPOSE 8100

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]