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
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class SubstringFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public SubstringFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        String result;
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);
        if (stringItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }
        Item indexItem = this.children.get(1)
            .materializeFirstItemOrNull(context);
        if (indexItem == null) {
            throw new UnexpectedTypeException(
                    "Type error; Start index parameter can't be empty sequence ",
                    getMetadata()
            );
        }
        int index = (int) Math.round(indexItem.getDoubleValue() - 1);
        if (index >= stringItem.getStringValue().length()) {
            return ItemFactory.getInstance().createStringItem("");
        }
        if (this.children.size() > 2) {
            Item endIndexItem = this.children.get(2)
                .materializeFirstItemOrNull(context);
            if (endIndexItem == null) {
                throw new UnexpectedTypeException(
                        "Type error; End index parameter can't be empty sequence ",
                        getMetadata()
                );
            }
            double endIndex = sanitizeEndIndex(stringItem, endIndexItem, index);
            if (endIndex < index) {
                return ItemFactory.getInstance().createStringItem("");
            }
            result = stringItem.getStringValue().substring(Math.max(index, 0), (int) Math.round(endIndex));
        } else {
            result = stringItem.getStringValue().substring(index);
        }

        return ItemFactory.getInstance().createStringItem(result);
    }

    private double sanitizeEndIndex(Item stringItem, Item endIndexItem, int startIndex) {
        // char indexing starts from 1 in JSONiq
        return Math.min(stringItem.getStringValue().length(), startIndex + endIndexItem.getDoubleValue());
    }
}
