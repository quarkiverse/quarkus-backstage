#!/bin/sh
#-------------------------------------
# Hook invoked by the release workflow
#-------------------------------------

# We need to rebuild the project
mvn clean install -DskipTests

# Update the package list
sudo apt-get update

# Install Quarkus CLI
jbang trust add https://repo1.maven.org/maven2/io/quarkus/quarkus-cli/
jbang app install --fresh --force quarkus@quarkusio

# Install Asciicinema
sudo apt-get install asciinema

# Install Asciinema-automation
python3 -m pip install asciinema-automation

# Install Asciinema-Agg
cargo install --git https://github.com/asciinema/agg

# Iterate all .sh files in the docs/modules/ROOT/assets/scenarios directory
# and replace 999-SNAPSHOT with the current version
sed -i "s/999-SNAPSHOT/${CURRENT_VERSION}/g" docs/modules/ROOT/assets/scenarios/*.sh

# Generate screencasts
cd docs/modules/ROOT/assets/ 
./generate-screencasts 
cd ../../../../

# Commit changes 
git checkout -b update-screencasts-${CURRENT_VERSION}

git add docs/modules/ROOT/assets/scenarios
git add docs/modules/ROOT/assets/casts
git add docs/modules/ROOT/assets/images
git commit -m "Update screencasts for ${CURRENT_VERSION}"


git push origin update-screencasts-${CURRENT_VERSION}
