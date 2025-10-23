# Use Java 22 slim image
FROM openjdk:22-jdk-slim

# Set working directory
WORKDIR /app

# Copy the fat JAR
COPY target/user-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8081

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
