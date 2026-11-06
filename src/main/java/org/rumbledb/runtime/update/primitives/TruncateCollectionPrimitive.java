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

import java.net.URI;

import org.apache.spark.sql.SparkSession;

import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.spark.SparkSessionManager;

public class TruncateCollectionPrimitive implements UpdatePrimitive {
    private final Collection collection;
    private ExceptionMetadata metadata;

    public TruncateCollectionPrimitive(Collection collection, ExceptionMetadata metadata) {
        this.collection = collection;
        this.metadata = metadata;
    }

    @Override
    public boolean isTruncateCollection() {
        return true;
    }

    @Override
    public String getCollectionName() {
        return this.collection.getLogicalName();
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
        Mode mode = this.collection.getMode();

        // Handle delta files
        if (mode == Mode.DELTA) {
            URI collectionURI =
                    FileSystemUtil.resolveURIAgainstWorkingDirectory(this.collection.getLogicalName(), this.metadata);
            FileSystemUtil.delete(collectionURI, this.metadata);
            return;
        }

        String tableName = this.collection.getLogicalName();

        // Table not found
        if (!session.catalog().tableExists(tableName)) {
            throw new CannotRetrieveResourceException(
                    "Table "
                            + this.collection.getLogicalName()
                            + " not found in "
                            + mode.toString().toLowerCase()
                            + " catalogue.",
                    this.metadata);
        }

        String truncateQuery = String.format("DROP TABLE %s PURGE", tableName);
        session.sql(truncateQuery);
    }
}
