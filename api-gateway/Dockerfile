FROM maven:3.8.5-openjdk-17 AS build
COPY pom.xml /app/pom.xml

COPY common-test/pom.xml /app/common-test/pom.xml
COPY common-test/src /app/common-test/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f /app/common-test/pom.xml clean install

COPY api-gateway/pom.xml /app/module/pom.xml
COPY api-gateway/src /app/module/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f /app/module/pom.xml clean package -DskipTests

FROM openjdk:17.0.1
LABEL authors="musht"
COPY --from=build /app/module/target/*.jar /chilly/api_gateway.jar
COPY keystore.p12 /etc/certs/keystore.p12

EXPOSE 8080
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=docker","/chilly/api_gateway.jar"]