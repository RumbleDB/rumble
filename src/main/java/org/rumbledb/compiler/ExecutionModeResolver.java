/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.compiler;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.UserDefinedFunctionExecutionModes;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.module.Module;

import static org.rumbledb.compiler.CompilationDiagnostics.debugPrintTree;

/**
 * Resolves execution modes after type-dependent rewrites. The first pass registers functions,
 * intermediate passes propagate modes until resolved or stalled, and a final strict pass follows
 * the local fallback for unresolved functions. The local-only path skips this fixed-point process.
 */
@Log4j2(topic = "org.rumbledb.compiler.VisitorHelpers")
final class ExecutionModeResolver {

    private ExecutionModeResolver() {}

    static void resolve(Module module, RumbleConfiguration conf, ExternalBindings externalBindings) {
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }
        if (!conf.runtime().useParallelExecution()) {
            LocalExecutionModeVisitor visitor = new LocalExecutionModeVisitor(conf);
            visitor.visit(module, module.getStaticContext());
            if (conf.debug().printIteratorTree()) {
                debugPrintTree(module, conf);
            }
            if (module.numberOfUnsetExecutionModes() > 0) {
                log.warn(
                        "[WARNING] Some execution modes could not be set. The query may still work, but we would welcome a bug report.");
            }
            return;
        }
        ExecutionModeVisitor visitor = new ExecutionModeVisitor(conf, externalBindings);
        visitor.visit(module, module.getStaticContext());

        visitor.setVisitorConfig(VisitorConfig.executionModeIntermediatePassConfig);
        int prevUnsetCount = module.numberOfUnsetExecutionModes();
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }

        while (true) {
            visitor.visit(module, module.getStaticContext());
            int currentUnsetCount = module.numberOfUnsetExecutionModes();

            if (currentUnsetCount == 0) {
                break;
            }

            if (currentUnsetCount > prevUnsetCount) {
                throw new OurBadException(
                        "Unexpected program state reached while performing execution-mode resolution.");
            }
            if (currentUnsetCount == prevUnsetCount) {
                setLocalExecutionForUnsetUserDefinedFunctions(
                        module.getStaticContext().getUserDefinedFunctionsExecutionModes());
                break;
            }
            prevUnsetCount = currentUnsetCount;
        }

        visitor.setVisitorConfig(VisitorConfig.executionModeFinalPassConfig);
        visitor.visit(module, module.getStaticContext());
        if (conf.debug().printIteratorTree()) {
            debugPrintTree(module, conf);
        }
        if (module.numberOfUnsetExecutionModes() > 0) {
            log.warn(
                    "[WARNING] Some execution modes could not be set. The query may still work, but we would welcome a bug report.");
        }
    }

    private static void setLocalExecutionForUnsetUserDefinedFunctions(
            UserDefinedFunctionExecutionModes userDefinedFunctionExecutionModes) {
        try {
            List<FunctionIdentifier> unsetFunctionIdentifiers = new ArrayList<>();
            unsetFunctionIdentifiers.addAll(
                    userDefinedFunctionExecutionModes.getUserDefinedFunctionIdentifiersWithUnsetExecutionModes());
            for (FunctionIdentifier functionIdentifier : unsetFunctionIdentifiers) {
                userDefinedFunctionExecutionModes.setExecutionMode(functionIdentifier, ExecutionMode.LOCAL, true, null);
            }
        } catch (DuplicateFunctionIdentifierException e) {
            throw new OurBadException(
                    "Unexpected program state reached while setting local execution for unset user defined functions.");
        }
    }
}
