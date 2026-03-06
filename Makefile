
SHELL := /bin/bash

builders:
	mvn clean install -pl :quarkus-backstage-model-builder-generator -am && \
  rm -rf model-generator/model/src/main/java/io.quarkiverse.backstage/*.java && \
  rm -rf model-generator/model/src/main/java/io/quarkiverse/backstage/v1alpha1/* && \
  rm -rf model-generator/model/src/main/java/io/quarkiverse/backstage/scaffolder/v1beta3/* && \
  rm -rf model-generator/model/src/main/java/io/quarkiverse/backstage/model/builder/* && \
	mkdir -p model-generator/model/src/main/java/io/quarkiverse/backstage/model/builder && \
	mkdir -p model-generator/model/src/main/java/io/quarkiverse/backstage/v1alpha1 && \
	mkdir -p model-generator/model/src/main/java/io/quarkiverse/backstage/scaffolder/v1beta3 && \
	cp model-generator/builder-generator/target/generated-sources/annotations/io/quarkiverse/backstage/*.java model-generator/model/src/main/java/io/quarkiverse/backstage/
	cp -r model-generator/builder-generator/target/generated-sources/annotations/io/quarkiverse/backstage/v1alpha1/* model-generator/model/src/main/java/io/quarkiverse/backstage/v1alpha1/
	cp -r model-generator/builder-generator/target/generated-sources/annotations/io/quarkiverse/backstage/scaffolder/v1beta3/* model-generator/model/src/main/java/io/quarkiverse/backstage/scaffolder/v1beta3/
	cp -r model-generator/builder-generator/target/generated-sources/annotations/io/quarkiverse/backstage/model/builder/* model-generator/model/src/main/java/io/quarkiverse/backstage/model/builder/

build:
	mvn clean install

all: builders build
