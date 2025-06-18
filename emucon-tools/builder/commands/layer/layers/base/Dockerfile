FROM phusion/baseimage:0.9.22

COPY scripts /emucon-scripts/

# Set up custom repo
COPY data/*.list /etc/apt/sources.list.d/
COPY data/pin-bwfla.pref /etc/apt/preferences.d/pin-bwfla.pref

RUN /sbin/my_init -- /emucon-scripts/install.sh

