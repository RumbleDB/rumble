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
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.InvalidArgumentTypeException;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.ComputedLocalCursor;
import org.rumbledb.runtime.cursor.Cursor;
import org.rumbledb.runtime.functions.base.LocalFunctionCallIterator;
import org.rumbledb.serialization.SerializationParameters;
import org.rumbledb.serialization.SerializationParameterUtils;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.serialization.Serializers;
import org.rumbledb.serialization.SerializerUtils;

import java.io.Serial;
import java.util.List;

public class SerializeFunctionIterator extends LocalFunctionCallIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    public SerializeFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public Cursor<Item> createNativeCursor(DynamicContext context) {
        return new ComputedLocalCursor<>(
                () -> serialize(
                    this.getChild(0).materialize(context),
                    resolveSerializationParameters(context)
                ),
                getMetadata()
        );
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            List<Item> items = this.getChild(0).materialize(this.currentDynamicContextForLocalExecution);
            this.hasNext = false;
            return serialize(items, resolveSerializationParameters());
        } else {
            throw new IteratorFlowException(
                    RuntimeIterator.FLOW_EXCEPTION_MESSAGE + " serialize function",
                    getMetadata()
            );
        }
    }

    private SerializationParameters resolveSerializationParameters() {
        List<Item> options = this.getChildren().size() < 2
            ? null
            : this.getChild(1).materialize(this.currentDynamicContextForLocalExecution);
        return resolveSerializationParameters(options);
    }

    private SerializationParameters resolveSerializationParameters(DynamicContext context) {
        List<Item> options = this.getChildren().size() < 2
            ? null
            : this.getChild(1).materialize(context);
        return resolveSerializationParameters(options);
    }

    private SerializationParameters resolveSerializationParameters(List<Item> options) {
        SerializationParameters params = SerializationParameterUtils.defaultsForSerializeFunction(
            this.staticContext.getQueryLanguage()
        );
        if (options != null) {
            SerializationParameterUtils.applyParameterItems(params, options, getMetadata());
        }
        return params;
    }

    private Item serialize(List<Item> items, SerializationParameters params) {
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
                        getMetadata()
                );
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
