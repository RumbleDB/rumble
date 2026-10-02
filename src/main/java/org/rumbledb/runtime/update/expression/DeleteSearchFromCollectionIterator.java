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
package org.rumbledb.runtime.update.expression;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import org.rumbledb.spark.SparkSessionManager;

public class DeleteSearchFromCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ItemRuntimePlan contentIterator;

    public DeleteSearchFromCollectionIterator(ItemRuntimePlan contentIterator, RuntimeStaticContext staticContext) {
        super(
                Arrays.asList(contentIterator),
                staticContext.toBuilder().isUpdating(true).build());
        this.contentIterator = contentIterator;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();

        if (this.contentIterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
            // DataFrame case
            Dataset<Row> contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
            List<Row> rows = contentDF.collectAsList();

            if (rows.isEmpty()) {
                // Not throwing an error for empty deletion
                return null;
            }

            Collection collection = new Collection(rows.get(0).getAs(SparkSessionManager.tableLocationColumnName));
            for (Row row : rows) {
                UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                        collection,
                        row.getAs(SparkSessionManager.rowOrderColumnName),
                        this.getRuntimeStaticContext().getMetadata());
                pul.addUpdatePrimitive(up);
            }
        } else if (this.contentIterator
                .getRuntimeStaticContext()
                .getExecutionMode()
                .isRDD()) {
            // TODO: habndle RDD case
        } else {
            // Local case
            for (Item item : this.contentIterator.materialize(context)) {
                // checks : not 0, not >1 (in try-catch) - is object/array (generated error)
                UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                        item.getCollection(),
                        item.getTopLevelOrder(),
                        this.getRuntimeStaticContext().getMetadata());
                pul.addUpdatePrimitive(up);
            }
        }

        return pul;
    }
}
