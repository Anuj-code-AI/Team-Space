FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["sh", "-c", "java -Dserver.port=$PORT -jar target/*.jar"]