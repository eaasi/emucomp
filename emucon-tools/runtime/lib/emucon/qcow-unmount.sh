#
# Implementation: emucon-qcow unmount
#

__cmd_name="${__cmd_name} unmount"


# ========== Helper Functions ==========

__print_usage() {
	cat <<- EOT
	USAGE:
	    ${__cmd_name} [--fs-path <path>] <mount>

	DESCRIPTION:
	    Un-mounts a qcow-image from the specified mount-point.

	OPTIONS:
	    --fs-path <path>
	        Path to unmount the qcow-image's filesystem from.

	    --no-sync
	        Do not execute a sync after unmount.

	ARGUMENTS:
	    <mount>
	        Mount-point for the image.

	EOT
}

__run_sync() {
	test "${dosync}" = 'y' && sync
}


# ========== Script's Begin ==========

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse subcommand's command line arguments
longopts='fs-path:,no-sync,help'
cmdargs=$(emucon_parse_cmdargs -s 'h' -l "${longopts}" -- "$@") || emucon_abort

dosync='y'

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while [ $# -gt 0 ] ; do
	case "$1" in
		--fs-path)
			fspath="$2"
			shift 1 ;;
		--no-sync)
			dosync='n'
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

mntpath="$1"

emucon_check_required_arg '<mount>' "${mntpath}"

if [ -n "${fspath}" ] ; then
	__run_sync
	emucon_print_info "Un-mounting qcow-image's filesystem from ${fspath}..."
	emucon_ensure_dir_exists "${fspath}"
	case "${mntpath}" in
		/dev/nbd*)
			sudo umount "${fspath}" ;;
		*)
			emucon_ensure_is_installed 'fusermount'
			fusermount -u -z "${fspath}" ;;
	esac
fi

__run_sync

emucon_print_info "Un-mounting qcow-image from ${mntpath}..."
case "${mntpath}" in
	/dev/nbd*)
		emucon_ensure_is_installed 'qemu-nbd'
		sudo qemu-nbd --disconnect "${mntpath}" ;;
	*)
		emucon_ensure_is_installed 'fusermount'
		fusermount -u -z "${mntpath}" ;;
esac

__run_sync

