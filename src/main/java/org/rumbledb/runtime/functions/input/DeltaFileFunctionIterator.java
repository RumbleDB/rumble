package org.rumbledb.runtime.functions.input;

import java.io.Serial;
import java.net.URI;
import java.util.List;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.CannotRetrieveResourceException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.spark.SparkSessionManager;

public class DeltaFileFunctionIterator extends DataFrameRuntimeIterator {

    @Serial private static final long serialVersionUID = 1L;

    public DeltaFileFunctionIterator(
            List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator urlIterator = this.getChild(0);
        urlIterator.open(context);
        String url = urlIterator.next().getStringValue();
        urlIterator.close();
        URI uri =
                FileSystemUtil.resolveFileSystemURI(
                        this.staticContext.getStaticURI(), url, getMetadata());
        if (!FileSystemUtil.exists(uri, getMetadata())) {
            throw new CannotRetrieveResourceException("File " + uri + " not found.", getMetadata());
        }

        Dataset<Row> dataFrame =
                SparkSessionManager.getInstance()
                        .getOrCreateSession()
                        .read()
                        .format("delta")
                        .load(FileSystemUtil.convertURIToStringForSpark(uri));

        return DeltaTableFunctionIterator.postProcess(
                dataFrame, "delta.`" + FileSystemUtil.convertURIToStringForSpark(uri) + "`");
    }
}
