#
# Helpers for emucon-tools, compatible with /bin/sh
#

# ========== Helper Functions ==========

emucon_exit()
{
	exit 0
}

emucon_abort()
{
	if [ "$1" = '-v' ] ; then
		emucon_print_error 'Aborting...'
	fi

	exit 1
}

emucon_cmd_failed()
{
	# Failed, if exit code != 0
	test $? -ne 0
}

emucon_print()
{
	echo "$*"
}

emucon_print_info()
{
	echo "[${__cmd_name}] $*"
}

emucon_print_warning()
{
	if [ -n "${TERM}" ] ; then
		# Output in yellow color!
		tput setaf 3
		emucon_print_info "$@"
		tput sgr0
	else
		emucon_print_info "$@"
	fi
}

emucon_print_error()
{
	if [ -n "${TERM}" ] ; then
		# Output in red color!
		tput setaf 1
		emucon_print_info "$@" >&2
		tput sgr0
	else
		emucon_print_info "$@" >&2
	fi
}

emucon_print_invalid_cmdargs_error()
{
	emucon_print_error "Invalid command line arguments: $1"
}

emucon_print_cmdargs_parsing_error()
{
	emucon_print_error 'Parsing command line arguments failed!'
	emucon_print_error "Run '${__cmd_name} --help' for usage documentation."
}

emucon_parse_cmdargs()
{
	# Example call:
	# emucon_parse_cmdargs -s 'o:h' -l 'output:,help' -- args

	local shortopts
	local longopts

	# Parse function's named arguments
	while true ; do
		case "$1" in
			-s|--short)
				shortopts="$2"
				shift 2 ;;
			-l|--long)
				longopts="$2"
				shift 2 ;;
			--)
				shift 1
				break ;;
			*)
				emucon_print_error "emucon_parse_cmdargs: unrecognized option '$1'"
				emucon_print_error 'Internal error occured! Aborting...'
				emucon_abort
		esac
	done

	local cmdargs

	# Parse supplied command's arguments
	cmdargs=$(getopt -o "${shortopts}" -l "${longopts}" -n "${__cmd_name}" -- "$@")
	if emucon_cmd_failed ; then
		emucon_print_cmdargs_parsing_error
		emucon_abort -v
	fi

	printf "%s" "${cmdargs}"
}

emucon_check_required_arg()
{
	local argname
	local argvalue

	argname="$1"
	argvalue="$2"

	if [ -z "${argvalue}" ] ; then
		emucon_print_error "Required argument ${argname} is missing!"
		emucon_abort -v
	fi
}

emucon_to_absolute_path()
{
	if [ -f "$1" ] ; then
		local dname="$(dirname "$1")"
		local fname="/$(basename "$1")"
	else
		local dname="$1"
	fi

	dname="$(cd "${dname}" && pwd)"
	printf '%s%s\n' "${dname}" "${fname}"
}

emucon_to_absolute_dirname()
{
	echo $(dirname $(emucon_to_absolute_path "$1"))
}

emucon_get_current_dir()
{
	echo $(emucon_to_absolute_dirname "$0")
}

emucon_ensure_dir_exists()
{
	if [ ! -d "$1" ] ; then
		emucon_print_error "Specified directory does not exist: $1"
		emucon_abort -v
	fi
}

emucon_ensure_file_exists()
{
	if [ ! -f "$1" ] ; then
		emucon_print_error "Specified file does not exist: $1"
		emucon_abort -v
	fi
}

emucon_ensure_is_installed()
{
	local cmd
	cmd="$1"

	if ! which "${cmd}" > /dev/null ; then
		emucon_print_error "${cmd} was not found in PATH!"
		emucon_print_error 'Please install it first.'
		emucon_abort -v
	fi
}

