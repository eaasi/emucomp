FROM maven:3.8.6-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
COPY ./commons ./commons/
COPY ./emucomp-grpc-interface ./emucomp-grpc-interface/
COPY ./emucomp-impl ./emucomp-impl/
COPY ./emucomp-api ./emucomp-api/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:11-jdk-jammy


RUN apt-get update && apt-get install -y \
    xpra \
    socat \
    vde2 \
    qemu \
    qemu-system-x86 \
    qemu-utils \
    fuse \
    primus \
    ntfs-3g \
    dosbox \
    fs-uae \
    mono-complete \
    sudo \
    gawk \
    coreutils \
    util-linux \
    fuse \
    dosfstools \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /linapple-pie /minivmac /usr/local/bin

WORKDIR /app

COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus-run.jar ./quarkus-run.jar
COPY --from=build /app/emucomp-api/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/emucomp-api/target/quarkus-app/app/ ./app/
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080

CMD ["java", "-jar", "./quarkus-run.jar"]
