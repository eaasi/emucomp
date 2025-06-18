#
# Implementation: emucon-qcow create
#

__cmd_name="${__cmd_name} create"


# ========== Helper Functions ==========

__print_usage() {
	cat <<- EOT
	USAGE:
	    ${__cmd_name} -o <options> <path>

	DESCRIPTION:
	    Creates a qcow-image.

	OPTIONS:
	    -o, --options <options>
	        List of image options.

	ARGUMENTS:
	    <path>
	        Path of the image to create.

	EOT
}

__has_option() {
	local name="$1"
	local list="$2"
	echo "${list}" | grep "${name}=" > /dev/null
}


# ========== Script's Begin ==========

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse subcommand's command line arguments
longopts='options:,help'
cmdargs=$(emucon_parse_cmdargs -s 'o:,h' -l "${longopts}" -- "$@") || emucon_abort

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while [ $# -gt 0 ] ; do
	case "$1" in
		-o|--options)
			options="$2"
			shift 1 ;;
		-h|--help)
			__print_usage
			emucon_exit ;;
		--)
			shift 1
			break ;;
		*)
			emucon_print_invalid_cmdargs_error "${cmdargs}"
			emucon_abort ;;
	esac
	shift 1
done

outpath="$1"

emucon_check_required_arg '<path>' "${outpath}"	
emucon_ensure_is_installed 'qemu-img'

# Option 'size' is required, when option 'backing_file' is not used!
if ! __has_option 'size' "${options}" && ! __has_option 'backing_file' "${options}" ; then
	emucon_print_error "Size of the qcow-image is missing!"
	emucon_print_error "It must be specified in the options list, like:"
	if [ -n "${options}" ] ; then
		options="${options},size=2G"
	else
		options="size=2G"
	fi
	emucon_print_error "${__cmd_name} -o ${options} ${outpath}"
	emucon_abort -v
fi

emucon_print_info "Creating qcow-image..."
qemu-img create -f qcow2 -o "${options}" "${outpath}"

