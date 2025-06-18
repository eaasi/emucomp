#!/bin/sh

__cmd_name=$(basename "$0")

# Default install directory
readonly default_dstdir='/usr/local'


# ========== Helper Functions ==========

__print_usage()
{
	cat <<- EOT
	USAGE:
	    ${__cmd_name} [--destination <path>]

	DESCRIPTION:
	    Builds and installs OCI-Tools.

	OPTIONS:
	    -d, --destination <path>
	        The path to install into. (default: ${default_dstdir})

	EOT
}


# ========== Script's Begin ==========

if ! which emucon-install > /dev/null ; then
	echo 'Required emucon-tools are not in PATH!'
	echo 'Please source bootstrap.sh first.'
	echo 'Aborting...'
	exit 1
fi

. emucon-init.sh

# Parse script's command line arguments
longopts='destination:,help'
cmdargs=$(emucon_parse_cmdargs -s 'd:h' -l "${longopts}" -- "$@")
if emucon_cmd_failed ; then
	emucon_abort
fi

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while true ; do
	case "$1" in
		-d|--destination)
			dstdir="$2"
			shift 2 ;;
		-h|--help)
			__print_usage
			emucon_exit ;;
		--)
			shift 1
			break ;;
		*)
			emucon_print_invalid_cmdargs_error "${cmdargs}"
			emucon_abort -v ;;
	esac
done

# Safety check!
if [ $# -ne 0 ] ; then
	emucon_print_invalid_cmdargs_error "${cmdargs}"
	emucon_abort -v
fi

# Runtime directory
curdir=$(emucon_get_current_dir "$0")

# Install directory
dstdir="${dstdir:-${default_dstdir}}"
emucon_ensure_dir_exists "${dstdir}"

emucon_print_info 'Creating temporary directory...'
readonly tmpdir=$(mktemp -d '/tmp/emucon-XXX')

__cleanup()
{
	emucon_print_info 'Cleaning up...'
	sudo rm -r -v "${tmpdir}"
}

# Setup an exit-trap
trap __cleanup EXIT

emucon_print_info 'Building OCI tools...'
cd "${curdir}/../builder"
./emucon-build tool --output-dir "${tmpdir}" || emucon_abort

emucon_print_info 'Installing OCI tools...'
emucon-install "${tmpdir}" "${dstdir}" || emucon_abort

