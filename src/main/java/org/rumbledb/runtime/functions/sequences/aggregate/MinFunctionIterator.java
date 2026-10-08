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
package org.rumbledb.runtime.functions.sequences.aggregate;

import java.io.Serial;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.plan.NativeQueryRuntimePlan;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.SequenceType;

public class MinFunctionIterator extends AbstractAtMostOneItemRuntimePlan implements NativeQueryRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan iterator;

    public MinFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
        this.iterator = this.getChild(0);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        if (!this.iterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
            return ExtremumEvaluation.min(this.iterator, getCollationPlan(), context, getMetadata());
        }
        ExtremumEvaluation.validateCollation(getCollationPlan(), context, getMetadata());

        if (this.iterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            HomogeneousItemDataFrame df = ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(this.iterator, context);
            if (df.isEmptySequence()) {
                return null;
            }
            String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
            HomogeneousItemDataFrame minDF = df.evaluateSQL(
                    String.format(
                            "SELECT MIN(`%s`) as `%s` FROM %s",
                            SparkSessionManager.nonObjectJSONiqItemColumnName,
                            SparkSessionManager.nonObjectJSONiqItemColumnName,
                            input),
                    df.getItemType());
            return minDF.getExactlyOneItem();
        }

        return ExtremumEvaluation.minRDD(this.iterator.getRDD(context), getMetadata());
    }

    private ItemRuntimePlan getCollationPlan() {
        return this.getChildren().size() > 1 ? this.getChild(1) : null;
    }

    @Override
    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.getChild(0) instanceof VariableReferenceIterator expression) {
            Map<Name, DynamicContext.VariableDependency> result = new TreeMap<>();
            result.put(expression.getVariableName(), DynamicContext.VariableDependency.MIN);
            return result;
        }
        return super.getVariableDependencies();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.getChildren().size() > 1) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext nativeChildQuery = NativeQueryRuntimePlan.generate(this.getChild(0), nativeClauseContext);
        if (nativeChildQuery == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!SequenceType.Arity.OneOrMore.isSubtypeOf(
                nativeChildQuery.getResultingType().getArity())) {
            return NativeClauseContext.NoNativeQuery;
        }
        return new NativeClauseContext(
                nativeChildQuery,
                "array_min(" + nativeChildQuery.getResultingQuery() + ")",
                nativeChildQuery.getResultingType());
    }
}
