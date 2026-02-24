# Use official Java 17 image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy everything
COPY . .

# Build the project
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run jar
CMD ["sh", "-c", "java -jar target/*.jar"]