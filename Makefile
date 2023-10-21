
SHELL := /bin/bash

builders:
	mvn clean install -pl :quarkus-backstage-model-builder-generator && \
  rm -rf model-generator/model/src/main/java/io/quarkus/backstage/model/* && \
	cp -r model-generator/builder-generator/target/generated-sources/annotations/io/quarkus/backstage/model/* model-generator/model/src/main/java/io/quarkus/backstage/model/

build:
	mvn clean install

all: builders build
