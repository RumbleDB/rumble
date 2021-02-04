package sparksoniq.spark.ml;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MLInvalidDataFrameSchemaException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.DataFrameUtils;

import java.util.List;

public class AnnotateFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public AnnotateFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Dataset<Row> getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.children.get(0);
        RuntimeIterator schemaIterator = this.children.get(1);
        Item schemaItem = schemaIterator.materializeFirstItemOrNull(context);

        try {

            if (inputDataIterator.isDataFrame()) {
                Dataset<Row> inputDataAsDataFrame = inputDataIterator.getDataFrame(context);
                DataFrameUtils.validateSchemaItemAgainstDataFrame(
                    schemaItem,
                    inputDataAsDataFrame.schema()
                );
                return inputDataAsDataFrame;
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return DataFrameUtils.convertItemRDDToDataFrame(rdd, schemaItem);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return DataFrameUtils.convertLocalItemsToDataFrame(items, schemaItem);
        } catch (MLInvalidDataFrameSchemaException ex) {
            MLInvalidDataFrameSchemaException e = new MLInvalidDataFrameSchemaException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                    getMetadata()
            );
            e.initCause(ex);
            throw e;
        }
    }

}
