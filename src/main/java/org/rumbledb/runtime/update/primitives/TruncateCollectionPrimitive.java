package org.rumbledb.runtime.update.primitives;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import sparksoniq.spark.SparkSessionManager;

import java.net.URI;

import org.apache.spark.sql.SparkSession;

public class TruncateCollectionPrimitive implements UpdatePrimitive {
    private final Collection collection;
    private ExceptionMetadata metadata;
    private RumbleRuntimeConfiguration configuration;

    public TruncateCollectionPrimitive(
            Collection collection,
            ExceptionMetadata metadata,
            RumbleRuntimeConfiguration configuration
    ) {
        this.collection = collection;
        this.metadata = metadata;
        this.configuration = configuration;
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
            URI collectionURI = FileSystemUtil.resolveURIAgainstWorkingDirectory(
                collection.getLogicalName(),
                configuration,
                metadata
            );
            FileSystemUtil.delete(collectionURI, this.configuration, this.metadata);
            return;
        }

        String tableName = this.collection.getLogicalName();
        if (mode == Mode.ICEBERG) {
            tableName = this.collection.getPhysicalName();
        }

        // Table not found
        if (!session.catalog().tableExists(tableName)) {
            throw new CannotRetrieveResourceException(
                    "Table "
                        + this.collection.getLogicalName()
                        + " not found in "
                        + mode.toString().toLowerCase()
                        + " catalogue.",
                    this.metadata
            );
        }

        String truncateQuery = String.format(
            "DROP TABLE %s PURGE",
            tableName
        );
        session.sql(truncateQuery);
    }
}
