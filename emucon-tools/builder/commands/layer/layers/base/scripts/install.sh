#!/bin/sh

# Directory containing scripts
readonly scripts=$(dirname $(readlink -f $0))

# ========== Internal Helpers ==========

__print_message()
{
	echo "[container] $1"
}

__to_flat_list()
{
	# Skip all commented out and empty lines
	cat - | grep --invert-match -E '#|^$' | tr '\n' ' '
}

__print_emulators()
{
	cat "${scripts}/emulators.txt" |  __to_flat_list
}

__print_dependencies()
{
	cat "${scripts}/dependencies.txt" | __to_flat_list
}


# ========== Script's Begin ==========

__print_message "Executing script $0..."

export DEBIAN_FRONTEND=noninteractive

__print_message 'Upgrading system packages...'
apt-get update
apt-get upgrade -y
apt-get autoremove

__print_message 'Installing emulators and deps...'
readonly emulators="$(__print_emulators)"
readonly deps="$(__print_dependencies)"
apt-get install -y --allow-unauthenticated ${deps} ${emulators}
apt-get autoremove

__print_message 'Installing xpra deps...'
pip install python-uinput

__print_message 'Removing emulators...'
apt-get remove -y --allow-unauthenticated ${emulators}

__print_message 'Installing emucon-init...'
install -v -m 'a=rx' "${scripts}/emucon-init" '/usr/bin/emucon-init'

# NOTE: apt-get tool tries to drop privileges, when run as root.
#       For this a user _apt is created inside of the container.
#       But when we export and run containers inside a user-namespace,
#       apt-get tool fails to change the owner of some temporary folders
#       with "Permission denied" errors. This happens because the host
#       user normally doesn't have the permissions to change owners of
#       folders without elevated privileges.

# HACK: change apt-tool's user ID to root
usermod --non-unique --uid 0 _apt

# Add main user
readonly uid='1000'
addgroup --gid "${uid}" bwfla
useradd -ms /bin/bash --uid "${uid}" --gid bwfla bwfla
adduser bwfla xpra

__print_message 'Update Xpra config...'
sed -i '$s/$/ -nolisten local/' /etc/xpra/conf.d/55_server_x11.conf
sed -i '$s/-auth *[^ ]*//' /etc/xpra/conf.d/55_server_x11.conf

# Xpra runtime directory
mkdir -p "/run/user/${uid}/xpra"
chmod a=rwx "/run/user/${uid}/xpra"

__print_message 'Cleaning up package manager...'
apt-get clean

__print_message 'Cleaning up directories...'
rm -rf /var/lib/apt/lists/*
rm -rf /var/tmp/*
rm -rf /tmp/*
rm -rf "${scripts}"

__print_message "Script $0 finished"

