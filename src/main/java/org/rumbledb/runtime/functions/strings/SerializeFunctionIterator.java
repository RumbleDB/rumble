/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.runtime.functions.strings;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
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
        SerializationParameters params =
                SerializationParameterUtils.defaultsForSerializeFunction(this.staticContext.getQueryLanguage());
        if (options != null) {
            SerializationParameterUtils.applyParameterItems(params, options, getMetadata());
        }

        List<Item> items = this.getChild(0).materialize(context);
        SerializationParameters itemParams = SerializationParameters.copy(params);
        if ("xml".equalsIgnoreCase(params.getMethod())) {
            itemParams.setOmitXmlDeclaration(true);
        }
        Serializer serializer = Serializers.from(itemParams);
        String itemSeparator = params.getItemSeparator();
        if (itemSeparator == null) {
            itemSeparator = "adaptive".equalsIgnoreCase(params.getMethod()) ? "\n" : "";
        }

        StringBuilder result = new StringBuilder();
        if ("json".equalsIgnoreCase(params.getMethod())) {
            if (items.isEmpty()) {
                result.append("null");
            } else if (items.size() == 1) {
                result.append(serializer.serialize(items.get(0)));
            } else {
                throw new InvalidArgumentTypeException(
                        "JSON serialization requires the top-level sequence to contain at most one item.",
                        getMetadata());
            }
        } else {
            if ("xml".equalsIgnoreCase(params.getMethod()) && !params.getOmitXmlDeclaration() && !items.isEmpty()) {
                SerializerUtils.appendXmlDeclaration(result, params);
            }
            for (int i = 0; i < items.size(); i++) {
                if (i > 0) {
                    result.append(itemSeparator);
                }
                result.append(serializer.serialize(items.get(i)));
            }
        }
        return ItemFactory.getInstance().createStringItem(result.toString());
    }
}
