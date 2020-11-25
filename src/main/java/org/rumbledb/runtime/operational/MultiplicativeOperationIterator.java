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
import java.math.RoundingMode;
import java.util.Arrays;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.DivisionByZeroException;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.OurBadException;
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
                    return processItem(this.left, this.right, this.multiplicativeOperator);
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

    private static Item processItem(
            Item left,
            Item right,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator
    ) {
        if (
            left.isInt()
                && right.isInt()
                && (left.getIntValue() < Short.MAX_VALUE
                    && left.getIntValue() > -Short.MAX_VALUE
                    && right.getIntValue() < Short.MAX_VALUE
                    && right.getIntValue() > -Short.MAX_VALUE)
        ) {
            return processInt(left.getIntValue(), right.getIntValue(), multiplicativeOperator);
        }

        // General cases
        if (left.isDouble() && right.isNumeric()) {
            double l = left.getDoubleValue();
            double r = 0;
            if (right.isDouble()) {
                r = right.getDoubleValue();
            } else {
                r = right.castToDoubleValue();
            }
            return processDouble(l, r, multiplicativeOperator);
        }
        if (right.isDouble() && left.isNumeric()) {
            return processItem(right, left, multiplicativeOperator);
        }
        if (left.isInteger() && right.isInteger()) {
            BigInteger l = left.getIntegerValue();
            BigInteger r = BigInteger.ZERO;
            if (right.isInteger()) {
                r = right.getIntegerValue();
            } else {
                r = right.castToIntegerValue();
            }
            return processInteger(l, r, multiplicativeOperator);
        }
        if (left.isDecimal() && right.isDecimal()) {
            BigDecimal l = left.getDecimalValue();
            BigDecimal r = BigDecimal.ZERO;
            if (right.isDecimal()) {
                r = right.getDecimalValue();
            } else {
                r = right.castToDecimalValue();
            }
            return processDecimal(l, r, multiplicativeOperator);
        }
        switch (multiplicativeOperator) {
            case MUL:
                return left.multiply(right);
            case DIV:
                return left.divide(right);
            case IDIV:
                return left.idivide(right);
            case MOD:
                return left.modulo(right);
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        ExceptionMetadata.EMPTY_METADATA
                );
        }
    }

    private static Item processDouble(
            double l,
            double r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createDoubleItem(l * r);
            case DIV:
                if (r == 0) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance().createDoubleItem(l / r);
            case IDIV:
                if (r == 0) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance()
                    .createDoubleItem((double) (long) (l / r));
            case MOD:
                if (r == 0) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance().createDoubleItem(l % r);
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        ExceptionMetadata.EMPTY_METADATA
                );
        }
    }

    private static Item processDecimal(
            BigDecimal l,
            BigDecimal r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createDecimalItem(l.multiply(r));
            case DIV:
                if (r.equals(BigDecimal.ZERO)) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance()
                    .createDecimalItem(l.divide(r, 10, BigDecimal.ROUND_HALF_UP));
            case IDIV:
                if (r.equals(BigDecimal.ZERO)) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance()
                    .createIntegerItem(
                        l.divide(r, 0, RoundingMode.DOWN).toBigInteger()
                    );
            case MOD:
                if (r.equals(BigDecimal.ZERO)) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance().createDecimalItem(l.remainder(r));
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        ExceptionMetadata.EMPTY_METADATA
                );
        }
    }

    private static Item processInteger(
            BigInteger l,
            BigInteger r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createIntegerItem(l.multiply(r));
            case DIV:
                if (r.equals(BigInteger.ZERO)) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                BigDecimal bdResult = new BigDecimal(l)
                    .divide(new BigDecimal(r), 10, BigDecimal.ROUND_HALF_UP);
                if (bdResult.stripTrailingZeros().scale() <= 0) {
                    return ItemFactory.getInstance().createIntegerItem(bdResult.toBigIntegerExact());
                } else {
                    return ItemFactory.getInstance().createDecimalItem(bdResult);
                }
            case IDIV:
                if (r.equals(BigInteger.ZERO)) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance()
                    .createIntegerItem(
                        l.divide(r)
                    );
            case MOD:
                if (r.equals(BigInteger.ZERO)) {
                    throw new DivisionByZeroException(ExceptionMetadata.EMPTY_METADATA);
                }
                return ItemFactory.getInstance()
                    .createIntegerItem(l.mod(r));
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        ExceptionMetadata.EMPTY_METADATA
                );
        }
    }

    private static Item processInt(
            int l,
            int r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator
    ) {
        return ItemFactory.getInstance().createIntItem(l * r);
    }

}
