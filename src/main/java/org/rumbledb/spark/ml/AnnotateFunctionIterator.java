/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributor acknowledgements are maintained in the CONTRIBUTORS file at the project root.
 */
package org.rumbledb.spark.ml;

import java.io.Serial;
import java.util.List;

import org.apache.spark.api.java.JavaRDD;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidInstanceException;
import org.rumbledb.items.structured.HomogeneousItemDataFrame;
import org.rumbledb.runtime.dataframe.ItemRuntimeDataFrameFactory;
import org.rumbledb.runtime.plan.DataFrameRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.JSONiqValidateIterator;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;

public class AnnotateFunctionIterator extends ItemRuntimePlan implements DataFrameRuntimePlan<Item> {

    @Serial
    private static final long serialVersionUID = 1L;

    public AnnotateFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public HomogeneousItemDataFrame createNativeDataFrame(DynamicContext context) {
        ItemRuntimePlan inputDataIterator = this.getChild(0);
        ItemRuntimePlan schemaIterator = this.getChild(1);
        Item schemaItem = schemaIterator.materializeFirstOrNull(context);
        ItemType schemaType = ItemTypeFactory.createItemTypeFromJSoundCompactItem(null, schemaItem, null);
        schemaType.resolve(context, getMetadata());
        try {

            if (inputDataIterator.getRuntimeStaticContext().getExecutionMode().isDataFrame()) {
                HomogeneousItemDataFrame inputDataAsDataFrame =
                        ItemRuntimeDataFrameFactory.INSTANCE.fromPlan(inputDataIterator, context);
                ItemType actualSchemaType = ItemTypeFactory.createItemType(
                        inputDataAsDataFrame.getDataFrame().schema());
                if (actualSchemaType.isSubtypeOf(schemaType)) {
                    return inputDataAsDataFrame;
                }
                JavaRDD<Item> inputDataAsRDDOfItems = inputDataAsDataFrame.toRDD(getMetadata());
                return JSONiqValidateIterator.convertRDDToValidDataFrame(
                        inputDataAsRDDOfItems, schemaType, context, true, this.staticContext);
            }

            if (inputDataIterator.getRuntimeStaticContext().getExecutionMode().isRDDOrDataFrame()) {
                JavaRDD<Item> rdd = inputDataIterator.getRDD(context);
                return JSONiqValidateIterator.convertRDDToValidDataFrame(
                        rdd, schemaType, context, true, this.staticContext);
            }

            List<Item> items = inputDataIterator.materialize(context);
            return JSONiqValidateIterator.convertLocalItemsToDataFrame(
                    items, schemaType, context, true, this.staticContext);
        } catch (InvalidInstanceException ex) {
            InvalidInstanceException e = new InvalidInstanceException(
                    "Schema error in annotate(); " + ex.getJSONiqErrorMessage(), getMetadata());
            e.initCause(ex);
            throw e;
        }
    }
}
