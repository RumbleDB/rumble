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

package org.rumbledb.runtime.operational;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;

public class UnaryOperationIterator extends LocalRuntimeIterator {

    private boolean negated;
    private final RuntimeIterator child;
    private Item item;
    private static final long serialVersionUID = 1L;

    public UnaryOperationIterator(
            RuntimeIterator child,
            boolean negated,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Collections.singletonList(child), executionMode, iteratorMetadata);
        this.child = child;
        this.negated = negated;
        this.item = null;
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasNext = false;

        if (this.negated) {
            return this.item;
        }
        if (this.item.isInt()) {
            return ItemFactory.getInstance().createIntItem(-1 * this.item.getIntValue());
        }
        if (this.item.isInteger()) {
            return ItemFactory.getInstance()
                .createIntegerItem(BigInteger.valueOf(-1).multiply(this.item.getIntegerValue()));
        }
        if (this.item.isDouble()) {
            return ItemFactory.getInstance().createDoubleItem(-1 * this.item.getDoubleValue());
        }
        if (this.item.isDecimal()) {
            return ItemFactory.getInstance()
                .createDecimalItem(this.item.getDecimalValue().multiply(new BigDecimal(-1)));
        }
        throw new UnexpectedTypeException(
                "Unary expression has non numeric args "
                    +
                    this.item.serialize(),
                getMetadata()
        );
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        try {
            this.item = this.child.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Unary expression requires at most one item in its input sequence.",
                    getMetadata()
            );
        }
        this.hasNext = this.item != null;
    }

}
