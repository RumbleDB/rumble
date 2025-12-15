package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
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


import java.net.URI;
import java.util.Arrays;

public class CreateCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;
    private final RuntimeIterator contentIterator;
    private boolean isTable;

    public CreateCollectionIterator(
            RuntimeIterator targetIterator,
            RuntimeIterator contentIterator,
            boolean isTable,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator, contentIterator), staticContext);
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.isTable = isTable;
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
        PendingUpdateList pul = new PendingUpdateList();
        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOneItem(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but more than one item was provided.",
                    this.getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "The collection name must be a string, but no item was provided.",
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

        String logicalPath = targetItem.getStringValue();
        Mode mode = (this.isTable) ? Mode.HIVE : Mode.DELTA;
        // If it is a delta-file() call we need to resolve the path to an absolute path.
        if (!this.isTable) {
            URI uri = FileSystemUtil.resolveURI(this.staticURI, logicalPath, getMetadata());
            logicalPath = FileSystemUtil.convertURIToStringForSpark(uri);
        }

        Collection collection = new Collection(mode, logicalPath);

        Dataset<Row> contentDF = null;
        try {
            contentDF = this.contentIterator.getOrCreateDataFrame(context).getDataFrame();
        } catch (CannotInferSchemaOnNonStructuredDataException e) {
            e.setMetadata(getMetadata());
            throw e;
        }

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createCreateCollectionPrimitive(
            collection,
            contentDF,
            this.isTable,
            this.getMetadata()
        );

        pul.addUpdatePrimitive(up);
        return pul;
    }

}
