#!/bin/sh

__cmd_name="${__cmd_name} tool"


# ========== Helper Functions ==========

__print_usage()
{
	cat <<- EOT
	USAGE:
	    ${__cmd_name} -o <path> <name> [<name>...]

	DESCRIPTION:
	    Builds the specified OCI tool(s) from sources.

	ARGUMENTS:
	    <name>
	        The name of the tool to build:
	            image-tools - OCI image-tools
	            runtime-tools - OCI runtime-tools
	            runc - OCI runc-tool

	OPTIONS:
	    -o, --output-dir <path>
	        Output directory for built files.

	EOT
}


# ========== Script's Begin ==========

. emucon-init.sh

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse script's command line arguments
shortopts='o:h'
longopts='output-dir:,help'
cmdargs=$(emucon_parse_cmdargs -s "${shortopts}" -l "${longopts}" -- "$@")
if emucon_cmd_failed ; then
	emucon_abort
fi

# Lookup parsed parameters and their arguments
eval set -- ${cmdargs}
while true ; do
	case "$1" in
		-o|--output-dir)
			outdir="$2"
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

# Tools to build
tools="$@"
if [ -z "${tools}" ] ; then
	tools='image-tools runtime-tools runc'
	emucon_print_warning "No tools specified! Building all: ${tools}"
else
	# Check tool names
	for tool in ${tools} ; do
		case "${tool}" in
			image-tools|runtime-tools|runc)
				# Valid name
				;;
			*)
				emucon_print_error "Invalid tool name specified: ${tool}"
				emucon_abort -v ;;
		esac
	done
fi

emucon_check_required_arg '-o/--output-dir' "${outdir}"
emucon_ensure_dir_exists "${outdir}"

# Docker args
image='ubuntu:18.04'
dstdir='/emucon-scripts'
scriptvol="$(emucon_get_current_dir)/scripts:${dstdir}:ro"
outvol="${outdir}:/emucon-output"
cmd="${dstdir}/build.sh ${tools}"

emucon_print_info 'Running container for building...'
sudo docker run -t --rm -v "${scriptvol}" -v "${outvol}" "${image}" ${cmd}
emucon_print_info "Built files copied to: ${outdir}"

