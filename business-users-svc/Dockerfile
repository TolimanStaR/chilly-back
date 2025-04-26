FROM maven:3.8.5-openjdk-17 AS build
COPY pom.xml /app/pom.xml

COPY common/pom.xml /app/common/pom.xml
COPY common/src /app/common/src
RUN mvn -f /app/common/pom.xml clean install

COPY business-users-svc/pom.xml /app/module/pom.xml
RUN mvn -f /app/module/pom.xml verify --fail-never

COPY business-users-svc/src /app/module/src
RUN mvn -f /app/module/pom.xml clean package -DskipTests -e -X

FROM openjdk:17.0.1
LABEL authors="musht"
COPY --from=build /app/module/target/*.jar /chilly/business_users_svc.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/chilly/business_users_svc.jar"]