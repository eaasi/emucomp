#!/bin/sh

__cmd_name=$(basename "$0")

# Default install directory
readonly default_dstdir='/usr/local'

# Default sudoers directory
readonly default_sudodir='/etc/sudoers.d'


# ========== Helper Functions ==========

__print_usage()
{
	cat <<- EOT
	USAGE:
	    ${__cmd_name} [-u <name>] [--sudoers-dir <path>] [--destination <path>]

	DESCRIPTION:
	    Installs emucon-tools and dependencies on the host system.

	OPTIONS:
	    -d, --destination <path>
	        The path to install into. (default: ${default_dstdir})

	    -u, --user <name>
	        The name of the user for sudoers configuration.

	    --sudoers-dir <path>
	        The path for sudoers drop-in files. (default: ${default_sudodir})

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
longopts='destination:,user:,sudoers-dir:,help'
cmdargs=$(emucon_parse_cmdargs -s 'd:u:h' -l "${longopts}" -- "$@")
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
		-u|--user)
			user="$2"
			shift 2 ;;
		--sudoers-dir)
			sudodir="$2"
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
curdir=$(emucon_get_current_dir)
srcdir=$(emucon_to_absolute_path "${curdir}/../runtime")

# Install directory
dstdir="${dstdir:-${default_dstdir}}"
emucon_ensure_dir_exists "${dstdir}"

# Sudoers directory
sudodir="${sudodir:-${default_sudodir}}"
emucon_ensure_dir_exists "${sudodir}"

# Sudoers user
user="${user:-$(id --user --name)}"

emucon_print_info 'Creating temporary directory...'
readonly tmpdir=$(mktemp -d '/tmp/emucon-XXX')

__cleanup()
{
	emucon_print_info 'Cleaning up...'
	sudo rm -r -v "${tmpdir}"
}

# Setup an exit-trap
trap __cleanup EXIT

emucon_print_info 'Installing emucon-tools...'
emucon-install "${srcdir}" "${dstdir}" || emucon_abort

emucon_print_info "Installing sudoers configuration for user '${user}'..."
sudosrc="$(emucon-paths sudoers.template)"
sudotmp="${tmpdir}/emucon-pwdless-commands"
sedcmds="s|{{install-dir}}|${dstdir}|g; s|{{user}}|${user}|g"
cat "${sudosrc}" | sed "${sedcmds}" > "${sudotmp}" || emucon_abort
visudo --check --file "${sudotmp}" || emucon_abort
if [ ! -w "${sudodir}" ] ; then
	# Install directory not writable,
	# use sudo for file operations
	cmdprefix='sudo'
fi

# TODO: skip installing it for now! A different solution is needed!
#${cmdprefix} install -v -m 440 "${sudotmp}" "${sudodir}" || emucon_abort
