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
package org.rumbledb.compiler.backend;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.UserDefinedFunctionExecutionModes;
import org.rumbledb.exceptions.DuplicateFunctionIdentifierException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.module.Module;

import static org.rumbledb.compiler.CompilerDiagnostics.debugPrintTree;

/** Assigns execution modes using the existing initial, intermediate, and final passes. */
@Log4j2
public final class ExecutionModeInference {
    private ExecutionModeInference() {}

    public static void populateExecutionModes(
            Module module, RumbleConfiguration conf, ExternalBindings externalBindings) {
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

        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorIntermediatePassConfig);
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

            /*
             * if (conf.isPrintIteratorTree()) {
             * printTree(module, conf);
             * }
             */

            if (currentUnsetCount > prevUnsetCount) {
                throw new OurBadException(
                        "Unexpected program state reached while performing multi-pass over StaticContext.");
            }
            if (currentUnsetCount == prevUnsetCount) {
                setLocalExecutionForUnsetUserDefinedFunctions(
                        module.getStaticContext().getUserDefinedFunctionsExecutionModes());
                break;
            }
            prevUnsetCount = currentUnsetCount;
        }

        visitor.setVisitorConfig(VisitorConfig.staticContextVisitorFinalPassConfig);
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
