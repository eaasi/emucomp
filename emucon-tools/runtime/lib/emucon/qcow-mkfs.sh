#
# Implementation: emucon-qcow mkfs
#

__cmd_name="${__cmd_name} mkfs"


# ========== Helper Functions ==========

__print_usage() {
	cat <<- EOT
	USAGE:
	    ${__cmd_name} --fs-type <type> <image>

	DESCRIPTION:
	    Creates a new filesystem on specified qcow-image.

	OPTIONS:
	    -t, --fs-type <type>
	        Filesystem (ext4, xfs, etc.) to create.

	ARGUMENTS:
	    <image>
	        Path of the image to create filesystem on.

	EOT
}


# ========== Script's Begin ==========

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse subcommand's command line arguments
cmdargs=$(emucon_parse_cmdargs -s 't:h' -l 'fs-type:,help' -- "$@") || emucon_abort

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while [ $# -gt 0 ] ; do
	case "$1" in
		-t|--fs-type)
			fstype="$2"
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

imgpath="$1"

emucon_check_required_arg '-t/--fs-type' "${fstype}"
emucon_check_required_arg '<image>' "${imgpath}"
emucon_ensure_is_installed "mkfs.${fstype}"

emucon_print_info "Creating mountpoint for qcow-image..."
mntpath=$(mktemp -d --tmpdir 'emucon-XXXXX')

__cleanup() {
	emucon_print_info 'Cleaning up...'
	emucon-qcow unmount "${mntpath}"
	rm -r -v "${mntpath}"
}

trap __cleanup EXIT

emucon-qcow mount "${imgpath}" "${mntpath}" || emucon_abort
rawimgpath=$(__to_mounted_image_path "${imgpath}" "${mntpath}")

emucon_print_info "Creating filesystem..."
case "${fstype}" in
	ext4)
		mkfs.ext4 -F "${rawimgpath}" || emucon_abort -v ;;
	*)
		emucon_print_error "Unsupported filesystem: ${fstype}"
		emucon_abort -v ;;
esac

