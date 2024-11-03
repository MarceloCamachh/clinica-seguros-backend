# Use the official gradle image to create a build artifact.
# https://hub.docker.com/_/gradle
FROM gradle:jdk21 AS builder

# Copy local code to the container image.
WORKDIR /app
COPY build.gradle .
COPY gradlew .
COPY gradle gradle
COPY src src

# Make sure gradlew has execute permissions
RUN chmod +x ./gradlew

# Build a release artifact.
RUN ./gradlew bootJar

RUN ls -la /app/build/libs/

FROM sapmachine:21.0.1

ARG JAR_FILE=/app/build/libs/clinic-system-0.1.0.jar

# Copy the jar to the production image from the builder stage.
COPY --from=builder ${JAR_FILE} /app/clinic-system.jar

# Run the web service on container startup.
CMD ["java", "-jar", "/app/clinic-system.jar"]

# Expose the application on port 8080
EXPOSE 8080