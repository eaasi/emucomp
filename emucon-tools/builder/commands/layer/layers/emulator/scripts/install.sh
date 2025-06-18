#!/bin/sh

# Directory containing scripts
readonly scripts=$(dirname $(readlink -f $0))


# ========== Internal Helpers ==========

__print_message()
{
	echo "[container] $1"
}


# ========== Script's Begin ==========

__print_message "Executing script $0..."

if [ $# -eq 0 ] ; then
	__print_message 'No packages specified!'
	__print_message "Usage: $0 <package> [<package>...]"
	exit 1
fi

export DEBIAN_FRONTEND=noninteractive

__print_message 'Installing packages...'
apt-get update
apt-get install -y --allow-unauthenticated "$@"

__print_message 'Cleaning up package manager...'
apt-get clean

__print_message 'Cleaning up directories...'
rm -rf /var/lib/apt/lists/*
rm -rf /var/tmp/*
rm -rf /tmp/*

__print_message "Script $0 finished"

