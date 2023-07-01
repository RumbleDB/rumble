package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.spark.SparkSessionManager;

import java.net.URI;
import java.util.List;

import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.monotonically_increasing_id;

public class DeltaFileFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public DeltaFileFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator urlIterator = this.children.get(0);
        urlIterator.open(context);
        String url = urlIterator.next().getStringValue();
        urlIterator.close();
        URI uri = FileSystemUtil.resolveURI(this.staticURI, url, getMetadata());
        if (!FileSystemUtil.exists(uri, context.getRumbleRuntimeConfiguration(), getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }
        // DeltaTable deltaTable = DeltaTable.forPath(SparkSessionManager.getInstance().getOrCreateSession(),
        // uri.toString());
        SparkSessionManager.getInstance()
            .getOrCreateSession()
            .read()
            .format("delta")
            .load(uri.toString())
            .withColumn("rowID", monotonically_increasing_id())
            .write()
            .format("delta")
            .mode("overwrite")
            .option("overwriteSchema", true)
            .save(uri.toString());
        Dataset<Row> dataFrame = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .read()
            .format("delta")
            .load(uri.toString());
        dataFrame = dataFrame.withColumn("mutabilityLevel", lit(0));
        dataFrame = dataFrame.withColumn("rowID", monotonically_increasing_id());
        dataFrame = dataFrame.withColumn("pathIn", lit(""));
        dataFrame = dataFrame.withColumn("tableLocation", lit(uri.toString()));
        // TODO: Make unique DeltaTable code
        return new JSoundDataFrame(dataFrame);
    }
}
