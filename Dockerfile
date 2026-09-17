FROM eclipse-temurin:21-jdk AS builder

LABEL org.opencontainers.image.source="https://github.com/jinnasaiteja/securebank-banking-service"
LABEL org.opencontainers.image.description="SecureBank Spring Boot banking service"

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
