FROM maven:3.8.5-openjdk-17 AS build
COPY pom.xml /app/pom.xml
COPY discovery-server/pom.xml /app/module/pom.xml
RUN mvn -f /app/module/pom.xml verify --fail-never

COPY discovery-server/src /app/module/src
RUN mvn -f /app/module/pom.xml clean package -DskipTests

FROM openjdk:17.0.1
LABEL authors="musht"
COPY --from=build /app/module/target/*.jar /chilly/discovery_server.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","/chilly/discovery_server.jar"]