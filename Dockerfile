FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .

COPY ./commons ./commons
COPY ./emucomp-grpc-interface ./emucomp-grpc-interface
COPY ./emucomp-impl ./emucomp-impl
COPY ./emucomp-api ./emucomp-api

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/target/quarkus-app/quarkus-run.jar ./quarkus-run.jar
COPY --from=build /app/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/target/quarkus-app/app/ ./app/
COPY --from=build /app/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080

CMD ["java", "-jar", "./emucomp-api.jar"]
