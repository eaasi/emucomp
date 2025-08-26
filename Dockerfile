FROM ubuntu:18.04 AS main

RUN apt-get update && apt-get install -y wget unzip sudo && rm -rf /var/lib/apt/lists/*

RUN wget https://gitlab.com/emulation-as-a-service/emucon-tools/-/archive/master/emucon-tools-master.zip -O /tmp/emucon-tools-master.zip

RUN unzip /tmp/emucon-tools-master.zip -d /tmp/ && \
    ls -la /tmp/ && \
    ls -la /tmp/emucon-tools-master || true

RUN addgroup --gid 1000 bwfla
RUN addgroup fuse
RUN useradd -ms /bin/bash --uid 1000 --gid bwfla bwfla && for grp in fuse disk audio plugdev; do adduser bwfla $grp; done

RUN chown bwfla:bwfla /home/bwfla

USER bwfla

RUN mkdir -p /home/bwfla/.bwFLA  \
/home/bwfla/demo-ui              \
/home/bwfla/image-archive        \
/home/bwfla/log                  \
/home/bwfla/objects              \
/home/bwfla/server-data		 \
/home/bwfla/import 		 \
/home/bwfla/export		 \
/home/bwfla/defaults

RUN cd /tmp/emucon-tools-master && \
    . ./bootstrap.sh && \
    . ./install.sh --destination /usr/local -u bwfla && \
    ./installer/install-oci-tools.sh --destination /tmp/oci-tools && \
    ./installer/install-deps.sh

FROM maven:3.8.6-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
COPY ./commons ./commons/
COPY ./emucomp-grpc-interface ./emucomp-grpc-interface/
COPY ./emucomp-impl ./emucomp-impl/
COPY ./emucomp-api ./emucomp-api/

RUN mvn clean package -DskipTests

FROM eclipse-temurin:11-jdk-jammy

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

COPY --from=main /tmp/oci-tools /usr/local

EXPOSE 8080
CMD ["java", "-jar", "./quarkus-run.jar"]
