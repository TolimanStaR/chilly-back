FROM maven:3.8.5-openjdk-17 AS build
COPY pom.xml /app/pom.xml

COPY common/pom.xml /app/common/pom.xml
COPY common/src /app/common/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f /app/common/pom.xml clean install

COPY common-test/pom.xml /app/common-test/pom.xml
COPY common-test/src /app/common-test/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f /app/common-test/pom.xml clean install

COPY main-svc/pom.xml /app/module/pom.xml
COPY main-svc/src /app/module/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f /app/module/pom.xml clean package -DskipTests

FROM openjdk:17.0.1
LABEL authors="musht"
COPY --from=build /app/module/target/*.jar /chilly/main_svc.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/chilly/main_svc.jar"]