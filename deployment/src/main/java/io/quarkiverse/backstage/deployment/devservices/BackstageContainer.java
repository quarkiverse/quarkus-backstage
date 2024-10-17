package io.quarkiverse.backstage.deployment.devservices;

import static org.testcontainers.containers.wait.strategy.Wait.forListeningPorts;

import org.jboss.logging.Logger;
import org.testcontainers.containers.GenericContainer;

import com.github.dockerjava.api.command.InspectContainerResponse;

import io.quarkiverse.backstage.common.utils.Github;

class BackstageContainer extends GenericContainer<BackstageContainer> {

    /**
     * Logger which will be used to capture container STDOUT and STDERR.
     */
    private static final Logger log = Logger.getLogger(BackstageContainer.class);

    private static final int HTTP_PORT = 7007;

    private BackstageDevServicesConfig devServiceConfig;

    BackstageContainer(BackstageDevServicesConfig devServiceConfig) {
        super(devServiceConfig.image());
        this.devServiceConfig = devServiceConfig;
        withEnv("BACKSTAGE_TOKEN", devServiceConfig.token());
        Github.getToken().ifPresent(token -> withEnv("GITHUB_TOKEN", token));
        withExposedPorts(HTTP_PORT);
        waitingFor(forListeningPorts(HTTP_PORT));
        withReuse(true);
        devServiceConfig.httpPort().ifPresent(port -> addFixedExposedPort(port, HTTP_PORT));
    }

    @Override
    protected void containerIsStarted(InspectContainerResponse containerInfo, boolean reused) {
    }

    public String getHttpUrl() {
        return "http://" + getHost() + ":" + getMappedPort(HTTP_PORT);
    }
}
