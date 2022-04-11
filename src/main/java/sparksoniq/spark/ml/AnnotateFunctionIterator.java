package sparksoniq.spark.ml;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

import java.util.List;

public class AnnotateFunctionIterator extends DataFrameRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public AnnotateFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.configuration = configuration;
    }

    @Override
    public JSoundDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.children.get(0);
        RuntimeIterator schemaIterator = this.children.get(1);
        Item schemaItem = schemaIterator.materializeFirstItemOrNull(context);
        ItemType schemaType = ItemTypeFactory.createItemTypeFromJSoundCompactItem(null, schemaItem, null);
        schemaType.resolve(context, getMetadata());
        try {

            if (inputDataIterator.isDataFrame()) {
                JSoundDataFrame inputDataAsDataFrame = inputDataIterator.getDataFrame(context);
                ItemType actualSchemaType = ItemTypeFactory.createItemType(
                    inputDataAsDataFrame.getDataFrame().schema()
                );
                if (actualSchemaType.isSubtypeOf(schemaType)) {
                    return inputDataAsDataFrame;
                }
                JavaRDD<Item> inputDataAsRDDOfItems = dataFrameToRDDOfItems(inputDataAsDataFrame, getMetadata());
                return ValidateTypeIterator.convertRDDToValidDataFrame(
                    inputDataAsRDDOfItems,
                    schemaType,
                    RumbleRuntimeConfiguration.getDefaultConfiguration()
                );
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return ValidateTypeIterator.convertRDDToValidDataFrame(
                    rdd,
                    schemaType,
                    RumbleRuntimeConfiguration.getDefaultConfiguration()
                );
            }

            List<Item> items = inputDataIterator.materialize(context);
            return ValidateTypeIterator.convertLocalItemsToDataFrame(
                items,
                schemaType,
                RumbleRuntimeConfiguration.getDefaultConfiguration()
            );
        } catch (InvalidInstanceException ex) {
            InvalidInstanceException e = new InvalidInstanceException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                    getMetadata()
            );
            e.initCause(ex);
            throw e;
        }
    }

}
