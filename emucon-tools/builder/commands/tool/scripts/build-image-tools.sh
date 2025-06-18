#
# Builds image-tools binaries
#

__print_message 'Downloading OCI image-tools...'
dstdir="${GOPATH}/src/github.com/opencontainers/image-tools"
url='https://github.com/opencontainers/image-tools.git'
tag='v1.0.0-rc3'
mkdir -v -p "${dstdir}"
cd "${dstdir}"
git clone --depth 1 --branch "${tag}" "${url}" .

__print_message 'Building OCI image-tools...'
go mod init
go mod tidy
go mod vendor
make tool man

__print_message 'Installing OCI image-tools...'
make install PREFIX="${HOME}/.local" BINDIR="${GOBIN}"

