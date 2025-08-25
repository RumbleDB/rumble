package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.util.Arrays;

public class InsertIndexIntoCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;
    private final RuntimeIterator contentIterator;
    private final RuntimeIterator posIterator;
    private final boolean isTable;
    private final boolean isFirst;
    private final boolean isLast;

    public InsertIndexIntoCollectionIterator(
            RuntimeIterator targetIterator,
            RuntimeIterator contentIterator,
            RuntimeIterator posIterator,
            boolean isTable,
            boolean isFirst,
            boolean isLast,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator, contentIterator, posIterator), staticContext);
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.posIterator = posIterator;
        this.isTable = isTable;
        this.isFirst = isFirst;
        this.isLast = isLast;

        if (!contentIterator.canProduceDataFrame()) {
            throw new CannotResolveUpdateSelectorException(
                    "No schema could be detected by RumbleDB for the content that you are attempting to insert into a collection. You can solve this issue by specifying a schema manually and wrapping the content in a validate expression. See https://docs.rumbledb.org/rumbledb-reference/types",
                    this.getMetadata()
            );
        }

        this.isUpdating = true;

    }

    public InsertIndexIntoCollectionIterator(
            RuntimeIterator targetIterator,
            RuntimeIterator contentIterator,
            boolean isTable,
            boolean isFirst,
            boolean isLast,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator, contentIterator), staticContext);
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.posIterator = null;
        this.isTable = isTable;
        this.isFirst = isFirst;
        this.isLast = isLast;

        if (!contentIterator.canProduceDataFrame()) {
            throw new CannotResolveUpdateSelectorException(
                    "No schema could be detected by RumbleDB for the content that you are attempting to insert into a collection. You can solve this issue by specifying a schema manually and wrapping the content in a validate expression. See https://docs.rumbledb.org/rumbledb-reference/types",
                    this.getMetadata()
            );
        }

        this.isUpdating = true;

    }

    public boolean hasPositionIterator() {
        return false;
    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    protected void openLocal() {

    }

    @Override
    protected void closeLocal() {

    }

    @Override
    protected void resetLocal() {

    }

    @Override
    protected boolean hasNextLocal() {
        return false;
    }

    @Override
    protected Item nextLocal() {
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOneItem(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a unique string, but more than one item was provided.",
                    this.getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a unique string, but no item was provided.",
                    this.getMetadata()
            );
        }

        if (!targetItem.isString()) {
            throw new InvalidUpdateTargetException(
                    "Expecting collection name as a String, but it was: "
                        + targetItem.getDynamicType().getIdentifierString(),
                    this.getMetadata()
            );
        }

        String collection = targetItem.getStringValue();
        if (!this.isTable) {
            URI uri = FileSystemUtil.resolveURI(this.staticURI, collection, getMetadata());
            collection = uri.toString();
        }

        Dataset<Row> contentDF = this.contentIterator.getOrCreateDataFrame(context).getDataFrame();
        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = null;
        if (this.isLast) {
            up = factory.createInsertLastIntoCollectionPrimitive(
                collection,
                contentDF,
                this.isTable,
                this.getMetadata()
            );
        } else if (this.isFirst) {
            up = factory.createInsertFirstIntoCollectionPrimitive(
                collection,
                contentDF,
                this.isTable,
                this.getMetadata()
            );
        } else {
            int posInt;
            Item posItem = null;

            try {
                posItem = this.posIterator.materializeExactlyOneItem(context);
            } catch (MoreThanOneItemException e) {
                throw new InvalidUpdateTargetException(
                        "The insertion index must be a unique integer, but more than one item was provided.",
                        this.getMetadata()
                );
            } catch (NoItemException e) {
                throw new InvalidUpdateTargetException(
                        "The insertion index must be a unique integer, but no item was provided.",
                        this.getMetadata()
                );
            }

            if (!posItem.isInt()) {
                throw new InvalidUpdateTargetException(
                        "Expecting insertion index as a integer, but it was: "
                            + posItem.getDynamicType().getIdentifierString(),
                        this.getMetadata()
                );
            } else {
                posInt = posItem.getIntValue();
            }

            Item targetMetadataItem = new ObjectItem();
            SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
            if (this.isTable) {
                collection = targetItem.getStringValue();
            } else {
                collection = "delta.`" + targetItem.getStringValue() + "` ";
            }
            String selectQuery = String.format(
                "SELECT * FROM %s ORDER BY rowOrder ASC LIMIT 1 OFFSET %d",
                collection,
                posInt - 1
            );
            Row res = session.sql(selectQuery).collectAsList().get(0);
            targetMetadataItem.setMutabilityLevel(res.getAs(SparkSessionManager.mutabilityLevelColumnName));
            targetMetadataItem.setPathIn(res.getAs(SparkSessionManager.pathInColumnName));
            targetMetadataItem.setTableLocation(res.getAs(SparkSessionManager.tableLocationColumnName));
            targetMetadataItem.setTopLevelID(res.getAs(SparkSessionManager.rowIdColumnName));
            targetMetadataItem.setTopLevelOrder(res.getAs(SparkSessionManager.rowOrderColumnName));

            up = factory.createInsertBeforeIntoCollectionPrimitive(targetMetadataItem, contentDF, this.getMetadata());
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }

}
