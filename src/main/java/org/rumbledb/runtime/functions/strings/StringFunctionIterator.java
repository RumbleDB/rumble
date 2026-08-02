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

import org.rumbledb.runtime.plan.ItemRuntimePlan;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.FunctionItemStringValueException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

import java.io.Serial;
import java.util.List;

public class StringFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public StringFunctionIterator(
            List<ItemRuntimePlan> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    private Item stringResultFromItem(Item item) {
        try {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        } catch (FunctionItemStringValueException e) {
            throw new FunctionItemStringValueException(e.getMessage(), getMetadata());
        }
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        if (this.getChildren().size() == 0) {
            List<Item> items = context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            Item contextItem = items.get(0);
            return stringResultFromItem(contextItem);
        }

        Item item = this.getChild(0).materializeFirstOrNull(context);
        return item == null ? null : stringResultFromItem(item);
    }

}
