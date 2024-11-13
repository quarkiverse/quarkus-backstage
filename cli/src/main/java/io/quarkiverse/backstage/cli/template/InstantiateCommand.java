package io.quarkiverse.backstage.cli.template;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.client.model.ScaffolderEvent;
import io.quarkiverse.backstage.common.utils.Serialization;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "instantiate", sortOptions = false, mixinStandardHelpOptions = false, header = "Instantiate a Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class InstantiateCommand extends BackstageClientAwareCommand {

    @Parameters(index = "0", arity = "1..1", description = "The name of the template.")
    private String name;

    @Option(names = { "--namespace" }, description = "The namespace of the template")
    protected Optional<String> namespace = Optional.empty();

    @Option(names = { "--values-file" }, description = "The path to the value file to use for template instantiation")
    protected Optional<String> valuesFile = Optional.empty();

    public InstantiateCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public Integer call() throws Exception {
        Map<String, Object> parameters = new HashMap<>();
        valuesFile.map(Paths::get).map(p -> Serialization.unmarshal(p.toFile(), new TypeReference<Map<String, Object>>() {
        })).ifPresent(parameters::putAll);
        String taskId = getBackstageClient().templates().withName(name).inNamespace(namespace.orElse("default"))
                .instantiate(parameters);
        System.out.println("Created initialization task with ID: " + taskId);
        List<ScaffolderEvent> events = getBackstageClient().events().forTask(taskId).waitingUntilCompletion().get();
        events.forEach(e -> {
            System.out.println(e.getCreatedAt() + " " + e.getType() + " " + e.getBody().getMessage());
        });

        boolean completed = events.stream()
                .anyMatch(e -> e.getType().equalsIgnoreCase("completion") && e.getBody().getMessage().contains("completed"));
        if (!completed) {
            System.err.println("Template instantiation failed");
            return ExitCode.SOFTWARE;
        }
        System.err.println("Template instantiation completed");
        return ExitCode.OK;

    }
}
