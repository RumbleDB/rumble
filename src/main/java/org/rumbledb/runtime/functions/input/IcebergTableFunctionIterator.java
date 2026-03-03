package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.update.primitives.Collection;
import org.rumbledb.runtime.update.primitives.Mode;
import sparksoniq.spark.SparkSessionManager;

import java.util.List;

public class IcebergTableFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public IcebergTableFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator collectionNameIterator = this.children.get(0);
        String userTableName = collectionNameIterator.materializeFirstItemOrNull(context).getStringValue();

        // Resolve to the configured Iceberg catalog (default is the first one passed to withIceberg()).
        String collectionName = new Collection(Mode.ICEBERG, userTableName).getLogicalName();
        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().table(collectionName);
        return DeltaTableFunctionIterator.postProcess(dataFrame, collectionName);
    }
}
