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
package org.rumbledb.runtime.functions.input;

import java.io.Serial;
import java.util.List;
import java.util.Optional;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import org.rumbledb.api.Item;
import org.rumbledb.compiler.typing.ReturnTypeContext;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.StringLiteralExpression;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.primitives.HudiTables;
import org.rumbledb.spark.SparkSessionManager;
import org.rumbledb.types.ItemTypeFactory;
import org.rumbledb.types.SequenceType;

public class HudiTableFunctionIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public HudiTableFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        ItemRuntimePlan collectionNameIterator = this.getChild(0);
        String collectionName =
                collectionNameIterator.materializeFirstOrNull(context).getStringValue();
        HudiTables.assertHudiTable(collectionName);

        Dataset<Row> dataFrame =
                SparkSessionManager.getInstance().getOrCreateSession().table(collectionName);
        return DeltaTableFunctionIterator.postProcess(
                HudiTables.dropMetaColumns(dataFrame), HudiTables.encodeTableLocation(collectionName));
    }

    public static Optional<SequenceType> inferReturnType(FunctionCallExpression call, ReturnTypeContext context) {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        // Only a Hudi table gets a precise type. Anything else is reported by the runtime check.
        if (!(call.getArguments().get(0) instanceof StringLiteralExpression name)
                || !HudiTables.isHudiTable(name.getValue())) {
            return Optional.empty();
        }

        return Optional.of(new SequenceType(
                ItemTypeFactory.createItemType(HudiTables.dropMetaColumns(
                        session.read().table(name.getValue()).schema())),
                SequenceType.Arity.ZeroOrMore));
    }
}
