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
 */

package org.rumbledb.runtime.functions.arrays;

import org.rumbledb.runtime.plan.RuntimePlan;


import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AbstractAtMostOneItemRuntimePlan;

public class ArrayReverseFunctionIterator extends AbstractAtMostOneItemRuntimePlan {

    @Serial
    private static final long serialVersionUID = 1L;

    private final RuntimePlan<Item> arrayIterator;

    public ArrayReverseFunctionIterator(
            List<org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item>> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        if (arguments.size() != 1) {
            throw new OurBadException("array:reverse must have exactly one argument.");
        }
        this.arrayIterator = arguments.get(0);
    }


    @Override
    public Item evaluateAtMostOne(DynamicContext context) {
        return reverseArgument(this.arrayIterator.materialize(context));
    }

    private Item reverse(Item arrayItem) {
        if (arrayItem == null) {
            return null;
        }
        if (!arrayItem.isArray()) {
            throw new UnexpectedTypeException(
                    "Type error; argument to array:reverse must be an array.",
                    getMetadata()
            );
        }

        if (arrayItem.isArrayOfItems()) {
            List<Item> originalMembers = arrayItem.getItemMembers();
            List<Item> reversedMembers = new ArrayList<>(originalMembers);
            Collections.reverse(reversedMembers);
            return ItemFactory.getInstance()
                .createArrayItem(reversedMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        } else {
            List<List<Item>> originalMembers = arrayItem.getSequenceMembers();
            List<List<Item>> reversedMembers = new ArrayList<>(originalMembers);
            Collections.reverse(reversedMembers);
            return ItemFactory.getInstance()
                .createSequenceArrayItem(reversedMembers, this.getRuntimeStaticContext().isQuerySideEffecting());
        }
    }

    private Item reverseArgument(List<Item> items) {
        if (items.size() > 1) {
            throw new UnexpectedTypeException(
                    "array:reverse expects exactly one array argument.",
                    getMetadata()
            );
        }
        return reverse(items.isEmpty() ? null : items.get(0));
    }
}
