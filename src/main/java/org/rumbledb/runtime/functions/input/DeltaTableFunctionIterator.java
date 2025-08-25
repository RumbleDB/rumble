package org.rumbledb.runtime.functions.input;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.StructField;
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

        Dataset<Row> dataFrame = SparkSessionManager.getInstance().getOrCreateSession().table(collectionName);
        return postProcess(dataFrame, collectionName);
    }

    public static JSoundDataFrame postProcess(Dataset<Row> dataFrame, String collectionName) {
        StructField[] fields = dataFrame.schema().fields();
        boolean hasLongRowId = false;
        for (org.apache.spark.sql.types.StructField field : fields) {
            if (
                field.name().equals(SparkSessionManager.rowIdColumnName) && field.dataType().typeName().equals("long")
            ) {
                hasLongRowId = true;
                break;
            }
        }
        boolean hasDoubleRowOrder = false;
        for (org.apache.spark.sql.types.StructField field : fields) {
            if (
                field.name().equals(SparkSessionManager.rowOrderColumnName)
                    && field.dataType().typeName().equals("double")
            ) {
                hasDoubleRowOrder = true;
                break;
            }
        }
        if (!hasLongRowId) {
            return new JSoundDataFrame(dataFrame);
        } else if (hasDoubleRowOrder) {
            dataFrame = dataFrame.orderBy(SparkSessionManager.rowOrderColumnName);
            dataFrame = dataFrame.withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(0));
            dataFrame = dataFrame.withColumn(SparkSessionManager.pathInColumnName, lit(""));
            dataFrame = dataFrame.withColumn(SparkSessionManager.tableLocationColumnName, lit(collectionName));
            return new JSoundDataFrame(dataFrame);
        } else {
            dataFrame = dataFrame.withColumn(SparkSessionManager.mutabilityLevelColumnName, lit(0));
            dataFrame = dataFrame.withColumn(SparkSessionManager.pathInColumnName, lit(""));
            dataFrame = dataFrame.withColumn(SparkSessionManager.tableLocationColumnName, lit(collectionName));
            return new JSoundDataFrame(dataFrame);
        }
    }
}
