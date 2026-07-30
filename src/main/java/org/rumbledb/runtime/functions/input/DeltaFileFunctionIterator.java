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

public class DeltaFileFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public DeltaFileFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
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

        Dataset<Row> dataFrame = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .read()
            .format("delta")
            .load(FileSystemUtil.convertURIToStringForSpark(uri));

        return DeltaTableFunctionIterator.postProcess(
            dataFrame,
            "delta.`" + FileSystemUtil.convertURIToStringForSpark(uri) + "`"
        );
    }
}

