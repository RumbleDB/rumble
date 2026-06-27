package org.rumbledb.cli.options;

import org.rumbledb.config.ExecutionOptions;

import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

public final class Execution {
    @Option(
        names = "--native-sql-predicates",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates native SQL predicates when possible."
    )
    private Boolean nativeSQLPredicates;

    @Option(
        names = "--data-frame-execution-mode-detection",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates DataFrame execution mode detection for higher-order functions."
    )
    private Boolean dataFrameExecutionModeDetection;

    @Option(
        names = "--parallel-execution",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates parallel execution when possible (activated by default)."
    )
    private Boolean parallelExecution;

    @Option(
        names = "--data-frame-execution",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates DataFrame execution when possible."
    )
    private Boolean dataFrameExecution;

    @Option(
        names = "--native-execution",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates native (Spark SQL) execution when possible (activated by default)."
    )
    private Boolean nativeExecution;

    @Option(
        names = "--function-inlining",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates function inlining for non-recursive functions (activated by default)."
    )
    private Boolean functionInlining;

    @Option(
        names = "--tail-call-optimization",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Activates tail call optimization."
    )
    private Boolean tailCallOptimization;

    @Option(
        names = "--apply-updates",
        scope = ScopeType.INHERIT,
        negatable = true,
        description = "Applies the pending update list returned by the query."
    )
    private Boolean applyUpdates;

    public ExecutionOptions toExecutionOptions() {
        ExecutionOptions.ExecutionOptionsBuilder builder = ExecutionOptions.builder();
        
        OptionConversion.applyIfPresent(this.nativeSQLPredicates, builder::useNativeSQLPredicates);
        OptionConversion.applyIfPresent(this.dataFrameExecutionModeDetection, builder::detectDataFrameExecutionMode);
        OptionConversion.applyIfPresent(this.parallelExecution, builder::useParallelExecution);
        OptionConversion.applyIfPresent(this.dataFrameExecution, builder::useDataFrameExecution);
        OptionConversion.applyIfPresent(this.nativeExecution, builder::useNativeExecution); 
        OptionConversion.applyIfPresent(this.functionInlining, builder::useFunctionInlining);
        OptionConversion.applyIfPresent(this.tailCallOptimization, builder::useTailCallOptimization);
        OptionConversion.applyIfPresent(this.applyUpdates, builder::shouldApplyUpdates);

        return builder.build();
    }
}
