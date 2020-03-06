package sparksoniq.spark.ml;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ObjectItem;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import sparksoniq.jsoniq.ExecutionMode;
import sparksoniq.semantics.DynamicContext;
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
        ObjectItem schemaItem;

        // materialize singleton pattern
        schemaIterator.open(context);
        if (!schemaIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Schema provided to annotate function can not be an empty sequence.",
                    getMetadata()
            );
        }
        schemaItem = (ObjectItem) schemaIterator.next();
        if (schemaIterator.hasNext()) {
            throw new UnexpectedTypeException(
                    "Schema provided to annotate function must be a singleton object.",
                    getMetadata()
            );
        }
        schemaIterator.close();

        if (inputDataIterator.isDataFrame()) {
            Dataset<Row> inputDataAsDataFrame = inputDataIterator.getDataFrame(context);
            DataFrameUtils.validateSchemaAgainstDataFrame(
                schemaItem,
                inputDataAsDataFrame.schema(),
                getMetadata()
            );
            return inputDataAsDataFrame;
        }

        if (inputDataIterator.isRDD()) {
            JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
            return DataFrameUtils.convertItemRDDToDataFrame(rdd, schemaItem, getMetadata());
        }

        List<Item> items = inputDataIterator.materialize(context);;
        return DataFrameUtils.convertLocalItemsToDataFrame(items, schemaItem, getMetadata());
    }

}
