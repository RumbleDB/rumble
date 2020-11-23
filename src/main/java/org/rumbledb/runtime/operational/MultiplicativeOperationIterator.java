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
import org.rumbledb.exceptions.DivisionByZeroException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.LocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.operational.base.ComparisonUtil;

public class MultiplicativeOperationIterator extends LocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    Item left;
    Item right;
    MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;

    public MultiplicativeOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(leftIterator, rightIterator), executionMode, iteratorMetadata);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.multiplicativeOperator = multiplicativeOperator;
    }

    @Override
    public void open(DynamicContext context) {
        super.open(context);

        try {
            this.left = this.leftIterator.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Multiplication expression requires at most one item in its left input sequence.",
                    getMetadata()
            );
        }
        try {
            this.right = this.rightIterator.materializeAtMostOneItemOrNull(this.currentDynamicContextForLocalExecution);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Multiplication expression requires at most one item in its right input sequence.",
                    getMetadata()
            );
        }

        // if left or right equals empty sequence, return empty sequence
        if (this.left == null || this.right == null) {
            this.hasNext = false;
        } else {
            ComparisonUtil.checkBinaryOperation(
                this.left,
                this.right,
                this.multiplicativeOperator.toString(),
                getMetadata()
            );
            this.hasNext = true;
        }
    }

    @Override
    public Item next() {
        if (!this.hasNext) {
            throw new IteratorFlowException(RuntimeIterator.FLOW_EXCEPTION_MESSAGE, getMetadata());
        }
        this.hasNext = false;
        try {
            switch (this.multiplicativeOperator) {
                case MUL:
                    if (this.left.isDouble() && this.right.isDouble()) {
                        return ItemFactory.getInstance()
                            .createDoubleItem(this.left.getDoubleValue() * this.right.getDoubleValue());
                    }
                    if (this.left.isDouble()) {
                        return ItemFactory.getInstance()
                            .createDoubleItem(this.left.getDoubleValue() * this.right.castToDoubleValue());
                    }
                    if (this.right.isDouble()) {
                        return ItemFactory.getInstance()
                            .createDoubleItem(this.left.castToDoubleValue() * this.right.getDoubleValue());
                    }
                    if (
                        this.left.isInt()
                            && this.right.isInt()
                            && (this.left.getIntValue() < Short.MAX_VALUE
                                && this.left.getIntValue() > -Short.MAX_VALUE
                                && this.right.getIntValue() < Short.MAX_VALUE
                                && this.right.getIntValue() > -Short.MAX_VALUE)
                    ) {
                        return ItemFactory.getInstance()
                            .createIntItem(this.left.getIntValue() * this.right.getIntValue());
                    }
                    if (this.left.isInteger() && this.right.isInteger()) {
                        return ItemFactory.getInstance()
                            .createIntegerItem(this.left.getIntegerValue().multiply(this.right.getIntegerValue()));
                    }
                    if (this.left.isInteger() && this.right.isNumeric()) {
                        return ItemFactory.getInstance()
                            .createIntegerItem(this.left.getIntegerValue().multiply(this.right.castToIntegerValue()));
                    }
                    if (this.right.isInteger() && this.left.isNumeric()) {
                        return ItemFactory.getInstance()
                            .createIntegerItem(this.left.castToIntegerValue().multiply(this.right.getIntegerValue()));
                    }
                    if (this.left.isDecimal() && this.right.isDecimal()) {
                        return ItemFactory.getInstance()
                            .createDecimalItem(this.left.getDecimalValue().multiply(this.right.getDecimalValue()));
                    }
                    if (this.left.isDecimal() && this.right.isNumeric()) {
                        return ItemFactory.getInstance()
                            .createDecimalItem(this.left.getDecimalValue().multiply(this.right.castToDecimalValue()));
                    }
                    if (this.right.isDecimal() && this.left.isNumeric()) {
                        return ItemFactory.getInstance()
                            .createDecimalItem(this.left.castToDecimalValue().multiply(this.right.getDecimalValue()));
                    }
                    return this.left.multiply(this.right);
                case DIV:
                    return this.left.divide(this.right);
                case IDIV:
                    return this.left.idivide(this.right);
                case MOD:
                    return this.left.modulo(this.right);
                default:
                    throw new IteratorFlowException("Non recognized multiplicative operator.", getMetadata());
            }
        } catch (DivisionByZeroException e) {
            throw new DivisionByZeroException(getMetadata());
        } catch (RuntimeException e) {
            UnexpectedTypeException ute = new UnexpectedTypeException(
                    " \""
                        + this.multiplicativeOperator.toString()
                        + "\": operation not possible with parameters of type \""
                        + this.left.getDynamicType().toString()
                        + "\" and \""
                        + this.right.getDynamicType().toString()
                        + "\"",
                    getMetadata()
            );
            ute.initCause(e);
            throw ute;
        }
    }

}
