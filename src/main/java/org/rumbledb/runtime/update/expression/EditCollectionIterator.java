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

public class EditCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;
    private final RuntimeIterator contentIterator;

    public EditCollectionIterator(
            RuntimeIterator targetIterator,
            RuntimeIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator, contentIterator), staticContext);
        this.targetIterator = targetIterator;
        this.contentIterator = contentIterator;

        // TODO: For 1 item, this is a TreatIterator not conforming to DF; but for more than 1, it is DataFrame
        if (!contentIterator.canProduceDataFrame()) {
            throw new CannotResolveUpdateSelectorException(
                    "No schema could be detected by RumbleDB for the content that you are attempting to edit a collection record into. You can solve this issue by specifying a schema manually and wrapping the content in a validate expression. See https://docs.rumbledb.org/rumbledb-reference/types",
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
                    "More than one target item cannot be Edited.",
                    this.getMetadata()
            );
        } catch (NoItemException e) {
            throw new InvalidUpdateTargetException(
                    "One target item must be provided for Edit.",
                    this.getMetadata()
            );
        }


        Dataset<Row> contentDF = this.contentIterator.getOrCreateDataFrame(context).getDataFrame();

        long contentCount = contentDF.count();

        if (contentCount != 1) {
            throw new InvalidUpdateTargetException(
                    "Exactly one content must be specified for edit, but " + contentCount + " found",
                    this.getMetadata()
            );
        }

        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createEditTuplePrimitive(targetItem, contentDF, this.getMetadata());
        pul.addUpdatePrimitive(up);
        return pul;
    }

}
