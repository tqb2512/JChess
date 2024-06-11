# Stage 1: Build the application
FROM gradle:8.7.0-jdk-21-and-22-graal-jammy AS build
WORKDIR /app
COPY . /app/
RUN gradle build --no-daemon

# Stage 2: Run the application
FROM openjdk:17-jdk-alpine3.14
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
