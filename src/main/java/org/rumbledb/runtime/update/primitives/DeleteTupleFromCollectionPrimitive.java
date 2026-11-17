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

import org.apache.spark.sql.SparkSession;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.spark.SparkSessionManager;

public class DeleteTupleFromCollectionPrimitive implements UpdatePrimitive {
    private final Collection collection;
    private final double rowOrder;

    @SuppressWarnings("unused")
    private ExceptionMetadata metadata;

    public DeleteTupleFromCollectionPrimitive(Collection collection, double rowOrder, ExceptionMetadata metadata) {
        this.collection = collection;
        this.rowOrder = rowOrder;
    }

    @Override
    public boolean isDeleteTuple() {
        return true;
    }

    @Override
    public String getCollectionPath() {
        return this.collection.getPhysicalName();
    }

    @Override
    public double getRowOrder() {
        return this.rowOrder;
    }

    @Override
    public boolean hasSelector() {
        return false;
    }

    @Override
    public void apply() {
        applyDelta();
    }

    @Override
    public void applyItem() {
        // The name of the collection is a string Item, therefore not required
        // throw new Exception("Apply Item not implemented for Create Collection");
        return;
    }

    @Override
    public void applyDelta() {
        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();

        String deleteQuery = String.format(
                "DELETE FROM %s WHERE %s = %s",
                this.collection.getPhysicalName(),
                SparkSessionManager.rowOrderColumnName,
                String.valueOf(this.rowOrder));
        session.sql(deleteQuery);
    }
}
