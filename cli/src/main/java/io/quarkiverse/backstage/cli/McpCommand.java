package io.quarkiverse.backstage.cli;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Callable;

import jakarta.inject.Inject;

import io.quarkiverse.backstage.cli.mcp.BackstageMcp;
import io.quarkiverse.backstage.cli.mcp.StdioMcpMessageHandler;
import io.quarkiverse.mcp.server.runtime.config.McpRuntimeConfig;
import io.quarkus.runtime.Quarkus;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "mcp", sortOptions = false, mixinStandardHelpOptions = false, header = "Start backstage MCP server.", headerHeading = "%n", commandListHeading = "%nCommands:%n", synopsisHeading = "%nUsage: ", optionListHeading = "%nOptions:%n")
public class McpCommand implements Callable<Integer> {

    @Spec
    protected CommandSpec spec;

    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Display this help message.")
    public boolean help;

    @Inject
    StdioMcpMessageHandler mcpMessageHandler;

    @Inject
    McpRuntimeConfig mcpRuntimeConfig;

    @Inject
    BackstageMcp backstageMcp;

    @Override
    public Integer call() {
        try {
            PrintStream stdout = System.out;
            System.setOut(new PrintStream(OutputStream.nullOutputStream()));
            mcpMessageHandler.initialize(stdout);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Quarkus.waitForExit();
        return CommandLine.ExitCode.OK;
    }
}
