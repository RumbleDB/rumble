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

package org.rumbledb.runtime.functions.numerics;

import java.io.Serial;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;
import org.rumbledb.runtime.plan.ItemRuntimePlan;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;

public class NumberFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    public NumberFunctionIterator(List<ItemRuntimePlan> parameters, RuntimeStaticContext staticContext) {
        super(parameters, staticContext);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        if (this.getChildren().size() == 0) {
            List<Item> items = context.getVariableValues().getLocalVariableValue(Name.CONTEXT_ITEM, getMetadata());
            return CastIterator.castItemToType(
                    items.get(0), BuiltinTypesCatalogue.doubleItem, getMetadata(), this.staticContext);
        }

        return castOrNaN(this.getChild(0).materializeFirstOrNull(context));
    }

    private Item castOrNaN(Item anyItem) {
        if (anyItem == null) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
        try {
            Item result = CastIterator.castItemToType(
                    anyItem, BuiltinTypesCatalogue.doubleItem, getMetadata(), this.staticContext);
            if (result != null) {
                return result;
            }
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        } catch (Exception e) {
            return ItemFactory.getInstance().createDoubleItem(Double.NaN);
        }
    }
}
