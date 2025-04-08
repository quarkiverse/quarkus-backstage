package io.quarkiverse.backstage.cli.template;

import static io.quarkiverse.backstage.cli.common.Properties.displayProperty;
import static io.quarkiverse.backstage.cli.common.Properties.extractValues;
import static io.quarkiverse.backstage.common.utils.Maps.flattenOptionals;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.quarkiverse.backstage.cli.common.BackstageClientAwareCommand;
import io.quarkiverse.backstage.client.BackstageClient;
import io.quarkiverse.backstage.common.utils.Serialization;
import io.quarkiverse.backstage.scaffolder.v1beta3.Parameter;
import picocli.CommandLine.Command;
import picocli.CommandLine.ExitCode;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "info", sortOptions = false, mixinStandardHelpOptions = false, header = "Get information of Backstage Template.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class TemplateInfoCommand extends BackstageClientAwareCommand {
    @Parameters(index = "0", arity = "1..1", description = "The name of the template.")
    private String name;

    @Option(names = { "--namespace" }, description = "The namespace of the template")
    protected Optional<String> namespace = Optional.empty();

    @Option(names = {
            "--show-default-values" }, description = "Flag to show default values of the template parameters, instead of the description.")
    protected boolean showDefaultValues = false;

    public TemplateInfoCommand(BackstageClient backstageClient) {
        super(backstageClient);
    }

    @Override
    public Integer call() throws Exception {
        var template = getBackstageClient().templates().withName(name).inNamespace(namespace.orElse("default")).get();
        if (showDefaultValues) {
            Map<String, Object> defaultValues = new HashMap<>();
            for (Parameter parameter : template.getSpec().getParameters()) {
                defaultValues.putAll(extractValues(parameter.getProperties()));
            }
            String json = Serialization.asJson(flattenOptionals(defaultValues));
            System.out.println(json);
        } else {
            template.getMetadata().getUid().ifPresent(n -> System.out.println("UID: " + n));
            System.out.println("Name: " + template.getMetadata().getName());
            template.getMetadata().getNamespace().ifPresent(n -> System.out.println("Namespace: " + n));
            System.out.println("Parameters: ");
            template.getSpec().getParameters().forEach(param -> {
                param.getProperties().forEach((name, p) -> displayProperty(name, p, "  "));
            });
            System.out.println("Steps: ");
            template.getSpec().getSteps().forEach(step -> {
                System.out.println("  - " + step.getId() + ": " + step.getName() + " (" + step.getAction() + ")");
            });
        }
        return ExitCode.OK;
    }

}
