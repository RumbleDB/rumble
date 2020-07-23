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

import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.operational.base.ComparisonUtil;


public class AdditiveOperationIterator extends LocalRuntimeIterator {

    private static final long serialVersionUID = 1L;

    private Item left;
    private Item right;
    private boolean isMinus;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;

    public AdditiveOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            boolean isMinus,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(leftIterator, rightIterator), executionMode, iteratorMetadata);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.isMinus = isMinus;
    }

    @Override
    public Item next() {
        if (this.hasNext) {
            this.hasNext = false;
            try {
                if (!this.isMinus) {
                    return this.left.add(this.right);
                }
                return this.left.subtract(this.right);
            } catch (RuntimeException e) {
                throw new UnexpectedTypeException(
                        " \""
                            + (this.isMinus ? "-" : "+")
                            + "\": operation not possible with parameters of type \""
                            + this.left.getDynamicType().toString()
                            + "\" and \""
                            + this.right.getDynamicType().toString()
                            + "\"",
                        getMetadata()
                );
            }
        }
        throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        this.leftIterator.open(this.currentDynamicContextForLocalExecution);
        this.rightIterator.open(this.currentDynamicContextForLocalExecution);

        if (!this.leftIterator.hasNext() || !this.rightIterator.hasNext()) {
            this.hasNext = false;
        } else {
            this.left = this.leftIterator.next();
            this.right = this.rightIterator.next();
            ComparisonUtil.checkBinaryOperation(
                this.left,
                this.right,
                (this.isMinus ? "-" : "+"),
                getMetadata()
            );
            this.hasNext = true;
            if (this.leftIterator.hasNext() || this.rightIterator.hasNext()) {
                throw new UnexpectedTypeException(
                        "Sequence of more than one item can not be promoted to "
                            +
                            "parameter type atomic of function add()",
                        getMetadata()
                );
            }
        }
        this.leftIterator.close();
        this.rightIterator.close();
    }

}
