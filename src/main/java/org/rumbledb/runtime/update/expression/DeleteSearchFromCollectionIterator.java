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
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.List;

public class DeleteSearchFromCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator contentIterator;

    public DeleteSearchFromCollectionIterator(
            RuntimeIterator contentIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(contentIterator), staticContext);
        this.contentIterator = contentIterator;

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
        Dataset<Row> contentDF = this.contentIterator.getDataFrame(context).getDataFrame();
        List<Row> rows = contentDF.collectAsList();

        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();

        if (rows.isEmpty()) {
            // Not throwing an error for empty deletion
            return null;
        }

        String collection = rows.get(0).getAs(SparkSessionManager.tableLocationColumnName);
        for (Row row : rows) {
            UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                collection,
                row.getAs(SparkSessionManager.rowOrderColumnName),
                this.getMetadata()
            );
            pul.addUpdatePrimitive(up);
        }

        return pul;
    }

}
