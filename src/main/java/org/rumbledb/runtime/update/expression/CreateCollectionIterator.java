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
        
        if (!contentIterator.isDataFrame()) {
            throw new CannotResolveUpdateSelectorException(
                "The given content doesn not conform to a dataframe",
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
        PendingUpdateList pul = new PendingUpdateList();

        Item targetItem = null;
        try {
            targetItem = this.targetIterator.materializeExactlyOneItem(context);
        }
        catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                "The collection name must be a string, but more than one item was provided.",
                this.getMetadata()
            );
        }
        catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                "The collection name must be a string, but no item was provided.",
                this.getMetadata()
            );
        }

        if(! targetItem.isString()) {
            throw new InvalidUpdateTargetException("Expecting collection name as a String, but it was: " 
                                                        + targetItem.getDynamicType().getIdentifierString(),
                                                    this.getMetadata()
            );
        }

        String collectionName = targetItem.getStringValue();

        Dataset<Row> contentDF = this.contentIterator.getDataFrame(context).getDataFrame();

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();

        UpdatePrimitive up = factory.createCreateCollectionPrimitive(collectionName, contentDF, this.isTable, this.getMetadata());
        
        pul.addUpdatePrimitive(up);
        return pul;
    }

}
