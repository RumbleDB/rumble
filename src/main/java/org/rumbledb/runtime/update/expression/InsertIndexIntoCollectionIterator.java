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
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;

public class InsertIndexIntoCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;
    private final RuntimeIterator contentIterator;
    private final boolean isTable;
    private final Integer pos;
    private final boolean isFirst;
    private final boolean isLast;

    public InsertIndexIntoCollectionIterator(
            RuntimeIterator targetIterator,
            RuntimeIterator contentIterator,
            boolean isTable,
            Integer pos,
            boolean isFirst,
            boolean isLast,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator, contentIterator), staticContext);
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.isTable = isTable;
        this.pos = pos;
        this.isFirst = isFirst;
        this.isLast = isLast;

        if (!contentIterator.isDataFrame()) {
            throw new CannotResolveUpdateSelectorException(
                    "The given content does not conform to a dataframe",
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
        // TODO: Ascertain this
        return false;
    }

    @Override
    protected Item nextLocal() {
        // TODO: Check for this
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

        String collection = null;
        if (this.isTable) {
            collection = targetItem.getStringValue();
        } else {
            collection = "delta.`" + targetItem.getStringValue() + "` ";
        }

        Dataset<Row> contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = null;
        if (this.isLast) {
            up = factory.createInsertLastIntoCollectionPrimitive(collection, contentDF, this.getMetadata());
        } else if (this.isFirst) {
            up = factory.createInsertFirstIntoCollectionPrimitive(collection, contentDF, this.getMetadata());
        } else {
            Item targetMetadataItem = new ObjectItem();
            SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
            String selectQuery = String.format(
                "SELECT rowOrder FROM %s ORDER BY rowOrder ASC OFFSET %d LIMIT 1",
                collection,
                this.pos - 1
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
