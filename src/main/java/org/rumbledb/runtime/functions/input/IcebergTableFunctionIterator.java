package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
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
        String collectionName = collectionNameIterator.materializeFirstItemOrNull(context).getStringValue();

        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().table(collectionName);
        String metadataName = qualifyForMetadata(collectionName);
        return DeltaTableFunctionIterator.postProcess(dataFrame, metadataName);
    }

    /**
     * This method qualifies the collection name for metadata retrieval. If the collection name is already
     * qualified with a catalog and a namespace, it is returned as is.
     * If it is qualified with only a catalog, the default namespace is added.
     * If it is not qualified at all, both the default catalog and the default namespace are added.
     * This is necessary because Iceberg tables are always qualified with at least a catalog and a namespace,
     * and we need to ensure that we can retrieve the correct metadata for the table.
     */
    private static String qualifyForMetadata(String collectionName) {
        int dots = 0;
        for (int i = 0; i < collectionName.length(); i++) {
            if (collectionName.charAt(i) == '.') {
                dots++;
                if (dots >= 2) {
                    return collectionName;
                }
            }
        }
        if (dots == 1) {
            return "spark_catalog." + collectionName;
        }
        String namespace = SparkSessionManager.getInstance()
            .getOrCreateSession()
            .catalog()
            .currentDatabase();
        return "spark_catalog." + namespace + "." + collectionName;
    }
}
