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
package org.rumbledb.runtime.functions;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.context.NamedFunctions;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.cursor.AbstractLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.LocalRuntimePlan;
import org.rumbledb.runtime.plan.RDDRuntimePlan;
import org.rumbledb.runtime.plan.UpdatingRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;

public class StaticUserDefinedFunctionCallIterator extends ItemRuntimePlan
        implements LocalRuntimePlan<Item>, RDDRuntimePlan<Item>, DataFrameRuntimePlan<Item>, UpdatingRuntimePlan {
    // static: functionIdentifier known at compile time

    @Serial
    private static final long serialVersionUID = 1L;
    // parametrized fields
    private final FunctionIdentifier functionIdentifier;
    private final List<ItemRuntimePlan> functionArguments;
    private final boolean tailCallOptimizationCandidate;

    public StaticUserDefinedFunctionCallIterator(
            FunctionIdentifier functionIdentifier,
            List<ItemRuntimePlan> functionArguments,
            RuntimeStaticContext staticContext,
            boolean tailCallOptimization) {
        super(List.of(), staticContext);
        this.functionIdentifier = functionIdentifier;
        this.functionArguments = functionArguments;
        this.tailCallOptimizationCandidate = tailCallOptimization;
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new UserDefinedCallLocalCursor(
                this.functionIdentifier,
                this.functionArguments,
                this.tailCallOptimizationCandidate,
                this.staticContext,
                context);
    }

    @Override
    public JavaRDD<Item> createNativeRDD(DynamicContext dynamicContext) {
        ItemRuntimePlan call = dynamicContext
                .getNamedFunctions()
                .getUserDefinedFunctionCallIterator(
                        this.functionIdentifier,
                        this.staticContext,
                        new ArrayList<ItemRuntimePlan>(this.functionArguments),
                        false);
        return call.getRDD(dynamicContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext dynamicContext) {
        ItemRuntimePlan call = dynamicContext
                .getNamedFunctions()
                .getUserDefinedFunctionCallIterator(
                        this.functionIdentifier,
                        this.staticContext,
                        new ArrayList<ItemRuntimePlan>(this.functionArguments),
                        false);
        return ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(call, dynamicContext);
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>(super.getVariableDependencies());
        for (ItemRuntimePlan iterator : this.functionArguments) {
            if (iterator == null) {
                continue;
            }
            result.putAll(iterator.getVariableDependencies());
        }
        return result;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        if (!this.staticContext.isUpdating()) {
            return new PendingUpdateList();
        }
        ItemRuntimePlan call = context.getNamedFunctions()
                .getUserDefinedFunctionCallIterator(
                        this.functionIdentifier,
                        this.staticContext,
                        new ArrayList<ItemRuntimePlan>(this.functionArguments),
                        false);
        return UpdatingRuntimePlan.get(call, context);
    }

    private static final class UserDefinedCallLocalCursor extends AbstractLocalCursor<Item> {

        private final FunctionIdentifier functionIdentifier;
        private final List<ItemRuntimePlan> functionArguments;
        private final boolean tailCallOptimizationCandidate;
        private final RuntimeStaticContext staticContext;
        private final DynamicContext context;
        private Cursor<Item> delegate;
        private Item nextResult;

        private UserDefinedCallLocalCursor(
                FunctionIdentifier functionIdentifier,
                List<ItemRuntimePlan> functionArguments,
                boolean tailCallOptimizationCandidate,
                RuntimeStaticContext staticContext,
                DynamicContext context) {
            super(staticContext.getMetadata());
            this.functionIdentifier = functionIdentifier;
            this.functionArguments = functionArguments;
            this.tailCallOptimizationCandidate = tailCallOptimizationCandidate;
            this.staticContext = staticContext;
            this.context = context;
        }

        @Override
        protected void openLocal() {
            ItemRuntimePlan call = this.context
                    .getNamedFunctions()
                    .getUserDefinedFunctionCallIterator(
                            this.functionIdentifier,
                            this.staticContext,
                            new ArrayList<ItemRuntimePlan>(this.functionArguments),
                            this.tailCallOptimizationCandidate);
            this.delegate = call.getCursor(this.context);
            advance();
            while (!this.tailCallOptimizationCandidate && isTailCall(this.nextResult)) {
                closeDelegate();
                ItemRuntimePlan tailCall = NamedFunctions.buildFunctionItemCallIterator(
                        this.nextResult, this.staticContext, ExecutionMode.LOCAL, Collections.emptyList(), false);
                this.delegate = tailCall.getCursor(this.context);
                advance();
            }
        }

        private boolean isTailCall(Item item) {
            return item != null
                    && item.isFunction()
                    && item.getIdentifier().getArity() == 0
                    && Name.TAIL_CALL_OPTIMIZATION.equals(item.getIdentifier().getName());
        }

        private void advance() {
            this.nextResult = null;
            try {
                if (this.delegate.hasNext()) {
                    this.nextResult = this.delegate.next();
                } else {
                    closeDelegate();
                }
            } catch (RuntimeException e) {
                closeDelegate();
                throw e;
            }
        }

        @Override
        protected boolean hasNextLocal() {
            return this.nextResult != null;
        }

        @Override
        protected Item nextLocal() {
            if (this.nextResult == null) {
                throw invalidState("No more user-defined function results are available.");
            }
            Item result = this.nextResult;
            advance();
            return result;
        }

        private void closeDelegate() {
            if (this.delegate != null) {
                this.delegate.close();
                this.delegate = null;
            }
        }

        @Override
        protected void closeLocal() {
            closeDelegate();
            this.nextResult = null;
        }
    }
}
