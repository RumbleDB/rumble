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

import org.apache.commons.lang.StringUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.util.List;

public class NormalizeSpaceFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    public NormalizeSpaceFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        if (this.children.size() == 0) {
            List<Item> items = context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            return ItemFactory.getInstance()
                .createStringItem(StringUtils.normalizeSpace(items.get(0).getStringValue()));
        }
        Item stringItem = this.children.get(0)
            .materializeFirstItemOrNull(context);

        if (stringItem == null) {
            return ItemFactory.getInstance().createStringItem("");
        }

        return ItemFactory.getInstance().createStringItem(StringUtils.normalizeSpace(stringItem.getStringValue()));
    }

}
