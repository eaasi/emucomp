#!/bin/bash
IMAGE_NAME="eaas-emucon-tools"
EMUCON_TOOLS_URL="https://gitlab.com/emulation-as-a-service/emucon-tools/-/archive/master/emucon-tools-master.zip"
TARGET_DIR="unzip"

cleanup() {
    if [ -f "$ZIP_FILE" ]; then
        rm -f "$ZIP_FILE"
        echo "Deleted temporary ZIP file: $ZIP_FILE"
    fi

    if [ -d "$TARGET_DIR" ]; then
        rm -rf "$TARGET_DIR"
        echo "Deleted temporary directory: $TARGET_DIR"
    fi
}
trap cleanup EXIT

for cmd in curl unzip; do
    if ! command -v "$cmd" &> /dev/null; then
        echo "Error: Command '$cmd' not found. Please install it."
        exit 1
    fi
done

if docker images --format "{{.Repository}}" | grep -q "^${IMAGE_NAME}$"; then
    echo "Image '$IMAGE_NAME' found locally."
    exit 0
else
    echo "Image '$IMAGE_NAME' not found locally. Downloading from ${}"
    ZIP_FILE=$(basename "$EMUCON_TOOLS_URL")
    echo "Downloading $ZIP_URL to $ZIP_FILE..."
    if ! curl -L -o "$ZIP_FILE" "$ZIP_URL"; then
        echo "Error: Failed to download $ZIP_URL."
        exit 1
    fi
    echo "Download complete."

    mkdir -p "$TARGET_DIR" || { echo "Error: Failed to create directory '$TARGET_DIR'."; exit 1; }
    echo "Created directory: '$TARGET_DIR'"

    if ! unzip -q "$ZIP_FILE" -d "$TARGET_DIR"; then
      echo "Error: Failed to unzip $ZIP_FILE to $TARGET_DIR."
      exit 1
    fi

    cd "$TARGET_DIR/common/eaas-emucon-tools"
    if [ $? -eq 0 ]; then
        docker build -t ${IMAGE_NAME}:latest .
        BUILD_STATUS=$?

        if [ "$BUILD_STATUS" -eq 0 ]; then
            echo "-------------------------------------"
            echo "Docker image build SUCCESS!"
            echo "Image '${IMAGE_NAME}:latest' created successfully."
            echo "You can now run it using: docker run -it ${IMAGE_NAME}:latest"
            echo "-------------------------------------"
            exit 0
        else
            echo "-------------------------------------"
            echo "Docker image build FAILED with exit code: $BUILD_STATUS"
            echo "Please review the build logs above for errors."
            echo "-------------------------------------"
            exit 1
        fi
    else
        echo "Error: Failed to change directory to '$TARGET_DIR/common/eaas-emucon-tools'."
        exit 1
    fi
fi