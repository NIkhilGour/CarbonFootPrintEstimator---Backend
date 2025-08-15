# Step 1: Build stage
FROM maven:3.9.6-eclipse-temurin-22 AS build

WORKDIR /app

# Copy source code
COPY .. .

# Build the project (skip tests for faster build)
RUN mvn clean package -DskipTests

# Step 2: Run stage
FROM openjdk:22-jdk

WORKDIR /app




# ✅ Copy the built JAR from the build stage
COPY --from=build /app/target/CarbonPrintAnalyzer.jar analyzer.jar

# Run the app
ENTRYPOINT ["java", "-jar", "analyzer.jar"]
