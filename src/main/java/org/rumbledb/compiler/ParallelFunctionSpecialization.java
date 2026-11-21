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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rumbledb.bindings.ExternalBindings;
import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.postfix.DynamicFunctionCallExpression;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.runtime.functions.FunctionCallArgumentConversion;
import org.rumbledb.runtime.functions.FunctionPlanVariant;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.types.SequenceType;

/** Compile-time specializations; no syntax trees or compilation state escape into function values. */
final class ParallelFunctionSpecialization {
    private ParallelFunctionSpecialization() {}

    static void prepare(MainModule module, RumbleConfiguration configuration) {
        if (!configuration.runtime().useParallelExecution()) {
            return;
        }
        boolean needed = new AbstractNodeVisitor<Boolean>() {
            @Override
            protected Boolean defaultAction(Node node, Boolean argument) {
                if (node instanceof DynamicFunctionCallExpression call) {
                    for (Expression expression : call.getArguments()) {
                        if (expression != null
                                && expression
                                        .getHighestExecutionMode(VisitorConfig.RUNTIME_PLAN_GENERATION)
                                        .isRDDOrDataFrame()) {
                            return true;
                        }
                    }
                }
                for (Node child : node.getChildren()) {
                    if (visit(child, false)) {
                        return true;
                    }
                }
                return false;
            }
        }.visit(module, false);
        if (!needed) {
            return;
        }
        new AbstractNodeVisitor<Void>() {
            @Override
            protected Void defaultAction(Node node, Void argument) {
                if (node instanceof InlineFunctionExpression function
                        && !function.isExternal()
                        && !function.isSequential()
                        && !function.isUpdating()
                        && !function.hasTailCalls()
                        && !function.hasExitStatement()) {
                    function.setParallelSpecializationEnabled(true);
                }
                return visitDescendants(node, argument);
            }
        }.visit(module, null);
    }

    static Map<FunctionPlanVariant, ItemRuntimePlan> compile(
            InlineFunctionExpression function, RumbleConfiguration configuration) {
        if (!function.isParallelSpecializationEnabled()) {
            return null;
        }
        Map<FunctionPlanVariant, ItemRuntimePlan> variants = new HashMap<>();
        for (Map.Entry<Name, SequenceType> parameter : function.getParams().entrySet()) {
            if (FunctionCallArgumentConversion.isAtMostOne(parameter.getValue())) {
                continue;
            }
            for (ExecutionMode mode : List.of(ExecutionMode.RDD, ExecutionMode.DATAFRAME)) {
                if (mode == ExecutionMode.DATAFRAME
                        && (!configuration.runtime().useDataFrameExecution()
                                || !parameter.getValue().getItemType().isCompatibleWithDataFrames(configuration))) {
                    continue;
                }
                InlineFunctionExpression copy;
                try {
                    copy = (InlineFunctionExpression) new SpecializationCloneVisitor().visit(function, null);
                } catch (UnclonableBody e) {
                    // A new AST node without an independent clone must not mutate the original contexts.
                    continue;
                }
                new StaticContextVisitor().visit(copy, function.getStaticContext());
                StaticContext context = copy.getBody().getStaticContext();
                for (Name name : copy.getParams().keySet()) {
                    context.setVariableStorageMode(name, name.equals(parameter.getKey()) ? mode : ExecutionMode.LOCAL);
                }
                ExecutionModeVisitor modes = new ExecutionModeVisitor(configuration, ExternalBindings.empty()) {
                    @Override
                    public StaticContext visitFunctionCall(FunctionCallExpression call, StaticContext argument) {
                        if (BuiltinFunctionCatalogue.exists(call.getFunctionIdentifier(), call.getStaticContext())) {
                            return super.visitFunctionCall(call, argument);
                        }
                        // These calls are lowered to dynamic dispatch in this variant. Do not change the
                        // shared catalogue used by the normal static-call fast path.
                        visitDescendants(call, argument);
                        ExecutionMode resultMode = ExecutionMode.LOCAL;
                        if (!call.isPartialApplication() && !call.alwaysReturnsAtMostOneItem()) {
                            for (Expression arg : call.getArguments()) {
                                if (arg.getHighestExecutionMode(VisitorConfig.RUNTIME_PLAN_GENERATION)
                                        .isRDDOrDataFrame()) {
                                    resultMode = arg.getHighestExecutionMode(VisitorConfig.RUNTIME_PLAN_GENERATION);
                                    break;
                                }
                            }
                        }
                        call.setHighestExecutionMode(resultMode);
                        return argument;
                    }
                };
                modes.setVisitorConfig(VisitorConfig.EXECUTION_MODE_FINAL_PASS);
                modes.visit(copy.getBody(), context);
                ItemRuntimePlan body = new RuntimeIteratorVisitor(configuration).generateSpecializedFunctionBody(copy);
                ItemRuntimePlan callee = FunctionCallArgumentConversion.wrapArgument(
                        body,
                        function.getReturnType(),
                        "Invalid return type for function. ",
                        body.getRuntimeStaticContext());
                variants.put(new FunctionPlanVariant(parameter.getKey(), mode), callee);
            }
        }
        return variants.isEmpty() ? null : variants;
    }

    private static final class UnclonableBody extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static final class SpecializationCloneVisitor extends CloneVisitor {
        @Override
        protected Node defaultAction(Node node, Node argument) {
            throw new UnclonableBody();
        }

        @Override
        public Node visit(Node node, Node argument) {
            Node copy = super.visit(node, argument);
            if (copy == node) {
                throw new UnclonableBody();
            }
            if (node instanceof Expression source && copy instanceof Expression target) {
                target.setStaticSequenceType(source.getStaticSequenceType());
                target.setExpressionClassification(source.getExpressionClassification());
                target.setSequential(source.isSequential());
            }
            if (node instanceof Statement source && copy instanceof Statement target) {
                target.setStaticSequenceType(source.getStaticSequenceType());
                target.setSequential(source.isSequential());
            }
            return copy;
        }
    }
}
