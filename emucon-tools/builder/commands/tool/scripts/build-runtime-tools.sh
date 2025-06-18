#
# Builds runtime-tools binaries
#

__print_message 'Downloading OCI runtime-tools...'
dstdir="${GOPATH}/src/github.com/opencontainers/runtime-tools"
url='https://github.com/opencontainers/runtime-tools.git'
tag='v0.9.0'
mkdir -v -p "${dstdir}"
cd "${dstdir}"
git clone --depth 1 --branch "${tag}" "${url}" .

__print_message 'Building OCI runtime-tools...'
go mod init
go mod tidy
go mod vendor
make tool man

__print_message 'Installing OCI runtime-tools...'
make install PREFIX="${HOME}/.local" BINDIR="${GOBIN}"

