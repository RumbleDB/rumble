package org.rumbledb.runtime.update.primitives;

import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import sparksoniq.spark.SparkSessionManager;
import org.apache.spark.sql.SparkSession;


public class TruncateCollectionPrimitive implements UpdatePrimitive {
    private String collectionName;
    private ExceptionMetadata metadata;

    public TruncateCollectionPrimitive(String collectionName, ExceptionMetadata metadata) {
        this.collectionName = collectionName;
    }

    @Override
    public boolean isTruncateCollection() {
        return true;
    }

    @Override
    public String getCollectionName() {
        return this.collectionName;
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

        if (session.catalog().tableExists(this.collectionName) == false) {
            throw new CannotRetrieveResourceException(
                    "Table " + this.collectionName + " not found in hive catalogue.",
                    this.metadata
            );
        }

        String truncateQuery = String.format(
            "DROP TABLE %s PURGE",
            this.collectionName
        );
        session.sql(truncateQuery);
    }

}
