#
# Implementation: emucon-qcow mount
#

__cmd_name="${__cmd_name} mount"


# ========== Helper Functions ==========

__print_usage() {
	cat <<- EOT
	USAGE:
	    ${__cmd_name} [--fs-type <type> --fs-path <path>] <image> <mount>

	DESCRIPTION:
	    Mounts a qcow-image at the specified mount-point.

	OPTIONS:
	    --fs-type <type>
	        Filesystem (ext4, xfs, etc.) to mount at <path>, if the qcow-image contains one.

	    --fs-path <path>
	        Path to mount the qcow-image's filesystem at.

	ARGUMENTS:
	    <image>
	        Path of the image to mount.

	    <mount>
	        Mount-point for the image.

	EOT
}


# ========== Script's Begin ==========

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse subcommand's command line arguments
longopts='fs-type:,fs-path:,help'
cmdargs=$(emucon_parse_cmdargs -s 'h' -l "${longopts}" -- "$@") || emucon_abort

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while [ $# -gt 0 ] ; do
	case "$1" in
		--fs-type)
			fstype="$2"
			shift 1 ;;
		--fs-path)
			fspath="$2"
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
mntpath="$2"

emucon_check_required_arg '<image>' "${imgpath}"
emucon_check_required_arg '<mount>' "${mntpath}"

emucon_print_info "Mounting qcow-image at ${mntpath}..."
case "${mntpath}" in 
	/dev/nbd*)
		# NBD mode
		emucon_ensure_is_installed 'qemu-nbd'

		sudo qemu-nbd --discard unmap \
			--cache writeback \
			--connect "${mntpath}" \
			"${imgpath}" || emucon_abort -v
		;;
	*)
		# xmount mode
		emucon_ensure_is_installed 'xmount'
		emucon_ensure_is_installed 'fusermount'

		xmount --out raw \
			--in qemu "${imgpath}" \
			--inopts 'qemuwritable=true,bdrv_cache=writeback' \
			--cache writethrough \
			"${mntpath}" || emucon_abort -v
		;;
esac

__cleanup() {
	if [ $? -eq 0 ] ; then
		# Normal termination
		exit 0
	fi

	emucon_print_info "Un-mounting qcow-image at ${mntpath}..."
	case "${mntpath}" in
		/dev/nbd*)
			sudo qemu-nbd --disconnect "${mntpath}" ;;
		*)
			fusermount -u -z "${mntpath}" ;;
	esac
}

trap __cleanup EXIT

if [ -n "${fstype}" ] ; then
	emucon_check_required_arg '--fs-path' "${fspath}"

	emucon_print_info "Mounting qcow-image's filesystem at ${fspath}..."
	case "${mntpath}" in
		/dev/nbd*)
			# System-mount mode
			sudo mount -t "${fstype}" "${mntpath}" "${fspath}"
			sudo chmod a+rwx "${fspath}" ;;
		*)
			# FUSE-mount mode
			emucon_ensure_is_installed 'lklfuse'
			rawimgpath=$(__to_mounted_image_path "${imgpath}" "${mntpath}")
			lklfuse "${rawimgpath}" "${fspath}" -o "allow_other,use_ino,rw,type=${fstype}" ;;
	esac
fi

