package io.quarkiverse.backstage.deployment.devservices;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPorts;

import java.util.Optional;

import org.jboss.logging.Logger;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import com.github.dockerjava.api.command.InspectContainerResponse;

import io.quarkiverse.backstage.common.utils.Github;
import io.quarkus.jgit.deployment.GiteaDevServiceInfoBuildItem;

class BackstageContainer extends GenericContainer<BackstageContainer> {

    /**
     * Logger which will be used to capture container STDOUT and STDERR.
     */
    private static final Logger log = Logger.getLogger(BackstageContainer.class);

    private static final int HTTP_PORT = 7007;

    private BackstageDevServicesConfig devServiceConfig;

    BackstageContainer(BackstageDevServicesConfig devServiceConfig, Optional<GiteaDevServiceInfoBuildItem> giteaServiceInfo) {
        super(devServiceConfig.image());
        this.devServiceConfig = devServiceConfig;
        withEnv("BACKSTAGE_TOKEN", devServiceConfig.token());
        Github.getToken().ifPresent(token -> withEnv("GITHUB_TOKEN", token));
        giteaServiceInfo.ifPresent(giteaInfo -> {
            giteaInfo.sharedNetworkHost().ifPresent(host -> {
                int port = giteaInfo.sharedNetworkHttpPort().orElse(3000);
                withEnv("GITEA_HOST", host + ":" + port);
                withEnv("GITEA_BASE_URL", "http://" + host + ":" + port);
            });
            withEnv("GITEA_USERNAME", giteaInfo.adminUsername());
            withEnv("GITEA_PASSWORD", giteaInfo.adminPassword());
        });
        withExposedPorts(HTTP_PORT);
        waitingFor(forListeningPorts(HTTP_PORT));
        withStartupAttempts(2);
        withReuse(true);
        withNetwork(Network.SHARED);
        withNetworkAliases("backstage");
        devServiceConfig.httpPort().ifPresent(port -> addFixedExposedPort(port, HTTP_PORT));
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo, boolean reused) {
    }

    public int getHttpPort() {
        return getMappedPort(HTTP_PORT);
    }

    public String getSharedHost() {
        return "backstage";
    }

    public int getSharedHttpPort() {
        return HTTP_PORT;
    }

    public String getHttpUrl() {
        return "http://" + getHost() + ":" + getMappedPort(HTTP_PORT);
    }
}
