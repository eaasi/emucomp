# Introduction

`emucon-tools` is a collection of scripts for running containerized emulators.


# Bootstrapping

The scripts and tools assume that `runtime/bin` folder is in your `PATH`.
To bootstrap a system for a single terminal session, simply run in your shell:
```
$ . ./bootstrap.sh
```


# Installation

To install `emucon-tools` and all dependencies into `/usr/local`, simply run:
```
$ ./install.sh --destination /usr/local
```

Some dependencies will be built from source and some installed using host's package manager.

The previous command will also add a sudoers-configuration for the current user, to be able
to run required privileged commands without asking user password. A different user can be
specified with `--user <name>` option. For all available options, please run:
```
$ ./install.sh --help
```

# Installation in Docker-Container

The OCI tools must be built on the host (outside of Docker) by running:
```
$ mkdir /tmp/oci-tools
$ ./installer/install-oci-tools.sh --destination /tmp/oci-tools
```

Then in your Dockerfile add:
```
COPY /tmp/oci-tools /usr/local
RUN <EMUCON-TOOLS-REPO>/installer/install-scripts.sh --destination /usr/local \
    && <EMUCON-TOOLS-REPO>/installer/install-deps.sh
```

