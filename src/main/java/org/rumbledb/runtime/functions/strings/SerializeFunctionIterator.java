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
package org.rumbledb.runtime.functions.strings;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.errorcodes.ErrorCode;
import org.rumbledb.exceptions.RumbleException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.serialization.SerializationParameterUtils;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.SerializerUtils;
import org.rumbledb.serialization.Serializers;

public class SerializeFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public SerializeFunctionIterator(List<ItemRuntimePlan> arguments, RuntimeStaticContext staticContext) {
        super(arguments, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        List<Item> options =
                this.getChildren().size() < 2 ? null : this.getChild(1).materialize(context);
        SerializationParameters params = new SerializationParameters();
        if (options != null) {
            SerializationParameterUtils.applyParameterItems(params, options, getMetadata());
        }

        List<Item> items = this.getChild(0).materialize(context);
        String method = params.getMethod();
        // Serialization 3.1 normalizes arrays before joining items, with or without a separator.
        if ("xml".equalsIgnoreCase(method)
                || "xhtml".equalsIgnoreCase(method)
                || "html".equalsIgnoreCase(method)
                || "text".equalsIgnoreCase(method)) {
            List<Item> flattenedItems = new ArrayList<>();
            flattenArrays(items, flattenedItems);
            items = flattenedItems;
        }
        SerializationParameters itemParams = SerializationParameters.copy(params);
        if ("xml".equalsIgnoreCase(method)) {
            itemParams.setOmitXmlDeclaration(true);
        }
        Serializer serializer = Serializers.from(itemParams);
        String itemSeparator = params.getItemSeparator();

        StringBuilder result = new StringBuilder();
        if ("json".equalsIgnoreCase(method)) {
            if (items.isEmpty()) {
                result.append("null");
            } else if (items.size() == 1) {
                result.append(serializer.serialize(items.get(0)));
            } else {
                throw new RumbleException(
                        "JSON serialization requires the top-level sequence to contain at most one item.",
                        ErrorCode.JsonSerializationSequence,
                        getMetadata());
            }
        } else {
            if ("xml".equalsIgnoreCase(method) && !params.getOmitXmlDeclaration() && !items.isEmpty()) {
                SerializerUtils.appendXmlDeclaration(result, params);
            }
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) {
                    if (itemSeparator != null) {
                        result.append(itemSeparator);
                    } else if ("adaptive".equalsIgnoreCase(method)) {
                        result.append('\n');
                    } else if (items.get(i - 1).isAtomic() && items.get(i).isAtomic()) {
                        // An absent separator inserts a space only between adjacent atomic values.
                        result.append(' ');
                    }
                }
                result.append(serializer.serialize(items.get(i)));
            }
        }
        return ItemFactory.getInstance().createStringItem(result.toString());
    }

    private static void flattenArrays(List<Item> items, List<Item> flattenedItems) {
        for (Item item : items) {
            if (item.isArray()) {
                for (List<Item> member : item.getSequenceMembers()) {
                    flattenArrays(member, flattenedItems);
                }
            } else {
                flattenedItems.add(item);
            }
        }
    }
}
