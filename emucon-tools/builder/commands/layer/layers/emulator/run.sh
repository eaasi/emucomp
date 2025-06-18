#!/bin/sh

__cmd_name="${__cmd_name} emulator"


# ========== Helper Functions ==========

__print_usage()
{
	cat <<- EOT
	USAGE:
	    ${__cmd_name} -t <type> -o <path> -b <path> [<name>...]

	DESCRIPTION:
	    Builds container's emulator layer.

	OPTIONS:
	    -t, --output-type <type>
	        Output type for built layer:
	            tree: Filesystem tree
	            qcow: QCow container image

	    -o, --output-dir <path>
	        Output directory for built files.

	    -b, --base-layer <path>
	        Base layer directory or qcow-image.

	    -n, --use-nbd <dev>
	        Connect qcow-image to specified NBD device.

	    --list
	        List all supported emulator package names.

	ARGUMENTS:
	    <name>
	        The emulator's package name.

	EOT
}

# All supported emulators
__print_emulator_names()
{
	# Skip all commented out and empty lines
	cat "$(emucon_get_current_dir)/../base/scripts/emulators.txt" \
		| grep --invert-match -E '#|^$'
}

__check_emulator_name()
{
	# Find first match and stop immediately
	__print_emulator_names | grep -m 1 --line-regexp "$1" > /dev/null
}


# ========== Script's Begin ==========

. $(emucon-paths helpers.sh)

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse script's command line arguments
shortopts='t:o:b:n:h'
longopts='output-type:,output-dir:,base-layer:,use-nbd:,list,help'
cmdargs=$(emucon_parse_cmdargs -s "${shortopts}" -l "${longopts}" -- "$@") || emucon_abort

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while true ; do
	case "$1" in
		-b|--base-layer)
			basepath="$2"
			shift 2 ;;
		-t|--output-type)
			outtype="$2"
			shift 2 ;;
		-o|--output-dir)
			outdir="$2"
			shift 2 ;;
		-n|--use-nbd)
			nbdpath="$2"
			shift 2 ;;
		--list)
			__print_emulator_names
			emucon_exit ;;
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
emucon_check_required_arg "-o/--output-dir" "${outdir}"
emucon_check_required_arg "-b/--base-layer" "${basepath}"
emucon_ensure_dir_exists "${outdir}"
case "${outtype}" in
	tree)
		emucon_ensure_dir_exists "${basepath}"
		;;
	qcow)
		emucon_ensure_file_exists "${basepath}"
		;;
	*)
		emucon_print_error "Invalid output type specified: ${outtype}"
		emucon_abort -v
		;;
esac

emulators=''
if [ $# -gt 0 ] ; then
	# Check specified emulator names
	for emulator in "$@" ; do
		if ! __check_emulator_name "${emulator}" ; then
			emucon_print_error "Invalid emulator's package name specified: ${emulator}"
			emucon_abort -v
		fi
		emulators="${emulators} ${emulator}"
	done
else
	# No emulators specified, use all
	emucon_print_warning 'No emulators specified! Build all supported.'
	emulators="$(__print_emulator_names | tr '\n' ' ')"
fi

# User for user-namespace mapping
user=$(id --user --name)
group=$(id --group --name)

# Path for scripts to be run in containers
scripts="$(emucon_get_current_dir)/scripts"

emucon_print_info 'Creating temporary directory...'
tmpdir=$(mktemp -d --tmpdir 'emucon-XXXXX')

# Generate requested layers...
for emulator in ${emulators} ; do
	emucon_print_info "Starting emulator-layer generation for ${emulator}..."
	workdir="${tmpdir}/${emulator}"
	mkdir -p "${workdir}"
	case "${outtype}" in
		tree)
			emuoutpath="${outdir}/${emulator}"
			mkdir -p "${emuoutpath}"
			;;
		qcow)
			emuoutpath="${outdir}/${emulator}.qcow"
			emucon-qcow create -o "backing_file=${basepath}" "${emuoutpath}"
			;;
	esac

	emucon_print_info "Using working directory: ${workdir}"

	emucon_print_info "Generating config.json for ${emulator}..."
	emucon-cgen --mount "${scripts}:/emucon-scripts:bind:ro" \
		-- /sbin/my_init -- /emucon-scripts/install.sh "${emulator}" \
		> "${workdir}/config.json"

	emucon_print_info "Running container for ${emulator}..."
	case "${outtype}" in
		tree)
			options="-t overlay -l ${basepath} -u ${emuoutpath}" ;;
		qcow)
			options="-t qcow -i ${emuoutpath}"
			if [ -n "${nbdpath}" ] ; then
				options="${options} --use-nbd ${nbdpath}"
			fi
			;;
	esac

	conid=$(mktemp --dry-run "congen-XXXXX")
	if emucon-run -w "${workdir}" ${options} -c "${conid}" ; then
		emucon_print_info "Emulator-layer for ${emulator} created at: ${emuoutpath}"
	else
		emucon_print_error "Generating emulator-layer for ${emulator} failed!"
	fi

	emucon_print_info 'Cleaning up...'
	rm -v -r "${workdir}"
done

emucon_print_info 'Final cleanup...'
rm -v -r "${tmpdir}"

