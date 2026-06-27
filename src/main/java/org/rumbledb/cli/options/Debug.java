package org.rumbledb.cli.options;

import org.rumbledb.config.DebugOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Debug {
    @Option(
        names = "--print-iterator-tree",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "For debugging purposes, prints out the expression tree and runtime interator tree."
    )
    private Boolean printIteratorTree;

    @Option(
        names = { "-v", "--show-error-info" },
        scope = ScopeType.INHERIT,
        negatable = true,
        description = {
            "For debugging purposes.",
            "If you want to report a bug, you can use this to get the full exception stack."
        }
    )
    private Boolean showErrorInfo;

    @Option(
        names = "--debug",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Enables debug output."
    )
    private Boolean logging;

    public DebugOptions toDebugOptions() {
        DebugOptions.DebugOptionsBuilder builder = DebugOptions.builder();

        OptionConversion.applyBooleanIfPresent(this.printIteratorTree, builder::printIteratorTree);
        OptionConversion.applyBooleanIfPresent(this.showErrorInfo, builder::showErrorInfo);
        OptionConversion.applyBooleanIfPresent(this.logging, builder::logging);

        return builder.build();
    }
}
