package org.rumbledb.cli.options;

import org.rumbledb.config.ExecutionOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Execution {
    @Option(
        names = "--native-sql-predicates",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates native SQL predicates when possible."
    )
    private boolean nativeSQLPredicates;

    @Option(
        names = "--data-frame-execution-mode-detection",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates DataFrame execution mode detection for higher-order functions."
    )
    private boolean dataFrameExecutionModeDetection;

    @Option(
        names = "--parallel-execution",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates parallel execution when possible (activated by default)."
    )
    private boolean parallelExecution;

    @Option(
        names = "--data-frame-execution",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates DataFrame execution when possible."
    )
    private boolean dataFrameExecution;

    @Option(
        names = "--native-execution",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates native (Spark SQL) execution when possible (activated by default)."
    )
    private boolean nativeExecution;

    @Option(
        names = "--function-inlining",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates function inlining for non-recursive functions (activated by default)."
    )
    private boolean functionInlining;

    @Option(
        names = "--tail-call-optimization",
        scope = ScopeType.INHERIT,
        negatable = true,
        defaultValue = "true",
        description = "Activates tail call optimization."
    )
    private boolean tailCallOptimization;

    @Option(
        names = "--apply-updates",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Applies the pending update list returned by the query."
    )
    private boolean applyUpdates;

    public ExecutionOptions toExecutionOptions() {
        return ExecutionOptions.builder()
            .nativeSQLPredicates(this.nativeSQLPredicates)
            .dataFrameExecutionModeDetection(this.dataFrameExecutionModeDetection)
            .parallelExecution(this.parallelExecution)
            .dataFrameExecution(this.dataFrameExecution)
            .nativeExecution(this.nativeExecution)
            .functionInlining(this.functionInlining)
            .tailCallOptimization(this.tailCallOptimization)
            .applyUpdates(this.applyUpdates)
            .build();
    }
}
