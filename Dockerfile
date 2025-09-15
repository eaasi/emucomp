FROM ubuntu:20.04 AS main

RUN apt-get update && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
    wget unzip sudo coreutils xpra python3-pip sed runc && rm -rf /var/lib/apt/lists/*

RUN wget https://gitlab.com/emulation-as-a-service/emucon-tools/-/archive/master/emucon-tools-master.zip -O /tmp/emucon-tools-master.zip

RUN unzip /tmp/emucon-tools-master.zip -d /tmp/ && \
    cp -vrp /tmp/emucon-tools-master/runtime/bin /usr/local && \
    cp -vrp /tmp/emucon-tools-master/runtime/lib /usr/local && \
    cp -vrp /tmp/emucon-tools-master/runtime/share /usr/local &&  \
    install -v -m 'a=rx' /tmp/emucon-tools-master/builder/commands/layer/layers/base/scripts/emucon-init /usr/bin/emucon-init &&  \
    chmod +x /usr/bin/emucon-init && \
    sed -i '/--html=off/a\\t    --speaker=on \\\n\t    --pulseaudio=yes \\\n\t  \
      --pulseaudio-server=unix:\/tmp\/${display#:}\/pulse-socket \\\n\t   \
     --speaker-codec=opus \\\n\t    --audio-source=pulse' /usr/bin/emucon-init && \
     . /tmp/emucon-tools-master/bootstrap.sh

RUN sed -i '$s/$/ -nolisten local/' /etc/xpra/conf.d/55_server_x11.conf
RUN sed -i '$s/-auth *[^ ]*//' /etc/xpra/conf.d/55_server_x11.conf


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

RUN apt-get update && apt-get install -y --no-install-recommends runc xpra socat vde2 qemu-utils ntfs-3g util-linux sudo unzip coreutils && rm -rf /var/lib/apt/lists/*


RUN mkdir -p /linapple-pie /minivmac /usr/local/bin

WORKDIR /app
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus-run.jar ./quarkus-run.jar
COPY --from=build /app/emucomp-api/target/quarkus-app/lib/ ./lib/
COPY --from=build /app/emucomp-api/target/quarkus-app/app/ ./app/
COPY --from=build /app/emucomp-api/target/quarkus-app/quarkus/ ./quarkus/

COPY --from=main /etc/xpra/conf.d/55_server_x11.conf /etc/xpra/conf.d/55_server_x11.conf

COPY --from=main /usr/bin/emucon-init /usr/bin/
COPY --from=main /usr/local /usr/local

EXPOSE 8080

CMD ["/bin/bash", "-c", "/usr/bin/emucon-init --networks-dir=/tmp/nics --xpra-socket=/tmp/xpra-socket -- java -jar ./quarkus-run.jar"]
