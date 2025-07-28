package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
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

import java.util.Arrays;

public class InsertSearchIntoCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;
    private final RuntimeIterator contentIterator;
    private boolean isBefore;

    public InsertSearchIntoCollectionIterator(
            RuntimeIterator targetIterator,
            RuntimeIterator contentIterator,
            boolean isBefore,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator, contentIterator), staticContext);
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;
        this.isBefore = isBefore;

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
        Dataset<Row> contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        Item target = null;
        try {
            target = this.targetIterator.materializeExactlyOneItem(context);
        } catch (MoreThanOneItemException e) {
            throw new InvalidUpdateTargetException(
                    "More than one target item cannot be used for insertion.",
                    this.getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "One target item must be provided for search based insertion. Please check if the target expression provided resolves to a valid target in the collection.",
                    this.getMetadata()
            );
        }

        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = null;
        if (this.isBefore) {
            up = factory.createInsertBeforeIntoCollectionPrimitive(
                target,
                contentDF,
                this.getMetadata()
            );
        } else {
            up = factory.createInsertAfterIntoCollectionPrimitive(
                target,
                contentDF,
                this.getMetadata()
            );
        }
        pul.addUpdatePrimitive(up);
        return pul;
    }

}
