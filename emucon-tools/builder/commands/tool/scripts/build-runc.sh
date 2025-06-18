#
# Builds runc binary
#

__print_message 'Downloading OCI runc...'
dstdir="${GOPATH}/src/github.com/opencontainers/runc"
url='https://github.com/opencontainers/runc.git'
tag='v1.1.4'
mkdir -v -p "${dstdir}"
cd "${dstdir}"
git clone --depth 1 --branch "${tag}" "${url}" .

__print_message 'Building OCI runc...'
make BUILDTAGS=''

__print_message 'Installing OCI runc...'
make install PREFIX="${HOME}/.local" BINDIR="${GOBIN}"

