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

package org.rumbledb.runtime.misc;

import java.io.Serial;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;

public class StringConcatIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> leftIterator;
    private final org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> rightIterator;

    public StringConcatIterator(
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> leftIterator,
            org.rumbledb.runtime.plan.RuntimePlan<org.rumbledb.api.Item> rightIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
    }

    @Override
    public Item evaluateAtMostOne(DynamicContext dynamicContext) {
        Item left = null;
        try {
            left = this.leftIterator.materializeAtMostOneOrDefault(
                dynamicContext,
                ItemFactory.getInstance().createStringItem("")
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "String concatenation expression requires at most one item in its left input sequence.",
                    getMetadata()
            );
        }
        Item right = null;
        try {
            right = this.rightIterator.materializeAtMostOneOrDefault(
                dynamicContext,
                ItemFactory.getInstance().createStringItem("")
            );
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "String concatenation expression requires at most one item in its right input sequence.",
                    getMetadata()
            );
        }
        return concatenate(left, right, getMetadata());
    }

    private static Item concatenate(Item left, Item right, ExceptionMetadata metadata) {
        if (!left.isAtomic() || !right.isAtomic()) {
            throw new UnexpectedTypeException(
                    "String concat expression has arguments that can't be converted to a string "
                        +
                        left.serialize()
                        + ", "
                        + right.serialize(),
                    metadata
            );
        }

        String leftStringValue = left.getStringValue();
        String rightStringValue = right.getStringValue();
        return ItemFactory.getInstance().createStringItem(leftStringValue.concat(rightStringValue));
    }
}
