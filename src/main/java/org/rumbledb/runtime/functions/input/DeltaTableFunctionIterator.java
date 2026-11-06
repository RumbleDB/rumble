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

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;

import static org.apache.spark.sql.functions.lit;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.spark.SparkSessionManager;

public class DeltaTableFunctionIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public DeltaTableFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        ItemRuntimePlan collectionNameIterator = this.getChild(0);
        String collectionName =
                collectionNameIterator.materializeFirstOrNull(context).getStringValue();

        Dataset<Row> dataFrame =
                SparkSessionManager.getInstance().getOrCreateSession().table(collectionName);
        return postProcess(dataFrame, collectionName);
    }

    public static HomogeneousItemDataFrame postProcess(Dataset<Row> dataFrame, String collectionName) {
        StructField[] fields = dataFrame.schema().fields();
        boolean hasLongRowId = false;
        for (StructField field : fields) {
            if (field.name().equals(SparkSessionManager.rowIdColumnName)
                    && field.dataType().typeName().equals("long")) {
                hasLongRowId = true;
                break;
            }
        }
        boolean hasDoubleRowOrder = false;
        for (StructField field : fields) {
            if (field.name().equals(SparkSessionManager.rowOrderColumnName)
                    && field.dataType().typeName().equals("double")) {
                hasDoubleRowOrder = true;
                break;
            }
        }
        if (!hasLongRowId) {
            return new HomogeneousItemDataFrame(dataFrame);
        } else if (hasDoubleRowOrder) {
            dataFrame = dataFrame.orderBy(SparkSessionManager.rowOrderColumnName);
            dataFrame = dataFrame.withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(0));
            dataFrame = dataFrame.withColumn(SparkSessionManager.pathInColumnName, lit(""));
            dataFrame = dataFrame.withColumn(SparkSessionManager.tableLocationColumnName, lit(collectionName));
            return new HomogeneousItemDataFrame(dataFrame);
        } else {
            dataFrame = dataFrame.withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(0));
            dataFrame = dataFrame.withColumn(SparkSessionManager.pathInColumnName, lit(""));
            dataFrame = dataFrame.withColumn(SparkSessionManager.tableLocationColumnName, lit(collectionName));
            return new HomogeneousItemDataFrame(dataFrame);
        }
    }
}
