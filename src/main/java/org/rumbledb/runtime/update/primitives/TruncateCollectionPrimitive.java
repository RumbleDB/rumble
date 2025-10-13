package org.rumbledb.runtime.update.primitives;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.runtime.functions.input.FileSystemUtil;

import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.spark.sql.SparkSession;


public class TruncateCollectionPrimitive implements UpdatePrimitive {
    private String collectionName;
    private boolean isTable;
    private ExceptionMetadata metadata;
    private RumbleRuntimeConfiguration configuration;

    public TruncateCollectionPrimitive(
            String collectionName,
            boolean isTable,
            ExceptionMetadata metadata,
            RumbleRuntimeConfiguration configuration
    ) {
        this.collectionName = collectionName;
        this.isTable = isTable;
        this.metadata = metadata;
        this.configuration = configuration;
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

        if (this.isTable) {
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
        } else {
            try {
                URI collectionURI = new URI(this.collectionName);
                if (collectionURI.getScheme() == null) {
                    collectionURI = new URI("file://" + this.collectionName);
                }
                FileSystemUtil.delete(collectionURI, this.configuration, this.metadata);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }


    }

}
