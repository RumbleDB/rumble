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
        if (!contentIterator.isDataFrame()) {
            System.out.println("##" + contentIterator);
            throw new CannotResolveUpdateSelectorException(
                    "The given content does not conform to a dataframe",
                    this.getMetadata()
            );
        }

        if (!targetIterator.isDataFrame()) {
            throw new CannotResolveUpdateSelectorException(
                    "The given target does not conform to a dataframe",
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
        Dataset<Row> targetDF = this.targetIterator.getDataFrame(context).getDataFrame();

        long contentCount = contentDF.count();
        long targetCount = targetDF.count();

        if (contentCount != 1) {
            throw new InvalidUpdateTargetException(
                    "Exactly one content must be specified for edit, but " + contentCount + " found",
                    this.getMetadata()
            );
        }
        if (targetCount != 1) {
            throw new InvalidUpdateTargetException(
                    "Exactly one target must be specified for edit, but " + targetCount + " found",
                    this.getMetadata()
            );
        }

        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createEditTuplePrimitive(targetDF, contentDF, this.getMetadata());
        pul.addUpdatePrimitive(up);
        return pul;
    }

}
