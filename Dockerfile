# Этап сборки
FROM maven:3.8.4-openjdk-17-slim AS builder

# Установка рабочей директории
WORKDIR /app

# Копирование файлов проекта
COPY . .

# Сборка проекта
RUN mvn clean package -DskipTests

# Этап запуска
FROM openjdk:17-slim

# Установка рабочей директории
WORKDIR /app

# Копирование собранного JAR-файла из этапа сборки
COPY --from=builder /app/emucomp-impl/target/*.jar app.jar

# Установка переменных окружения
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Открытие порта (замените на нужный порт вашего приложения)
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 