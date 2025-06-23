FROM maven:3.8.6-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
COPY ./commons ./commons/
COPY ./emucomp-grpc-interface ./emucomp-grpc-interface/
COPY ./emucomp-impl ./emucomp-impl/
COPY ./emucomp-api ./emucomp-api/

RUN apt-get update && apt-get install -y wget unzip && rm -rf /var/lib/apt/lists/*

RUN wget https://gitlab.com/emulation-as-a-service/emucon-tools/-/archive/master/emucon-tools-master.zip -O /tmp/emucon-tools-master.zip

RUN unzip /tmp/emucon-tools-master.zip -d /tmp/

RUN ls -la /tmp/

RUN mkdir -p /tmp/oci-tools

RUN if [ ! -d "/tmp/emucon-tools-master" ]; then \
        echo "Error: Extracted directory /tmp/emucon-tools-master not found!"; \
        exit 1; \
    fi && \
    cd /tmp/emucon-tools-master && \
    . ./bootstrap.sh && \
    ./installer/install-oci-tools.sh --destination /tmp/oci-tools

RUN mvn clean package -DskipTests

FROM eclipse-temurin:11-jdk-jammy

COPY --from=build /tmp/oci-tools /usr/local

RUN echo "locales locales/default_environment_locale string en_US.UTF-8" | debconf-set-selections
RUN echo "keyboard-configuration keyboard-configuration/layoutcode string us" | debconf-set-selections
RUN DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y xpra socat vde2 qemu-utils qemu-system ntfs-3g util-linux sudo unzip
RUN rm -rf /var/lib/apt/lists/*

RUN mkdir -p /linapple-pie /minivmac /usr/local/bin

WORKDIR /app
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus-run.jar ./quarkus-run.jar
COPY --from=build /app/emucomp-api/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/emucomp-api/target/quarkus-app/app/ ./app/
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus/ ./quarkus/

EXPOSE 8080
CMD ["java", "-jar", "./quarkus-run.jar"]
