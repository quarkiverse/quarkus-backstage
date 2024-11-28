# Quarkus Backstage

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.backstage/quarkus-backstage?logo=apache-maven&style=flat-square)](https://central.sonatype.com/artifact/io.quarkiverse.backstage/quarkus-backstage-parent)

An extension that simplifies the integration of Quarkus applications with [Backstage](https://backstage.io/).

## Features

- Provides a Backstage client to interact with the Backstage back-end in Java.
- Generates the catalog-info.yaml for the Quarkus application.
- Generates Backstage Template from an existing Quarkus application.
- Command-line interface for managing entities and templates:
  - generate 
  - list
  - install
  - uninstall
- Orchestrates the configuration and alignment of Quarkus extensions:
  - [Kubernetes](https://quarkus.io/guides/deploying-to-kubernetes)
  - [Helm](https://docs.quarkiverse.io/quarkus-helm/dev/index.html)
    - Expose Helm values as parameters in the template
  - [ArgoCD](https://github.com/quarkiverse/quarkus-argocd)
- Dev Service and DevUI for Backstage:
  - Provides an out-of-the-box integration of Backstage with [Gitea](https://about.gitea.com/).
  - Automatic registration of the catalog-info.yaml in the Backstage Dev Service.
  - Dev version of templates that publish to [Gitea](https://about.gitea.com/) instead of the actual remote repository.
  - Bring your own Templates and automatically install them in the Dev Service.

*Note*: To fully take advantage of the orchestration feature, backstage needs to be configured accordingly.

## Client

The client provides a Java API to interact with the Backstage back-end.
The client requires the URL to the back-end and a token for service-to-service communication.

### Creating an instance of the client

The client can be used with or without the extension, even in non Quarkus applications.

#### Instantiating the client from a regular Java app

The client is provided by the following dependency:

```xml
<dependency>
    <groupId>io.quarkiverse.backstage</groupId>
    <artifactId>quarkus-backstage-client</artifactId>
    <version>${quarkus-backstage.version}</version>
</dependency>
```

To instantiate the client one needs the URL to the back-end and a token for the service to service communication (see: [Service to Service authentication](https://backstage.io/docs/auth/service-to-service-auth#static-tokens))
After configuring the URL and token, instantiate the client as follows:

```java
BackstageClient client = BackstageClient(url, token);
```

Below are some examples of how the client can be used.

### Entities

```java

        //Entities
        List<Entity> entities = client.entities().list();
        List<Entity> filtered = client.entities().list("filter");
        Entity entity = client.entities().withKind("Component").withName("my-component").inNamespace("default").get();
        client.entities().withKind("Component").withName("my-component").inNamespace("default").refresh();
        client.entities().withKind("Component").withName("my-component").inNamespace("default");

        client.entities().withUID("my-uid").delete();
        client.entities().create(entities);
```        
### Locations
```java        
        //Locations
        List<LocationEntry> locations = client.locations().list();
        LocationEntry byId = client.locations().withId("id").get();
        LocationEntry byKindNameAndNamespace = client.locations().withKind("kind").withName("name").inNamespace("namespace").get();
        client.locations().withId("id").delete();
```
### Templates
```java

        //Template
        String id = client.templates().withName("name").instantiate(values);
        String id = client.templates().withName("name").inNamespace("namespace").instantiate(values);

```        
### Injecting an instance of the client in a Quarkus application

By adding the extension to the project, the client can be injected as a CDI bean.

```xml
<dependency>
    <groupId>io.quarkiverse.backstage</groupId>
    <artifactId>quarkus-backstage</artifactId>
    <version>${quarkus-backstage.version}</version>
</dependency>
```

Quarkus manages the client as a CDI bean when the `url` and `token` are configured in properties.

```properties
quarkus.backstage.url=https://backstage.example.com        
quarkus.backstage.token=your-token
```

The properties can also be set using environment variables:

```properties
QUARKUS_BACKSTAGE_URL=https://backstage.example.com
QUARKUS_BACKSTAGE_TOKEN=your-token
```
In either case, inject the client as follows:

```java
@Inject
BackstageClient client;
```

## Generation (catalog-info.yaml)

The extension can help users generate the `catalog-info.yaml` file for their Quarkus application.
The generation can happen:

- At build time (when adding the extension to the project)
- Using the CLI (without requiring the extension to be added to the project)

### Generating the catalog-info.yaml at build time

To generate the `catalog-info.yaml` at build time, add the `quarkus-backstage` extension to the project.

```xml
<dependency>
    <groupId>io.quarkiverse.backstage</groupId>
    <artifactId>quarkus-backstage</artifactId>
    <version>${quarkus-backstage.version}</version>
</dependency>
```

This feature is enabled out of the box and can be disabled using the following property:

```properties
quarkus.backstage.catalog.generation.enabled=false
```

Alternatively, the extension can be added using the CLI:

```shell
quarkus ext add quarkus-backstage
```

After adding the extension, the `catalog-info.yaml` will be generated on each build at the root of the project.

The feature is enabled by default and can be disabled using the following property:

```properties
quarkus.backstage.catalog.generation.enabled=false
```

### Generating the catalog-info.yaml using the CLI

The `catalog-info.yaml` can be generated using the CLI without requiring the extension to be added to the project.
This requires adding `quarkus-backstage` CLI plugin to the Quarkus CLI (see [Using the CLI](#using-the-cli)).

```shell
quarkus backstage entities generate
```

### The content of the catalog-info.yaml
The catalog-info.yaml is expected to contain:
- A Component matching the current project
- Optional API entries for the detected APIs


## Generation (Template from a Quarkus application)

Authoring templates for Backstage can be a tedious task. On top of the complexity of the dealing with placeholders, 
Backstage templates include additional steps for publishing the generated code to SCM, registering the component in the catalog, etc.

To help developers / platform engineers to quickly create and test their templates this extension provide a template generator.
The generator reverse engineers the template from an existing Quarkus application.

The generator can be use in the following ways:

- Enabled as part of the build
- Using the CLI
- Using the Dev UI

### Generating the template at build time
To generate the template at build time, add the `quarkus-backstage` extension to the project.

```xml
<dependency>
    <groupId>io.quarkiverse.backstage</groupId>
    <artifactId>quarkus-backstage</artifactId>
    <version>${quarkus-backstage.version}</version>
</dependency>
```
Alternatively, the extension can be added using the CLI:

```shell
quarkus ext add quarkus-backstage
```

The feature is disabled by default and can be enabled using the following property:

```properties
quarkus.backstage.template.generation.enabled=true
```

The generated template is placed under the `.backstage/templates` directory.

When used in conjunction with the [Dev Service], the generated template can be automatically installed using the property:

```properties
quarkus.backstage.devservices.template.installation.enabled=true
```

### Generating the template using the CLI
The template can be generated using the CLI without requiring the extension to be added to the project.
This requires that the `quarkus-backstage` CLI plugin is added to the Quarkus CLI (see [Using the CLI](#using-the-cli)).

```shell
quarkus backstage template generate
```
### Generating the template using the Dev UI
When the `quarkus-backstage` extension is added to the project, A Backstage card will be available in the Dev UI (http://localhost:8080/q/dev-ui).
The card will include a link to the template generator, that works similarly to the CLI.

### Content of the generated template

#### Steps
Each generated template includes the following steps:

- `render`: Render the project template.
- `publish`: Publish the generated code to the remote repository.
- `register`: Register the component in the backstage catalog.

#### Parameters
The parameters of the template include:
- `componentId`: The component id that is used in the backstage catalog
- `groupId`: The group id of the project
- `artifactId`: The artifact id of the project
- `version`: The version of the project
- `description`: The description of the project
- `name`: The name of the project
- `package`: The base package of the project

#### Skeleton
The skeleton of the project includes:
- build files (e.g. pom.xml, build.gradle etc)
- the src directory
- the catalog-info.yaml
- .argocd directory (if argocd is available)
- .helm directory (if helm is available)
- .kubernetes directory (if kubernetes is available)
- openapi.yaml (if available)

**Note**: Kubernetes and Helm do not output generated files in the project root, but use the `kubernetes` and `helm` directories under the build output directory instead. 
However, when `quarkus-backstage` is added to the project, the output directories change (for git ops friendliness).

### Additional Template Configuration Options

The following sections describe additional configuration options that are available for template generation

#### Endpoints

It is often desirable to optionally include extensions in the project that expose additional endpoints. 
This is a great use case for using Template parameters to control the inclusion of these extensions.
The template generator allows generating and using parameters for the following endpoints:

- **Health** 
- **Metrics**
- **Info**

The endpoints can be configured using the following properties:

```properties
quarkus.backstage.template.parameters.endpoints.health.enabled=true
quarkus.backstage.template.parameters.endpoints.metrics.enabled=true
quarkus.backstage.template.parameters.endpoints.info.enabled=true
```

When the template is generated using the CLI, the following options are available:

- **--health-endpoint**
- **--metrics-endpoint**
- **--info-endpoint**

#### Helm Values

When the project uses Helm, the values are exposed as parameters in the template.
Specifically, a new configuration parameter is added to the template containing properties that correspond to the values in the `values.yaml` file.
The feature can be disabled using the following property:

```properties
quarkus.backstage.template.parameters.helm.enabled=true
```

## Dev Service

The extension provides a Dev Service for Backstage that can be used to quickly test the integration with Backstage.
The Dev Service uses a custom minimal Backstage container that is configured to optionally, configure things like:

- Github integration
- [Gitea](https://about.gitea.com/) integration

The Dev Service can be enabled using the following property:

```properties
quarkus.backstage.devservices.enabled=true
```

When the [Dev Service](#dev-service) is started, the Backstage URL is reported in the console: 
```
2024-11-01 23:48:30,471 INFO  [io.qua.bac.dep.dev.BackstageDevServiceProcessor] (build-3) Backstage HTTP URL: http://localhost:35612
```
The URL above, can be used to access the Backstage UI.
Alternatively, the Backstage UI can be accessed from the `Backstage` card in the Dev UI: `http://localhost:8080/q/dev-ui`

If you need to access [Gitea](https://about.gitea.com/) the URL is reported in the console:

```
2024-11-05 09:04:58,723 INFO  [io.qua.jgi.dep.JGitDevServicesProcessor] (build-47) Gitea HTTP URL: http://localhost:32769
```

The default credentials for the [Gitea](https://about.gitea.com/) Dev Service are: `quarkus` / `quarkus`.
[Gitea](https://about.gitea.com/) can be accessed from the `Gitea` card in the Dev UI: `http://localhost:8080/q/dev-ui`

### Container image

The default container image used by the Dev Service is `quay.io/iocanel/backstage:latest` and can be changed using the following property:

```properties 
quarkus.backstage.devservices.image=<custom image>
```

The source code of the image can be found at [github.com/iocanel/backstage-docker](https://github.com/iocanel/backstage-docker).

#### Custom image requirements

To use a custom image, the following environment variables need to be supported: 

- BACKSTAGE_TOKEN: The token used by the Backstage back end for service to service communication
- GITHUB_TOKEN: The token used by the Backstage back end to interact with Github
- GITEA_HOST: The host of the Gitea instance
- GITEA_BASE_URL: The base URL of the Gitea instance
- GITEA_USERNAME: The username used to authenticate with Gitea
- GITEA_PASSWORD: The password used to authenticate with Gitea

When these environment variables are set, the Dev Service needs to apply them to `app-config.production.yaml` before starting Backstage.

Additionally, the Backstage instance needs to have the following plugins installed and configured:

- @backstage/plugin-scaffolder-backend-module-github'
- @backstage/plugin-scaffolder-backend-module-gitea'

Finally, it is expected that the port 7007 is used.

### Backstage Dev Service catalog

By default the catalog contains the following entities:

- A `Component` matching the current project
- Optional `API` entries for the detected APIs
- A `Location` pointing to the `Component`.

A `Location` is a special kind of entity that is used to reference other entities like `Component`, `API`, etc.
The `Location` is the only entity that can be directly installed in Backstage. All others need to be referenced as a URL by a `Location`.

This means that the entities above needs to be accessed as a URL by a `Location` entity. This is usually done by pushing them to a git repository and referencing the URL.

To avoid pushing the entities to a remote repository, the [Dev Service](#dev-service) uses another container running [Gitea](https://about.gitea.com/) (provided by the [Quarkus JGit extension](https://quarkus.io/extensions/io.quarkiverse.jgit/quarkus-jgit/)).

### Dev Template

A special version of the template, the `Dev Template` is optionally generated and installed when using the Dev Service.

The Dev Template is a variation of the generated template that is modified so that it's usable a dev time and is integrated with the Dev Service.
More specifically the `Dev Template` is configured so that it publishes to [Gitea](https://about.gitea.com/) instead of the actual remote repository.
This allows the developer to test the integration with Backstage without having to push the generated code to a remote repository.

#### Generating the Dev Template

To enable the generation of the `Dev Template` set the following property:

```properties
quarkus.backstage.dev-template.generation.enabled=true
```

### Automatic installation of the Dev Template

When the `Dev Template` is generated, it can be automatically installed in the Dev Service using the following property:

```properties
quarkus.backstage.devservices.dev-template.installation.enabled=true
```

### Bringing your own templates

It is often desirable to use custom / user provided template in the Dev Service.
This can be achieved by placing the template in the `src/main/backstage/templates` directory and enabling the following property:

```properties
quarkus.backstage.user-provided-templates.generation.enabled=true
```

After compiling the project, the template will be included in the `.backstage/templates` directory.
To automatically install the template in the Dev Service, enable the following property:

```properties
quarkus.backstage.devservices.user-provided-templates.installation.enabled=true
```

## Using the CLI

The project provides a companion CLI that can be used to install / uninstall and list the backstage entities.
The CLI can be added with the following command:

```shell
quarkus plug add io.quarkiverse.backstage:quarkus-backstage-cli:999-SNAPSHOT
```

#### Setting the Backstage back end token

To talk the backstage back end, the CLI needs to know:
- The URL to the back end
- The Token used by the back end for Service to Service communication

Both can be set either using environment:
- environment variables: `QUARKUS_BACKSTAGE_URL` and `QUARKUS_BACKSTAGE_TOKEN`
- application.properties: `quarkus.backstage.url` and `quarkus.backstage.token`

#### Connecting to the Backstage Dev Service

For ease of use, it is possible to connect the CLI to the Dev Service, without having to set the URL and token (as mentioned above).
Instead, the CLI provides the following flag `--dev-service`. Commands that support this flag, will try to connect to the Dev Service.

Connection is performed using the ephemeral file: `.quarkus/dev/backstage/<container id>.yaml` that is created by the Dev Service when created.

**Note**: This feature requires that the command is executed from within the project that is running the Dev Service.

### Entities

#### Regenerating the entities:

To re-trigger the file generation:

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

To list all entities installed

```shell
quarkus backstage entities list
```

#### Generating a Template using the CLI

To generate a backstage template from an existing Quarkus application:

```shell
quarkus backstage template generate
```

#### Generating a Backstage Template

To generate a backstage template from an existing Quarkus application:

```shell
quarkus backstage template generate
```

The command generates a template under the `.backstage/templates` directory.
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
#### Getting Template information

It is often desired to get information about a template without using the Backstage UI.
Also, its often needed to get details that are not listed in the UI.

The following command summarizes the template information:

```shell
quarkus backstage template info my-template
```
The output includes: 

- uid
- name
- namespace
- parameters
- steps

#### Instantiating Templates

To instantiate a template (create an application using the template):

```shell
quarkus backstage template instantiate my-template
```
The command above will create an application using the template `my-template` using the default values for all parameters.
A custom value file can be optionally specified using the `--values-file` flag.

```shell
quarkus backstage template instantiate my-template --value-file values.yaml
```

Where `values.yaml` is a file containing the values for the parameters.

The `values.yaml` file that contains the defaults for the parameter can be obtain using the `info` command with the `--show-default-values` flag.

```shell
quarkus backstage template info my-template --show-default-values
```
