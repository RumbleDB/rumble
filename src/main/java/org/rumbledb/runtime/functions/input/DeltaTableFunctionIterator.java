package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.util.List;

import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;

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
        
        String selectQuery = "SELECT * FROM " + collectionName;
        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().sql(selectQuery);
        
        return new JSoundDataFrame(dataFrame);
    }
}
