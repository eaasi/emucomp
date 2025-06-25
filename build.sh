#!/bin/bash


apt-get update && apt-get install -y curl unzip sudo \
    && rm -rf /var/lib/apt/lists/*

mkdir /tmp/oci-tools
./emucon-tools/installer/install-oci-tools.sh --destination /tmp/oci-tools