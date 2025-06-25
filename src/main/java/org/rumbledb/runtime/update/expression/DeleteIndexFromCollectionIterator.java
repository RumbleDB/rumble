package org.rumbledb.runtime.update.expression;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotResolveUpdateSelectorException;
import org.rumbledb.exceptions.InvalidUpdateTargetException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NoItemException;
import org.rumbledb.runtime.HybridRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.PendingUpdateList;
import org.rumbledb.runtime.update.primitives.UpdatePrimitive;
import org.rumbledb.runtime.update.primitives.UpdatePrimitiveFactory;
import sparksoniq.spark.SparkSessionManager;

import java.util.Collections;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteIndexFromCollectionIterator extends HybridRuntimeIterator {

    private static final long serialVersionUID = 1L;
    private final RuntimeIterator targetIterator;
    private final int numDelete;
    private final boolean isFirst;
    private final boolean isTable;

    public DeleteIndexFromCollectionIterator(
        RuntimeIterator targetIterator,
        int numDelete,
        boolean isFirst,
        boolean isTable,
        RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(targetIterator), staticContext);
        this.targetIterator = targetIterator;
        this.numDelete = numDelete;
        this.isFirst = isFirst;
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
        return false;
    }

    @Override
    protected Item nextLocal() {
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
            throw new InvalidUpdateTargetException("The collection name must be a string, but more than one item was provided.",
                     this.getMetadata()
            );
        }
        catch (NoItemException e) {
            throw new InvalidUpdateTargetException("The collection name must be a string, but no item was provided.",
                     this.getMetadata()
            );
        }

        if(! targetItem.isString()) {
            throw new InvalidUpdateTargetException("Expecting collection name as a String, but it was: " 
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


        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        String selectQuery = String.format(
            "SELECT %s FROM %s ORDER BY %s %s LIMIT %d",
            SparkSessionManager.rowOrderColumnName,
            collection,
            SparkSessionManager.rowOrderColumnName,
            this.isFirst ? "ASC" : "DESC",
            this.numDelete
        );
        System.out.println("##"+selectQuery);
        List<Row> rows = session.sql(selectQuery).collectAsList();

        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        for (Row row: rows) {
            double rowOrder = row.getAs(SparkSessionManager.rowOrderColumnName);
            UpdatePrimitive up = factory.createDeleteTupleFromCollectionPrimitive(
                collection, 
                rowOrder,
                this.getMetadata()
            );
            pul.addUpdatePrimitive(up);
        }
        
        return pul;
    }

}