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

import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

public class RangeOperationIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;
    private long left;
    private long right;
    private long index;

    public RangeOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightiterator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(leftIterator, rightiterator), executionMode, iteratorMetadata);
        this.leftIterator = leftIterator;
        this.rightIterator = rightiterator;
    }

    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            if (this.index == this.right) {
                this.hasNext = false;
            }
            return ItemFactory.getInstance().createLongItem(this.index++);
        }
        throw new IteratorFlowException("Invalid next call in Range Operation", getMetadata());
    }

    public void open(DynamicContext context) {
        super.open(context);

        this.index = 0;
        this.leftIterator.open(this.currentDynamicContextForLocalExecution);
        this.rightIterator.open(this.currentDynamicContextForLocalExecution);
        if (this.leftIterator.hasNext() && this.rightIterator.hasNext()) {
            Item left = this.leftIterator.next();
            Item right = this.rightIterator.next();

            if (
                this.leftIterator.hasNext()
                    || this.rightIterator.hasNext()
                    || !(left.isInteger())
                    || !(right.isInteger())
            ) {
                throw new UnexpectedTypeException(
                        "Range expression must have integer input, but instead received "
                            +
                            left.getDynamicType()
                            + " and "
                            + right.getDynamicType(),
                        getMetadata()
                );
            }
            try {
                this.left = left.castToIntegerValue().longValue();
                this.right = right.castToIntegerValue().longValue();
            } catch (IteratorFlowException e) {
                throw new IteratorFlowException(e.getJSONiqErrorMessage(), getMetadata());
            }
            if (this.right < this.left) {
                this.hasNext = false;
            } else {
                this.index = this.left;
                this.hasNext = true;
            }
        } else {
            this.hasNext = false;
        }

        this.leftIterator.close();
        this.rightIterator.close();

    }
}
