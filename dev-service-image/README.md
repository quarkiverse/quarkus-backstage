# Backstage App Docker Image

This Docker image contains a customized [Backstage](https://backstage.io/) application, providing a powerful developer portal that integrates various tools and services, such as Gitea, GitHub, ArgoCD, and Kubernetes. 
The image is easily customized via environment variables to integrate with your existing tools and services.

The image can be found at:

- [Quay.io: quay.io/quarkiverse/quarkus-backstage](https://quay.io/repository/quarkiverse/quarkus-backstage)

## Features

- **Gitea Integration**: Manage your repositories on Gitea, perform Git operations, and track repository metrics.
- **GitHub Integration**: Seamlessly connect with your GitHub repositories, automate workflows, and view project information.
- **ArgoCD Integration**: Deploy and manage your applications using GitOps principles with ArgoCD.
- **Kubernetes Integration**: Monitor and manage Kubernetes clusters, deployments, and services directly from Backstage.

## Configuration

This Docker image supports a variety of configuration options via environment variables. You can configure integrations with Gitea, GitHub, ArgoCD, and Kubernetes during runtime by passing these environment variables when running the container.

### Environment Variables

#### Gitea Configuration

- `GITEA_HOST`: The host of the Gitea instance.
- `GITEA_USERNAME`: The Gitea username to authenticate with.
- `GITEA_PASSWORD`: The Gitea password to authenticate with.
  
Example:

```bash
docker run -e GITTEA_HOST=https://gitea.example.com -e GITEA_USERNAME=admin -e GITEA_PASSWORD=secret quay.io/quarkiverse/quarkus-backstage
```

#### GitHub Configuration

- `GITHUB_TOKEN`: The GitHub personal access token to authenticate with.
- `GITHUB_HOST`: The host of the GitHub instance.

Example:

```bash
docker run -e GITHUB_TOKEN=ghp_1234567890 quay.io/quarkiverse/quarkus-backstage
```

#### ArgoCD Configuration

- `ARGOCD_NAME`: The name of the ArgoCD instance.
- `ARGOCD_SERVER`: The server URL of the ArgoCD instance.
- `ARGOCD_ADMIN_USER`: The ArgoCD admin username to authenticate with.
- `ARGOCD_ADMIN_PASSWORD`: The ArgoCD admin password to authenticate with.

Example:

```bash
docker run -e ARGOCD_NAME=argocd -e ARGOCD_SERVER=https://argocd.example.com -e ARGOCD_ADMIN_USER=admin -e ARGOCD_ADMIN_PASSWORD=secret quay.io/quarkiverse/quarkus-backstage
```
