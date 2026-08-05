package org.rumbledb.cli.arguments;

import org.rumbledb.config.model.RuntimeConfig;

import picocli.CommandLine.Option;

public final class RuntimeArguments {
    @Option(
        names = "--result-size",
        paramLabel = "count",
        description = "A cap on the maximum number of items to output on the screen or to a local list."
    )
    private Integer resultSize;

    @Option(
        names = { "-c", "--materialization-cap" },
        paramLabel = "count",
        description = "A cap on the maximum number of items to materialize during the query execution for large sequences within a query."
    )
    private Integer materializationCap;

    @Option(
        names = "--native-sql-predicates",
        negatable = true,
        description = "Activates native SQL predicates when possible."
    )
    private Boolean nativeSQLPredicates;

    @Option(
        names = "--data-frame-execution-mode-detection",
        negatable = true,
        description = "Activates DataFrame execution mode detection for higher-order functions."
    )
    private Boolean dataFrameExecutionModeDetection;

    @Option(
        names = "--parallel-execution",
        negatable = true,
        description = "Activates parallel execution when possible (activated by default)."
    )
    private Boolean parallelExecution;

    @Option(
        names = "--data-frame-execution",
        negatable = true,
        description = "Activates DataFrame execution when possible."
    )
    private Boolean dataFrameExecution;

    @Option(
        names = "--native-execution",
        negatable = true,
        description = "Activates native (Spark SQL) execution when possible (activated by default)."
    )
    private Boolean nativeExecution;

    @Option(
        names = "--apply-updates",
        negatable = true,
        description = "Applies the pending update list returned by the query."
    )
    private Boolean applyUpdates;

    public RuntimeConfig toConfig() {
        RuntimeConfig.RuntimeConfigBuilder builder = RuntimeConfig.builder();
        OptionConversion.applyIntIfPresent(this.resultSize, builder::resultsSizeCap);
        OptionConversion.applyIntIfPresent(this.materializationCap, builder::materializationCap);
        OptionConversion.applyBooleanIfPresent(this.nativeSQLPredicates, builder::useNativeSQLPredicates);
        OptionConversion.applyBooleanIfPresent(
            this.dataFrameExecutionModeDetection,
            builder::detectDataFrameExecutionMode
        );
        OptionConversion.applyBooleanIfPresent(this.parallelExecution, builder::useParallelExecution);
        OptionConversion.applyBooleanIfPresent(this.dataFrameExecution, builder::useDataFrameExecution);
        OptionConversion.applyBooleanIfPresent(this.nativeExecution, builder::useNativeExecution);
        OptionConversion.applyBooleanIfPresent(this.applyUpdates, builder::shouldApplyUpdates);
        return builder.build();
    }
}
