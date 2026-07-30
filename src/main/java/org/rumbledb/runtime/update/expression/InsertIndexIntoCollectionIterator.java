package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.functions.input.FileSystemUtil;
import org.rumbledb.exceptions.CannotInferSchemaOnNonStructuredDataException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.Mode;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.net.URI;
import java.util.Arrays;

public class InsertIndexIntoCollectionIterator extends UpdatingExpressionIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> posIterator;
    private final Mode mode;
    private final boolean isFirst;
    private final boolean isLast;

    public InsertIndexIntoCollectionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> posIterator,
            Mode mode,
            boolean isFirst,
            boolean isLast,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(targetIterator, contentIterator, posIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.posIterator = posIterator;
        this.mode = mode;
        this.isFirst = isFirst;
        this.isLast = isLast;


    }

    public InsertIndexIntoCollectionIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> targetIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> contentIterator,
            Mode mode,
            boolean isFirst,
            boolean isLast,
            RuntimeStaticContext staticContext
    ) {
        super(
            Arrays.asList(targetIterator, contentIterator),
            staticContext.toBuilder().isUpdating(true).build()
        );
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.posIterator = null;
        this.mode = mode;
        this.isFirst = isFirst;
        this.isLast = isLast;


    }

    @Override
    protected JavaRDD<Item> getRDDAux(DynamicContext context) {
        return null;
    }

    @Override
    public PendingUpdateList getPendingUpdateList(DynamicContext context) {
        PendingUpdateList pul = new PendingUpdateList();
        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOne(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a unique string, but more than one item was provided.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a unique string, but no item was provided.",
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        if (!targetItem.isString()) {
            throw new InvalidUpdateTargetException(
                    "Expecting collection name as a String, but it was: "
                        + targetItem.getDynamicType().getIdentifierString(),
                    this.getRuntimeStaticContext().getMetadata()
            );
        }

        String logicalPath = targetItem.getStringValue();
        Mode mode = this.mode;
        if (mode == Mode.DELTA) {
            URI uri = FileSystemUtil.resolveFileSystemURI(
                this.staticContext.getStaticURI(),
                logicalPath,
                getMetadata()
            );
            logicalPath = FileSystemUtil.convertURIToStringForSpark(uri);
        }

        Collection collection = new Collection(mode, logicalPath);
        Dataset<Row> contentDF = null;
        try {
            contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        } catch (CannotInferSchemaOnNonStructuredDataException e) {
            e.setMetadata(getMetadata());
            throw e;
        }
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = null;
        if (this.isLast) {
            up = factory.createInsertLastIntoCollectionPrimitive(
                collection,
                contentDF,
                this.getRuntimeStaticContext().getMetadata()
            );
        } else if (this.isFirst) {
            up = factory.createInsertFirstIntoCollectionPrimitive(
                collection,
                contentDF,
                this.getRuntimeStaticContext().getMetadata()
            );
        } else {
            int posInt;
            Item posItem = null;

            try {
                posItem = this.posIterator.materializeExactlyOne(context);
            } catch (MoreThanOneItemException e) {
                throw new InvalidUpdateTargetException(
                        "The insertion index must be a unique integer, but more than one item was provided.",
                        this.getRuntimeStaticContext().getMetadata()
                );
            } catch (NoItemException e) {
                throw new InvalidUpdateTargetException(
                        "The insertion index must be a unique integer, but no item was provided.",
                        this.getRuntimeStaticContext().getMetadata()
                );
            }

            if (!posItem.isInt()) {
                throw new InvalidUpdateTargetException(
                        "Expecting insertion index as a integer, but it was: "
                            + posItem.getDynamicType().getIdentifierString(),
                        this.getRuntimeStaticContext().getMetadata()
                );
            } else {
                posInt = posItem.getIntValue();
            }

            Item targetMetadataItem = new ObjectItem();
            SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
            String selectQuery = String.format(
                "SELECT * FROM %s ORDER BY rowOrder ASC LIMIT 1 OFFSET %d",
                collection.getPhysicalName(),
                posInt - 1
            );
            Row res = session.sql(selectQuery).collectAsList().get(0);
            targetMetadataItem.setMutabilityLevel(res.getAs(SparkSessionManager.mutabilityLevelColumnName));
            targetMetadataItem.setPathIn(res.getAs(SparkSessionManager.pathInColumnName));
            // targetMetadataItem.setTableLocation(res.getAs(SparkSessionManager.tableLocationColumnName));
            targetMetadataItem.setCollection(new Collection(res.getAs(SparkSessionManager.tableLocationColumnName)));
            targetMetadataItem.setTopLevelID(res.getAs(SparkSessionManager.rowIdColumnName));
            targetMetadataItem.setTopLevelOrder(res.getAs(SparkSessionManager.rowOrderColumnName));

            up = factory.createInsertBeforeIntoCollectionPrimitive(
                targetMetadataItem,
                contentDF,
                this.getRuntimeStaticContext().getMetadata()
            );
        }

        pul.addUpdatePrimitive(up);
        return pul;
    }

}
