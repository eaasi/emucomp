FROM maven:3.8.6-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
COPY ./commons/pom.xml ./commons/
COPY ./emucomp-grpc-interface/pom.xml ./emucomp-grpc-interface/
COPY ./emucomp-impl/pom.xml ./emucomp-impl/
COPY ./emucomp-api/pom.xml ./emucomp-api/

RUN mvn dependency:go-offline && \
    mvn clean install -pl commons -am -DskipTests && \
    mvn clean install -pl emucomp-grpc-interface -am -DskipTests && \
    mvn clean install -pl emucomp-impl -am -DskipTests && \
    mvn clean package -pl emucomp-api -DskipTests

FROM eclipse-temurin:11-jdk-jammy

WORKDIR /app

COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus-run.jar ./quarkus-run.jar
COPY --from=build /app/emucomp-api/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/emucomp-api/target/quarkus-app/app/ ./app/
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080

CMD ["java", "-jar", "./quarkus-run.jar"]
