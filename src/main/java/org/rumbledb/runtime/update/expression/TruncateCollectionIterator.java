package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.commons.lang3.SerializationUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
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

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TruncateCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;

    public TruncateCollectionIterator(
        RuntimeIterator targetIterator,
        RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator), staticContext);
        this.targetIterator = targetIterator;
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
        Item collectionNameItem = null;
        try {
            collectionNameItem = this.targetIterator.materializeExactlyOneItem(context);
        }
        catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                "The collection name must be a unique string, but more than one item was provided.",
                this.getMetadata()
            );
        }
        catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                "The collection name must be a string, but no item was provided.",
                this.getMetadata()
            );
        }

        if(! collectionNameItem.isString()) {
            throw new InvalidUpdateTargetException(
                "Expecting collection name as a String, but it was: " 
                    + collectionNameItem.getDynamicType().getIdentifierString(),
                this.getMetadata()
            );
        }
        String collectionName = collectionNameItem.getStringValue();

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createTruncateCollectionPrimitive(collectionName, this.getMetadata());

        PendingUpdateList pul = new PendingUpdateList();
        pul.addUpdatePrimitive(up);
        
        return pul;
    }

}