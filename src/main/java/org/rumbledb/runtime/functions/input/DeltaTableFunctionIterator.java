package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import static org.apache.spark.sql.functions.lit;

import java.util.List;


public class DeltaTableFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public DeltaTableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator collectionNameIterator = this.children.get(0);
        String collectionName = collectionNameIterator.materializeFirstItemOrNull(context).getStringValue();

        String selectQuery = "SELECT * FROM " + collectionName + " ORDER BY " + SparkSessionManager.rowOrderColumnName;
        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().sql(selectQuery);
        dataFrame = dataFrame.withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(0));
        dataFrame = dataFrame.withColumn(SparkSessionManager.pathInColumnName, lit(""));
        dataFrame = dataFrame.withColumn(SparkSessionManager.tableLocationColumnName, lit(collectionName));

        // dataFrame.show();

        return new JSoundDataFrame(dataFrame);
    }
}
