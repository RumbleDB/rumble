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

import org.rumbledb.api.Item;
import org.rumbledb.config.SerializationParameterBuilder;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.Serializers;

import java.util.List;

public class SerializeFunctionIterator extends LocalFunctionCallIterator {

    private static final long serialVersionUID = 1L;

    public SerializeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            List<Item> items = this.children.get(0).materialize(this.currentDynamicContextForLocalExecution);
            SerializationParameters params = resolveSerializationParameters();
            Serializer serializer = Serializers.from(params);
            String itemSeparator = params.getItemSeparator();
            if (itemSeparator == null) {
                itemSeparator = "adaptive".equalsIgnoreCase(params.getMethod()) ? "\n" : "";
            }

            StringBuilder stringBuilder = new StringBuilder();
            if ("json".equalsIgnoreCase(params.getMethod())) {
                if (items.isEmpty()) {
                    stringBuilder.append("null");
                } else if (items.size() == 1) {
                    stringBuilder.append(serializer.serialize(items.get(0)));
                } else {
                    throw new InvalidArgumentTypeException(
                            "JSON serialization requires the top-level sequence to contain at most one item.",
                            getMetadata()
                    );
                }
            } else {
                for (int i = 0; i < items.size(); i++) {
                    if (i > 0) {
                        stringBuilder.append(itemSeparator);
                    }
                    stringBuilder.append(serializer.serialize(items.get(i)));
                }
            }
            this.hasNext = false;
            return ItemFactory.getInstance().createStringItem(stringBuilder.toString());
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " serialize function",
                    getMetadata()
            );
        }
    }

    private SerializationParameters resolveSerializationParameters() {
        SerializationParameters params = SerializationParameters.copy(
            this.staticContext.getSerializationParameters()
        );
        if (this.children.size() < 2) {
            return params;
        }

        List<Item> optionsItems = this.children.get(1).materialize(this.currentDynamicContextForLocalExecution);
        if (optionsItems.isEmpty()) {
            return params;
        }
        Item options = optionsItems.get(0);
        if (!options.isMap() && !options.isObject()) {
            throw new InvalidArgumentTypeException("The second argument of fn:serialize must be a map.", getMetadata());
        }
        for (String key : options.getStringKeys()) {
            Item value = options.getItemByKey(key);
            SerializationParameterBuilder.update(params, key, value == null ? "" : value.getStringValue());
        }
        return params;
    }
}
