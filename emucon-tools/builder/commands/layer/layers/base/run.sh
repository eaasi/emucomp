#!/bin/sh

__cmd_name="${__cmd_name} base"


# ========== Helper Functions ==========

__print_usage()
{
	cat <<- EOT
	USAGE:
	    ${__cmd_name} -t <type> [-s <size>] -o <path>

	DESCRIPTION:
	    Builds container's base layer.

	OPTIONS:
	    -t, --output-type <type>
	        Output type for built layer:
	            tree: Filesystem tree
	            qcow: QCow container image

	    -o, --output-path <path>
	        Output path for built layer

	    -s, --output-size <size>
	        Output image size in bytes (with optional suffix K, M or G)

	    -n, --use-nbd <dev>
	        Connect qcow-image to specified NBD device

	EOT
}


# ========== Script's Begin ==========

. $(emucon-paths helpers.sh)

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse script's command line arguments
shortopts='t:o:s:n:h'
longopts='output-type:,output-path:,output-size:,use-nbd:,help'
cmdargs=$(emucon_parse_cmdargs -s "${shortopts}" -l "${longopts}" -- "$@")
if emucon_cmd_failed ; then
	emucon_abort
fi

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while true ; do
	case "$1" in
		-t|--output-type)
			outtype="$2"
			shift 2 ;;
		-o|--output-path)
			outpath="$2"
			shift 2 ;;
		-s|--output-size)
			outsize="$2"
			shift 2 ;;
		-n|--use-nbd)
			nbdpath="$2"
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

# Check arguments
emucon_check_required_arg "-t/--output-type" "${outtype}"
emucon_check_required_arg "-o/--output-path" "${outpath}"

# Prepare output image/directory
case "${outtype}" in
	tree)
		emucon_ensure_dir_exists "${outpath}"
		outdir="${outpath}"
		;;
	qcow)
		emucon_check_required_arg "-s/--output-size" "${outsize}"
		emucon-qcow create -o "size=${outsize}" "${outpath}" || emucon_abort
		emucon-qcow mkfs --fs-type ext4 "${outpath}" || emucon_abort

		emucon_print_info "Creating temporary mountpoints for qcow-image..."
		tmpdir=$(mktemp -d --tmpdir 'emucon-XXXXX')
		outdir="${tmpdir}/fs"
		mkdir -v -p "${outdir}"
		if [ -n "${nbdpath}" ] ; then
			rawdir="${nbdpath}"
		else
			rawdir="${tmpdir}/raw"
			mkdir -v -p "${rawdir}"
		fi

		__cleanup() {
			emucon_print_info 'Cleaning up...'
			emucon-qcow unmount --fs-path "${outdir}" "${rawdir}" || emucon_abort
			rm -r -v "${tmpdir}"
		}

		trap __cleanup EXIT

		emucon-qcow mount --fs-type ext4 --fs-path "${outdir}" "${outpath}" "${rawdir}" || emucon_abort
		;;
	*)
		emucon_print_error "Invalid output type specified: ${outtype}"
		emucon_abort -v
		;;
esac

# Docker names
tag='eaas/emucon-base-layer:latest'
name='eaas-emucon-base'

emucon_print_info "Building new image for '${tag}'..."
sudo docker build --no-cache --force-rm=true -t "${tag}" . || emucon_abort -v

emucon_print_info "Creating container from image '${tag}'..."
container=$(sudo docker create --name "${name}" "${tag}") || emucon_abort -v

emucon_print_info "Exporting image to ${outdir}"
excludes='--exclude=dev/* --exclude=proc/* --exclude=usr/share/doc/* --exclude=usr/share/man/* --exclude=*/__pycache__/*'
tarargs="--ignore-failed-read --totals -C ${outdir}"
sudo docker export "${container}" | tar ${excludes} ${tarargs} -xvf -

emucon_print_info "Removing container ${name}"
sudo docker rm "${name}"

