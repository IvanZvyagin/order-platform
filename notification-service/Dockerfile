FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

COPY settings.gradle /app/
COPY build.gradle /app/
COPY common-libs/ /app/common-libs/
COPY notification-service/ /app/notification-service/

RUN gradle :notification-service:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/notification-service/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]