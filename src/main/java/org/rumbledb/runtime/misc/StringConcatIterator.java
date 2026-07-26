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
import java.util.Objects;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.cursor.AtMostOneLocalCursor;
import org.rumbledb.runtime.cursor.LocalCursor;
import org.rumbledb.runtime.cursor.LocalCursorUtils;
import org.rumbledb.runtime.plan.RuntimePlan;

public class StringConcatIterator extends AtMostOneItemLocalRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private final RuntimeIterator leftIterator;
    private final RuntimeIterator rightIterator;

    public StringConcatIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
    }

    @Override
    public LocalCursor<Item> createLocalCursor(DynamicContext context) {
        return new Cursor(this.leftIterator, this.rightIterator, context, getMetadata());
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        Item left = null;
        try {
            left = LocalCursorUtils.materializeAtMostOneOrDefault(
                this.leftIterator,
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
            right = LocalCursorUtils.materializeAtMostOneOrDefault(
                this.rightIterator,
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

    private static final class Cursor extends AtMostOneLocalCursor<Item> {

        private final RuntimePlan<Item> leftPlan;
        private final RuntimePlan<Item> rightPlan;
        private final DynamicContext context;
        private final ExceptionMetadata metadata;

        private Cursor(
                RuntimePlan<Item> leftPlan,
                RuntimePlan<Item> rightPlan,
                DynamicContext context,
                ExceptionMetadata metadata
        ) {
            this.leftPlan = Objects.requireNonNull(leftPlan, "left plan cannot be null");
            this.rightPlan = Objects.requireNonNull(rightPlan, "right plan cannot be null");
            this.context = Objects.requireNonNull(context, "dynamic context cannot be null");
            this.metadata = Objects.requireNonNull(metadata, "metadata cannot be null");
        }

        @Override
        protected Item materializeFirstItemOrNull() {
            Item emptyString = ItemFactory.getInstance().createStringItem("");
            Item left = materializeOperand(this.leftPlan, emptyString, "left");
            Item right = materializeOperand(this.rightPlan, emptyString, "right");
            return concatenate(left, right, this.metadata);
        }

        private Item materializeOperand(RuntimePlan<Item> plan, Item defaultValue, String side) {
            try {
                return LocalCursorUtils.materializeAtMostOneOrDefault(plan, this.context, defaultValue);
            } catch (MoreThanOneItemException exception) {
                throw new UnexpectedTypeException(
                        "String concatenation expression requires at most one item in its "
                            + side
                            + " input sequence.",
                        this.metadata
                );
            }
        }
    }
}
