#!/bin/sh

set -e

__print_message()
{
	echo "[container] $1"
}

__print_message "Executing script $0..."

# Tools to build
if [ -z "$*" ]; then
	__print_message 'No tools spcecified! Exiting...'
	exit 1
fi

readonly outdir='/emucon-output'
readonly scripts="$(dirname $(readlink -f $0))"

. "${scripts}/prepare-go.sh"

# Build all requested tools
for tool in "$@" ; do
	. "${scripts}/build-${tool}.sh"
done

__print_message 'Copying output files...'
cp -v -r -t "${outdir}" ${HOME}/.local/*

__print_message "Script $0 finished"

