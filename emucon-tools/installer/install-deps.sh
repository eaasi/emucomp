#!/bin/sh

__cmd_name=$(basename "$0")


# ========== Script's Begin ==========

. emucon-init.sh

emucon_print_info 'Installing dependencies...'
sudo apt-get update
sudo apt-get install jq

