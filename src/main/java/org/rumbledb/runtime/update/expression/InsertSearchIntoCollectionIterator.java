package org.rumbledb.runtime.update.expression;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
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
import sparksoniq.spark.SparkSessionManager;

import java.util.Arrays;
import java.util.List;

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
            System.out.println("##" + contentIterator);
            throw new CannotResolveUpdateSelectorException(
                    "The given content does not conform to a dataframe",
                    this.getMetadata()
            );
        }

        // TODO: For 1 item, this is a TreatIterator not conforming to DF; but for more than 1, it is DataFrame
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
        List<Row> targetList = this.targetIterator.getDataFrame(context).getDataFrame().collectAsList();

        long targetCount = targetList.size();

        if (targetCount != 1) {
            throw new InvalidUpdateTargetException(
                    "Exactly one target must be specified for search based insertion " + targetCount + " found",
                    this.getMetadata()
            );
        }
        double targetRowOrder = targetList.get(0).getAs(SparkSessionManager.rowOrderColumnName);
        String collection = targetList.get(0).getAs(SparkSessionManager.tableLocationColumnName);

        SparkSession session = SparkSessionManager.getInstance().getOrCreateSession();
        String selectQuery = String.format(
            "SELECT %s from %s WHERE %s %s %f ORDER BY %s ASC LIMIT 2",
            SparkSessionManager.rowOrderColumnName,
            collection,
            SparkSessionManager.rowOrderColumnName,
            this.isBefore ? "<=" : ">=",
            targetRowOrder,
            SparkSessionManager.rowOrderColumnName
        );
        List<Row> res = session.sql(selectQuery).collectAsList();

        Double rowOrderBase, rowOrderMax;
        if (res.size() == 1) {
            if (this.isBefore) {
                rowOrderBase = -100000.0;
                rowOrderMax = res.get(0).getAs(SparkSessionManager.rowOrderColumnName);
            } else {
                rowOrderBase = res.get(0).getAs(SparkSessionManager.rowOrderColumnName);
                rowOrderMax = 100000.0;
            }
        }
        else {
            rowOrderBase = res.get(0).getAs(SparkSessionManager.rowOrderColumnName);
            rowOrderMax = res.get(1).getAs(SparkSessionManager.rowOrderColumnName);
        }

        PendingUpdateList pul = new PendingUpdateList();
        UpdatePrimitiveFactory factory = UpdatePrimitiveFactory.getInstance();
        UpdatePrimitive up = factory.createInsertTuplePrimitive(
            collection,
            contentDF,
            rowOrderBase,
            rowOrderMax,
            this.getMetadata()
        );
        pul.addUpdatePrimitive(up);
        return pul;
    }

}
