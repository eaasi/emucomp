FROM maven:3.8.6-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
COPY ./commons ./commons/
COPY ./emucomp-grpc-interface ./emucomp-grpc-interface/
COPY ./emucomp-impl ./emucomp-impl/
COPY ./emucomp-api ./emucomp-api/

RUN mvn clean package -DskipTests

FROM ubuntu:22.04 AS tools_builder

WORKDIR /tmp/tools-build
COPY build-emucon-tools.sh .
RUN chmod +x build-emucon-tools.sh

RUN ./build-emucon-tools.sh

FROM eclipse-temurin:11-jdk-jammy

RUN echo "locales locales/default_environment_locale string en_US.UTF-8" | debconf-set-selections && \
    echo "keyboard-configuration keyboard-configuration/layoutcode string us" | debconf-set-selections && \
    DEBIAN_FRONTEND=noninteractive apt-get update && apt-get install -y \
    xpra socat vde2 qemu-utils qemu-system ntfs-3g util-linux sudo unzip \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /linapple-pie /minivmac /usr/local/bin
COPY --from=tools_builder /tmp/tools-build/emucon-output /usr/local/bin/

WORKDIR /app
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus-run.jar ./quarkus-run.jar
COPY --from=build /app/emucomp-api/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/emucomp-api/target/quarkus-app/app/ ./app/
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080
CMD ["java", "-jar", "./quarkus-run.jar"]