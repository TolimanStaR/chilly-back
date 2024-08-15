FROM maven:3.8.5-openjdk-17 AS build
COPY pom.xml /app/pom.xml
COPY api-gateway/pom.xml /app/module/pom.xml
RUN mvn -f /app/module/pom.xml verify --fail-never

COPY api-gateway/src /app/module/src
RUN mvn -f /app/module/pom.xml clean package -DskipTests

FROM openjdk:17.0.1
LABEL authors="musht"
COPY --from=build /app/module/target/*.jar /chilly/api_gateway.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker","/chilly/api_gateway.jar"]