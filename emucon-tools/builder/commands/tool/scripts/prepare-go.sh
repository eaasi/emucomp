#
# Prepares GO toolchain
#

__print_message 'Installing GO toolchain...'
apt-get update
apt-get install -y git curl golang go-md2man
curl -L "https://go.dev/dl/go1.19.linux-amd64.tar.gz" | tar xz -C /usr/local

__print_message 'Setting up GO toolchain...'
mkdir -p "${HOME}/.local/bin"
cat >> "${HOME}/.profile" <<- "EOF"
	export GOPATH=${HOME}/work
	export GOBIN=${HOME}/.local/bin
	export PATH="/usr/local/go/bin:${GOBIN}:${PATH}"
	export MANPATH=/usr/local/go/share/man:${HOME}/.local/share/man:"${MANPATH}"
EOF

. "${HOME}/.profile"
