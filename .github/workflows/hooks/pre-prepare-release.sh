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
curl -Ls https://sh.jbang.dev | bash -s - trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/
curl -Ls https://sh.jbang.dev | bash -s - app install --fresh --force quarkus@quarkusio
exec quarkus version

# Generate screencasts
cd docs/modules/ROOT/assets/ 
exec ./generate-screencasts 
cd ../../../../

# Commit changes 
git commit -m "Update screencasts for ${CURRENT_VERSION}" -a
