package org.rumbledb.spark.ml;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.DataFrameRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.typing.ValidateTypeIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

public class AnnotateFunctionIterator extends DataFrameRuntimeIterator {

    @Serial private static final long serialVersionUID = 1L;

    public AnnotateFunctionIterator(
            List<RuntimeIterator> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame getDataFrame(DynamicContext context) {
        RuntimeIterator inputDataIterator = this.getChild(0);
        RuntimeIterator schemaIterator = this.getChild(1);
        Item schemaItem = schemaIterator.materializeFirstItemOrNull(context);
        ItemType schemaType =
                ItemTypeFactory.createItemTypeFromJSoundCompactItem(null, schemaItem, null);
        schemaType.resolve(context, getMetadata());
        try {

            if (inputDataIterator.isDataFrame()) {
                HomogeneousItemDataFrame inputDataAsDataFrame =
                        inputDataIterator.getDataFrame(context);
                ItemType actualSchemaType =
                        ItemTypeFactory.createItemType(
                                inputDataAsDataFrame.getDataFrame().schema());
                if (actualSchemaType.isSubtypeOf(schemaType)) {
                    return inputDataAsDataFrame;
                }
                JavaRDD<Item> inputDataAsRDDOfItems = inputDataAsDataFrame.toRDD(getMetadata());
                return ValidateTypeIterator.convertRDDToValidDataFrame(
                        inputDataAsRDDOfItems, schemaType, context, true, this.staticContext);
            }

            if (inputDataIterator.isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return ValidateTypeIterator.convertRDDToValidDataFrame(
                        rdd, schemaType, context, true, this.staticContext);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return ValidateTypeIterator.convertLocalItemsToDataFrame(
                    items, schemaType, context, true, this.staticContext);
        } catch (InvalidInstanceException ex) {
            InvalidInstanceException e =
                    new InvalidInstanceException(
                            "Schema error in annotate(); " + ex.getJSONiqErrorMessage(),
                            getMetadata());
            e.initCause(ex);
            throw e;
        }
    }
}
