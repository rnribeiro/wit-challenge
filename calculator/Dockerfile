## Stage 1: Build the app
#FROM gradle:8.13-jdk21 AS builder
#WORKDIR /home/app
#COPY --chown=gradle:gradle . .
#RUN ./gradlew :calculator:BootJar --no-daemon
#
## Stage 2: Run the app
#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY --from=builder /home/app/calculator/build/libs/calculator-*.jar calculator.jar
#ENTRYPOINT ["java", "-jar", "calculator.jar"]

# Use a base image with Java 21
FROM eclipse-temurin:21-jdk
# Copy the application JAR file
COPY build/libs/calculator-*.jar app.jar
# Expose the application port
 # Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]