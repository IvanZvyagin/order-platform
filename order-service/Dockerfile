# Этап сборки
FROM gradle:8.5-jdk17 AS builder
WORKDIR /app

# Копируем корневые файлы Gradle (только существующие)
COPY settings.gradle /app/

# Если есть корневой build.gradle, раскомментируйте следующую строку
COPY build.gradle /app/

# Если есть gradle.properties, раскомментируйте
# COPY gradle.properties /app/

# Если есть папка gradle/wrapper, раскомментируйте
# COPY gradle/ /app/gradle/

# Копируем зависимые модули
COPY common-libs/ /app/common-libs/
COPY order-service/ /app/order-service/

# Собираем только order-service (без тестов)
RUN gradle :order-service:bootJar --no-daemon -x test

# Финальный образ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/order-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]