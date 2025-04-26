## Stage 1: Build the app
#FROM gradle:8.13-jdk21 AS builder
#WORKDIR /home/app
#COPY --chown=gradle:gradle . .
#RUN ./gradlew :rest:BootJar --no-daemon
#
## Stage 2: Run the app
#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY --from=builder /home/app/rest/build/libs/rest-*.jar rest.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "rest.jar"]

# Use a base image with Java 21
FROM eclipse-temurin:21-jdk
# Copy the application JAR file
COPY build/libs/rest-*.jar app.jar
# Expose the application port
 # Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
