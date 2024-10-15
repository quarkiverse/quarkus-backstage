# Quarkus Backstage

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.backstage/quarkus-backstage?logo=apache-maven&style=flat-square)](https://central.sonatype.com/artifact/io.quarkiverse.backstage/quarkus-backstage-parent)

Generate Backstage Catalog Information as part of the Quarkus build or the Quarkus CLI.

## Features

- Generate the catalog-info.yaml for the Quarkus application
- Command Line interface to install / uninstall and Component to Backstage
- Generate Backstage Template from an existing Quarkus application
- Orchestrate (configure & align) Quarkus extensions:
  - kubernetes
  - helm
  - argocd

*Note*: To fully take advantage of the orchestration feature, backstage needs to be configured accordingly.

## Requirements

### For using the CLI / Client
- A running Bacsktage installation with a known token (see: [Service to Service authentication](https://backstage.io/docs/auth/service-to-service-auth#static-tokens))

### For Catalog Info Generation
To generate the catalog-info.yaml nothing special is required. The catalog is generated without requiring connection to the backstage backend.

### For Template Generation
To generate a backstage template, at build time, the following property is required:

```
quarkus.backstage.template.generation.enabled=true
```


## Building

To build the extension use the following command:

```shell
mvn clean install
```

## Usage

To get the backstage catalog-info.yaml generated, it is needed to add the `quarkus-backstage` extension to the project.

### Add extension to your project 

To add the extension to the project, manually edit the `pom.xml` or `build.gradle` file.

#### Manually editing the `pom.xml` file

```xml
<dependency>
    <groupId>io.quarkiverse.backstage</groupId>
    <artifactId>quarkus-backstage</artifactId>
    <version>999-SNAPSHOT</version>
</dependency>
```

#### Manually editing the `build.gradle` file

```groovy
dependencies {
    implementation 'io.quarkiverse.backstage:quarkus-backstage:999-SNAPSHOT'
}
```

After this step the catalog-info.yaml will be generated in the root of the project.

### Using the CLI

The project provides a companion CLI that can be used to install / uninstall and list the backstage entities.
The CLI can be added with the following command:

```shell
quarkus plug add io.quarkiverse.backstage:quarkus-backstage-cli:999-SNAPSHOT
```

#### Setting the Backstage backend token

To talk the backstage backend, the CLI needs to know:
- The URL to the backend
- The Token used by the backend for Service to Service communication

Both can be set either using environment:
- environment variables: `QUARKUS_BACKSTAGE_URL` and `QUARKUS_BACKSTAGE_TOKEN`
- application.properties: `quarkus.backstage.url` and `quarkus.backstage.token`


### Entities

#### Regenerating the entities:

To re-triggger the file generation:

```shell
quarkus backstage entities generate
```

#### Installing the application

To install generated entities:

```shell
quarkus backstage entities install
```
To uninstall:

```shell
quarkus backstage entities uninstall
```

#### Listing entities

To list all entitties installed

```shell
quarkus backstage entities list
```

### Templates

#### Generating a Backstage Template

The backstage extension is able to generate a backstage template from an existing Quarkus application.
The generated template will include a parameterized version of the project and a template definition.

##### Generated Template

###### Parameters
The following parameters are generated:
- `componentId`: The component id that will be used in the backstage catalog
- `groupId`: The group id of the project
- `artifactId`: The artifact id of the project
- `version`: The version of the project
- `description`: The description of the project
- `name`: The name of the project
- `package`: The base package of the project
###### Steps

- `render`: Render the project template.
- `publish`: Publish the generated code to github.
- `register`: Register the component in the backstage catalog.


#### Generating a Template using the CLI

To generate a backstage template from an existing Quarkus application:

```shell
quarkus backstage template generate
```

#### Listing Backstage Templates

To list all the backstage templates:

```shell
quarkus backstage template list
```

The command will generate a template under the `.backstage/templates` directory.
The template can then be manually imported to backstage.


#### Installing a Backstage Template

The generated template can be installed to backstage using the following command:

```shell
quarkus backstage template install
```

This requires the application to be added to SCM.
The command will commit the template related files to the `backstage` branch and push it to `origin`.
The branch name and remote name can be optionally configured using the following flags.

```shell
quarkus backstage template install --branch <branch> --remote <remote>
```
