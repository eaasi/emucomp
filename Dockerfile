FROM maven:3.8.6-eclipse-temurin-17 as build

WORKDIR /app

COPY commons/pom.xml commons/
COPY emucomp-grpc-interface/pom.xml emucomp-grpc-interface/
COPY emucomp-impl/pom.xml emucomp-impl/
COPY emucomp-api/pom.xml emucomp-api/

RUN mvn dependency:go-offline -B

COPY ./commons ./commons
COPY ./emucomp-grpc-interface ./emucomp-grpc-interface
COPY ./emucomp-impl ./emucomp-impl
COPY ./emucomp-api ./emucomp-api

RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /app/commons/target/commons-*.jar ./commons.jar
COPY --from=build /app/emucomp-grpc-interface/target/emucomp-grpc-interface-*.jar ./emucomp-grpc-interface.jar
COPY --from=build /app/emucomp-impl/target/emucomp-impl-*.jar ./emucomp-impl.jar
COPY --from=build /app/emucomp-api/target/emucomp-api-*.jar ./emucomp-api.jar

EXPOSE 8080

CMD ["java", "-jar", "./emucomp-api.jar"]
