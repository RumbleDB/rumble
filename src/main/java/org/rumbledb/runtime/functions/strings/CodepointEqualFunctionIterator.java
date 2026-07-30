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
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

import java.io.Serial;
import java.util.List;

public class CodepointEqualFunctionIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;

    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> leftIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> rightIterator;

    public CodepointEqualFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.leftIterator = arguments.get(0);
        this.rightIterator = arguments.get(1);
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        Item left = this.leftIterator.materializeFirstOrNull(context);
        if (left == null) {
            return null;
        }
        Item right = this.rightIterator.materializeFirstOrNull(context);
        if (right == null) {
            return null;
        }
        return evaluate(left, right);
    }

    private static Item evaluate(Item left, Item right) {
        return ItemFactory.getInstance()
            .createBooleanItem(left.getStringValue().equals(right.getStringValue()));
    }
}
