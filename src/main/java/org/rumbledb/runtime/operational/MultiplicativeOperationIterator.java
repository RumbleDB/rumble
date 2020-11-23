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

import java.math.BigDecimal;
import java.math.BigInteger;
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
                    return multiply(this.left, this.right);
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

    private static Item multiply(Item left, Item right) {
        if (left.isDouble() && right.isNumeric()) {
            double l = left.getDoubleValue();
            double r = 0;
            if (right.isDouble()) {
                r = right.getDoubleValue();
            } else {
                r = right.castToDoubleValue();
            }
            return multiplyDouble(l, r);
        }
        if (right.isDouble() && left.isNumeric()) {
            return multiply(right, left);
        }
        if (
            left.isInt()
                && right.isInt()
                && (left.getIntValue() < Short.MAX_VALUE
                    && left.getIntValue() > -Short.MAX_VALUE
                    && right.getIntValue() < Short.MAX_VALUE
                    && right.getIntValue() > -Short.MAX_VALUE)
        ) {
            return multiplyInt(left.getIntValue(), right.getIntValue());
        }
        if (left.isInteger() && right.isNumeric()) {
            BigInteger l = left.getIntegerValue();
            BigInteger r = BigInteger.ZERO;
            if (right.isInteger()) {
                r = right.getIntegerValue();
            } else {
                r = right.castToIntegerValue();
            }
            return multiplyInteger(l, r);
        }
        if (right.isInteger() && left.isNumeric()) {
            return multiply(right, left);
        }
        if (left.isDecimal() && right.isNumeric()) {
            BigDecimal l = left.getDecimalValue();
            BigDecimal r = BigDecimal.ZERO;
            if (right.isDecimal()) {
                r = right.getDecimalValue();
            } else {
                r = right.castToDecimalValue();
            }
            return multiplyDecimal(l, r);
        }
        if (right.isDecimal() && left.isNumeric()) {
            return multiply(right, left);
        }
        return left.multiply(right);
    }

    private static Item multiplyDouble(double l, double r) {
        return ItemFactory.getInstance().createDoubleItem(l * r);
    }

    private static Item multiplyDecimal(BigDecimal l, BigDecimal r) {
        return ItemFactory.getInstance().createDecimalItem(l.multiply(r));
    }

    private static Item multiplyInteger(BigInteger l, BigInteger r) {
        return ItemFactory.getInstance().createIntegerItem(l.multiply(r));
    }

    private static Item multiplyInt(int l, int r) {
        return ItemFactory.getInstance().createIntItem(l * r);
    }

}
