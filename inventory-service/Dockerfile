FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY settings.gradle /app/
COPY build.gradle /app/
#COPY gradle.properties /app/
#COPY gradle/ /app/gradle/

COPY common-libs/ /app/common-libs/
COPY inventory-service/ /app/inventory-service/

RUN gradle :inventory-service:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/inventory-service/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]