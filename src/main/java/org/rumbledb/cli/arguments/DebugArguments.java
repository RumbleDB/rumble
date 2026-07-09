package org.rumbledb.cli.arguments;

import org.rumbledb.config.model.DebugConfig;

import picocli.CommandLine.Option;

public final class DebugArguments {
    @Option(
        names = "--print-iterator-tree",
        negatable = true,
        description = "For debugging purposes, prints out the expression tree and runtime interator tree."
    )
    private Boolean printIteratorTree;

    @Option(
        names = { "-v", "--show-error-info" },
        negatable = true,
        description = {
            "For debugging purposes.",
            "If you want to report a bug, you can use this to get the full exception stack."
        }
    )
    private Boolean showErrorInfo;

    @Option(
        names = "--debug",
        negatable = true,
        description = "Enables debug output."
    )
    private Boolean logging;

    @Option(
        names = "--log-level",
        paramLabel = "level",
        description = "Sets the diagnostic logging level. Valid values: off, fatal, error, warn, info, debug, trace, all."
    )
    private String logLevel;

    public DebugConfig toConfig() {
        DebugConfig.DebugConfigBuilder builder = DebugConfig.builder();

        OptionConversion.applyBooleanIfPresent(this.printIteratorTree, builder::printIteratorTree);
        OptionConversion.applyBooleanIfPresent(this.showErrorInfo, builder::showErrorInfo);
        OptionConversion.applyBooleanIfPresent(this.logging, builder::logging);
        OptionConversion.applyIfPresent(this.logLevel, builder::logLevel);

        return builder.build();
    }
}
