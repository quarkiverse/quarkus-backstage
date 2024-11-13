#!/bin/sh
#-------------------------------------
# Hook invoked by the release workflow
#-------------------------------------

# Update the package list
sudo apt-get update

# Install Asciicinema
sudo apt-get install asciinema

# Install Asciinema-automation
python3 -m pip install asciinema-automation

# Install Asciinema-Agg
cargo install --git https://github.com/asciinema/agg

# Install Quarkus CLI
export SDKMAN_DIR="$HOME/.sdkman" && curl -s "https://get.sdkman.io?rcupdate=false" | bash
source $HOME/.sdkman/bin/sdkman-init.sh && sdk env install
sdk version
sdk install quarkus
quarkus version 

# Generate screencasts
cd docs/modules/ROOT/assets/ 
./generate-screencasts 
cd ../../../../

# Commit changes 
git commit -m "Update screencasts for ${CURRENT_VERSION}" -a
