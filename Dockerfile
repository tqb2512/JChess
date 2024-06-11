# Stage 1: Build the application
FROM maven:3.6.3-jdk-11 AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn clean package

# Stage 2: Run the application
FROM openjdk:11-jre-slim
WORKDIR /home/app
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]