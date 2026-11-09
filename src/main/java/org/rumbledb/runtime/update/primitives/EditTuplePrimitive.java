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
package org.rumbledb.runtime.update.primitives;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.lit;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.spark.SparkSessionManager;

public class EditTuplePrimitive implements UpdatePrimitive {
    private final Item target;
    private Dataset<Row> contents;
    // private Row targetRow;
    private final Collection collection;

    public EditTuplePrimitive(Item target, Dataset<Row> contents, ExceptionMetadata metadata) {
        this.target = target;
        this.contents = contents;
        this.collection = target.getCollection();
    }

    @Override
    public boolean isEditTuple() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.collection.getPhysicalName();
    }

    @Override
    public double getRowOrder() {
        return this.target.getTopLevelOrder();
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public Dataset<Row> getContentDataFrame() {
        return this.contents;
    }

    @Override
    public Item getTarget() {
        return this.target;
    }

    @Override
    public void apply() {
        applyDelta();
    }

    @Override
    public void applyItem() {
        return;
    }

    @Override
    public void applyDelta() {
        String collectionPath = this.getCollectionPath();
        long targetRowID = this.target.getTopLevelID();
        double targetRowOrder = this.target.getTopLevelOrder();

        this.contents = this.contents
                .withColumn(SparkSessionManager.rowIdColumnName, lit(targetRowID))
                .withColumn(SparkSessionManager.rowOrderColumnName, lit(targetRowOrder));

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        String deleteQuery = String.format(
                "DELETE FROM %s WHERE %s = %d", collectionPath, SparkSessionManager.rowIdColumnName, targetRowID);
        session.sql(deleteQuery);

        // Insert back the edited tuple
        this.collection.insertUnordered(this.contents);
    }
}
