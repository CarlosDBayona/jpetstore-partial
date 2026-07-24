# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper files and pom.xml
COPY .mvn .mvn
COPY mvnw mvnw.cmd pom.xml ./

# Fix line endings (CRLF -> LF) and grant execution permission to mvnw
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

# Copy source code and build package directly (fast build)
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built JAR artifact
COPY --from=builder /app/target/jpetstore-partial-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
