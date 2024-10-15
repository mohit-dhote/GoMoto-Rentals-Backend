# Step 5: Use a minimal JRE image for the final container
FROM openjdk:21-jdk-slim

# Step 6: Set working directory inside the final container
WORKDIR /app

# Step 7: Copy the built JAR from the builder container
COPY ./target/*.jar app.jar

# Step 8: Expose the application port (default Spring Boot port)
EXPOSE 9090

# Step 9: Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]