#!/bin/sh

export __cmd_name="${__cmd_name} layer"


# ========== Helper Functions ==========

__print_usage()
{
	cat <<- EOT
	USAGE:
	    ${__cmd_name} <name> [<args>...]

	DESCRIPTION:
	    Builds the specified container layer.

	ARGUMENTS:
	    <name>
	        The name of the layer to build:
	            base - Base layer
	            emulator - Emulator layers

	EOT
}


# ========== Script's Begin ==========

. $(emucon-paths helpers.sh)

if [ $# -eq 0 ] ; then
	__print_usage
	emucon_exit
fi

# Parse script's command line arguments
case "$1" in
	-h|--help)
		__print_usage
		emucon_exit ;;
	base|emulator)
		layer="$1"
		shift 1 ;;
	-*)
		emucon_print_error "${__cmd_name}: unrecognized option '$1'"
		emucon_print_cmdargs_parsing_error
		emucon_abort -v ;;
	*)
		emucon_print_error "${__cmd_name}: invalid name -- '$1'"
		emucon_print_cmdargs_parsing_error
		emucon_abort -v ;;
esac

# Running subcommand
cd "layers/${layer}"
./run.sh "$@"

