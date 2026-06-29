package org.rumbledb.cli.options;

import org.rumbledb.config.ExecutionOptions;

import picocli.CommandLine.Option;

public final class Execution {
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
        names = "--function-inlining",
        negatable = true,
        description = "Activates function inlining for non-recursive functions (activated by default)."
    )
    private Boolean functionInlining;

    @Option(
        names = "--tail-call-optimization",
        negatable = true,
        description = "Activates tail call optimization."
    )
    private Boolean tailCallOptimization;

    @Option(
        names = "--apply-updates",
        negatable = true,
        description = "Applies the pending update list returned by the query."
    )
    private Boolean applyUpdates;

    public ExecutionOptions toExecutionOptions() {
        ExecutionOptions.ExecutionOptionsBuilder builder = ExecutionOptions.builder();

        OptionConversion.applyBooleanIfPresent(this.nativeSQLPredicates, builder::useNativeSQLPredicates);
        OptionConversion.applyBooleanIfPresent(
            this.dataFrameExecutionModeDetection,
            builder::detectDataFrameExecutionMode
        );
        OptionConversion.applyBooleanIfPresent(this.parallelExecution, builder::useParallelExecution);
        OptionConversion.applyBooleanIfPresent(this.dataFrameExecution, builder::useDataFrameExecution);
        OptionConversion.applyBooleanIfPresent(this.nativeExecution, builder::useNativeExecution);
        OptionConversion.applyBooleanIfPresent(this.functionInlining, builder::useFunctionInlining);
        OptionConversion.applyBooleanIfPresent(this.tailCallOptimization, builder::useTailCallOptimization);
        OptionConversion.applyBooleanIfPresent(this.applyUpdates, builder::shouldApplyUpdates);

        return builder.build();
    }
}
