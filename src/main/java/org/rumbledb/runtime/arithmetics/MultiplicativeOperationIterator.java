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

package org.rumbledb.runtime.arithmetics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Arrays;

import java.time.Period;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression;
import org.rumbledb.expressions.arithmetic.MultiplicativeExpression.MultiplicativeOperator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;


public class MultiplicativeOperationIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    Item left;
    Item right;
    MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator;
    private final RuntimeIterator leftIterator;
    private final RuntimeIterator rightIterator;

    public MultiplicativeOperationIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.multiplicativeOperator = multiplicativeOperator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {

        try {
            this.left = this.leftIterator.materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Multiplication expression requires at most one item in its left input sequence.",
                    getMetadata()
            );
        }
        try {
            this.right = this.rightIterator.materializeAtMostOneItemOrNull(context);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Multiplication expression requires at most one item in its right input sequence.",
                    getMetadata()
            );
        }

        // if left or right equals empty sequence, return empty sequence
        if (this.left == null || this.right == null) {
            return null;
        }
        if (!this.left.isAtomic()) {
            String message = String.format(
                "Can not atomize an %1$s item: an %1$s has probably been passed where "
                    + "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                this.left.getDynamicType().toString()
            );
            throw new NonAtomicKeyException(message, getMetadata());
        }
        if (!this.right.isAtomic()) {
            String message = String.format(
                "Can not atomize an %1$s item: an %1$s has probably been passed where "
                    + "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                this.right.getDynamicType().toString()
            );
            throw new NonAtomicKeyException(message, getMetadata());
        }
        return processItem(this.left, this.right, this.multiplicativeOperator, getMetadata());
    }

    public static Item processItem(
            Item left,
            Item right,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        if (left.isInt() && right.isInt()) {
            switch (multiplicativeOperator) {
                case MUL:
                    if (
                        left.getIntValue() < Short.MAX_VALUE
                            && left.getIntValue() > -Short.MAX_VALUE
                            && right.getIntValue() < Short.MAX_VALUE
                            && right.getIntValue() > -Short.MAX_VALUE
                    ) {
                        return processInt(left.getIntValue(), right.getIntValue(), multiplicativeOperator, metadata);
                    } else {
                        break;
                    }
                case DIV:
                case MOD:
                case IDIV:
                    return processInt(left.getIntValue(), right.getIntValue(), multiplicativeOperator, metadata);
            }
        }

        // General cases
        if (left.isDouble() && right.isNumeric()) {
            double l = left.getDoubleValue();
            double r;
            if (right.isDouble()) {
                r = right.getDoubleValue();
            } else {
                r = right.castToDoubleValue();
            }
            return processDouble(l, r, multiplicativeOperator, metadata);
        }
        if (right.isDouble() && left.isNumeric()) {
            double l = left.castToDoubleValue();
            double r = right.getDoubleValue();
            return processDouble(l, r, multiplicativeOperator, metadata);
        }
        if (left.isFloat() && right.isNumeric()) {
            float l = left.getFloatValue();
            float r;
            if (right.isFloat()) {
                r = right.getFloatValue();
            } else {
                r = right.castToFloatValue();
            }
            return processFloat(l, r, multiplicativeOperator, metadata);
        }
        if (right.isFloat() && left.isNumeric()) {
            float l = left.castToFloatValue();
            float r = right.getFloatValue();
            return processFloat(l, r, multiplicativeOperator, metadata);
        }
        if (left.isInteger() && right.isInteger()) {
            BigInteger l = left.getIntegerValue();
            BigInteger r = right.getIntegerValue();
            return processInteger(l, r, multiplicativeOperator, metadata);
        }
        if (left.isDecimal() && right.isDecimal()) {
            BigDecimal l = left.getDecimalValue();
            BigDecimal r = right.getDecimalValue();
            return processDecimal(l, r, multiplicativeOperator, metadata);
        }
        if (left.isYearMonthDuration() && right.isYearMonthDuration()) {
            Period l = left.getPeriodValue();
            Period r = right.getPeriodValue();
            return processYearMonthDuration(l, r, multiplicativeOperator, metadata);
        }
        if (left.isDayTimeDuration() && right.isDayTimeDuration()) {
            Duration l = left.getDurationValue();
            Duration r = right.getDurationValue();
            return processDayTimeDuration(l, r, multiplicativeOperator, metadata);
        }
        if (left.isYearMonthDuration() && right.isNumeric()) {
            Period l = left.getPeriodValue();
            double r;
            if (right.isDouble()) {
                r = right.getDoubleValue();
            } else {
                r = right.castToDoubleValue();
            }
            return processYearMonthDurationDouble(l, r, multiplicativeOperator, metadata);
        }
        if (left.isDayTimeDuration() && right.isNumeric()) {
            Duration l = left.getDurationValue();
            double r;
            if (right.isDouble()) {
                r = right.getDoubleValue();
            } else {
                r = right.castToDoubleValue();
            }
            return processDayTimeDurationDouble(l, r, multiplicativeOperator, metadata);
        }
        if (
            left.isNumeric() && right.isYearMonthDuration() && multiplicativeOperator.equals(MultiplicativeOperator.MUL)
        ) {
            Period r = right.getPeriodValue();
            double l;
            if (left.isDouble()) {
                l = left.getDoubleValue();
            } else {
                l = left.castToDoubleValue();
            }
            return processYearMonthDurationDouble(r, l, multiplicativeOperator, metadata);
        }
        if (
            left.isNumeric() && right.isDayTimeDuration() && multiplicativeOperator.equals(MultiplicativeOperator.MUL)
        ) {
            Duration r = right.getDurationValue();
            double l;
            if (left.isDouble()) {
                l = left.getDoubleValue();
            } else {
                l = left.castToDoubleValue();
            }
            return processDayTimeDurationDouble(r, l, multiplicativeOperator, metadata);
        }
        throw new UnexpectedTypeException(
                " \""
                    + multiplicativeOperator
                    + "\": operation not possible with parameters of type \""
                    + left.getDynamicType().toString()
                    + "\" and \""
                    + right.getDynamicType().toString()
                    + "\"",
                metadata
        );
    }

    private static Item processDouble(
            double l,
            double r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createDoubleItem(l * r);
            case DIV:
                return ItemFactory.getInstance().createDoubleItem(l / r);
            case IDIV:
                if (r == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                if (Double.isNaN(l) || Double.isInfinite(l)) {
                    throw new NumericOverflowOrUnderflow("Left side of division is infinite or NaN: " + l, metadata);
                }
                if (Double.isNaN(r)) {
                    throw new NumericOverflowOrUnderflow("Right side of division is NaN: " + r, metadata);
                }
                if (Double.isInfinite(r)) {
                    return ItemFactory.getInstance().createIntegerItem(BigInteger.ZERO);
                }
                return processDecimal(BigDecimal.valueOf(l), BigDecimal.valueOf(r), multiplicativeOperator, metadata);
            case MOD:
                return ItemFactory.getInstance().createDoubleItem(l % r);
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        metadata
                );
        }
    }

    private static Item processFloat(
            float l,
            float r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createFloatItem(l * r);
            case DIV:
                return ItemFactory.getInstance().createFloatItem(l / r);
            case IDIV:
                if (r == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                if (Float.isNaN(l) || Float.isInfinite(l)) {
                    throw new NumericOverflowOrUnderflow("Left side of division is infinite or NaN: " + l, metadata);
                }
                if (Float.isNaN(r)) {
                    throw new NumericOverflowOrUnderflow("Right side of division is NaN: " + r, metadata);
                }
                if (Float.isInfinite(r)) {
                    return ItemFactory.getInstance().createIntegerItem(BigInteger.ZERO);
                }
                return processDecimal(BigDecimal.valueOf(l), BigDecimal.valueOf(r), multiplicativeOperator, metadata);
            case MOD:
                return ItemFactory.getInstance().createFloatItem(l % r);
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        metadata
                );
        }
    }

    private static Item processDecimal(
            BigDecimal l,
            BigDecimal r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createDecimalItem(l.multiply(r));
            case DIV:
                if (r.compareTo(BigDecimal.ZERO) == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createDecimalItem(l.divide(r, 18, RoundingMode.HALF_UP));
            case IDIV:
                if (r.compareTo(BigDecimal.ZERO) == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createIntegerItem(l.divide(r, 0, RoundingMode.DOWN).toBigInteger());
            case MOD:
                if (r.compareTo(BigDecimal.ZERO) == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createDecimalItem(l.remainder(r));
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        metadata
                );
        }
    }

    private static Item processInteger(
            BigInteger l,
            BigInteger r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createIntegerItem(l.multiply(r));
            case DIV:
                if (r.equals(BigInteger.ZERO)) {
                    throw new DivisionByZeroException(metadata);
                }
                BigDecimal bdResult = new BigDecimal(l).divide(new BigDecimal(r), 18, RoundingMode.HALF_UP);
                if (bdResult.stripTrailingZeros().scale() <= 0) {
                    return ItemFactory.getInstance().createIntegerItem(bdResult.toBigIntegerExact());
                } else {
                    return ItemFactory.getInstance().createDecimalItem(bdResult);
                }
            case IDIV:
                if (r.equals(BigInteger.ZERO)) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createIntegerItem(l.divide(r));
            case MOD:
                if (r.equals(BigInteger.ZERO)) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createIntegerItem(l.remainder(r));
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        metadata
                );
        }
    }

    private static Item processInt(
            int l,
            int r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case MUL:
                return ItemFactory.getInstance().createIntItem(l * r);
            case DIV:
                if (r == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                BigDecimal bdResult = new BigDecimal(l).divide(new BigDecimal(r), 18, RoundingMode.HALF_UP);
                if (bdResult.stripTrailingZeros().scale() <= 0) {
                    return ItemFactory.getInstance().createIntItem(bdResult.intValueExact());
                } else {
                    return ItemFactory.getInstance().createDecimalItem(bdResult);
                }
            case IDIV:
                if (r == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createIntItem(l / r);
            case MOD:
                if (r == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance().createIntItem(l % r);
            default:
                throw new OurBadException(
                        "Non recognized multiplicative operator: " + multiplicativeOperator,
                        metadata
                );
        }
    }

    private static Item processYearMonthDuration(
            Period l,
            Period r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case DIV:
                int months = l.getYears() * 12 + l.getMonths();
                int otherMonths = 12 * r.getYears() + r.getMonths();
                if (otherMonths == 0) {
                    throw new DivisionByZeroException(metadata);
                }
                return ItemFactory.getInstance()
                    .createDecimalItem(
                        BigDecimal.valueOf(months).divide(BigDecimal.valueOf(otherMonths), 16, RoundingMode.HALF_UP)
                    );
            default:
                throw new UnexpectedTypeException(
                        " \""
                            + multiplicativeOperator
                            + "\": operation not possible with parameters of types yearMonthDuration",
                        metadata
                );
        }
    }

    private static Item processYearMonthDurationDouble(
            Period l,
            double r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        if (Double.isNaN(r)) {
            throw new InvalidNaNOperationException("Invalid operation with NaN value.", metadata);
        }
        if (Double.isInfinite(r)) {
            throw new DurationOverflowOrUnderflow("Overflow after multiplying duration with infinity.", metadata);
        }
        switch (multiplicativeOperator) {
            case MUL: {
                int months = l.getYears() * 12 + l.getMonths();
                int totalMonths = (int) Math.round(months * r);
                try {
                    return ItemFactory.getInstance().createYearMonthDurationItem(Period.ofMonths(totalMonths));
                } catch (ArithmeticException e) {
                    throw new DatetimeOverflowOrUnderflow(e.getMessage(), metadata);
                }
            }
            case DIV: {
                int months = l.getYears() * 12 + l.getMonths();
                if (r == -0) {
                    throw new DurationOverflowOrUnderflow("Division of a duration by 0.", metadata);
                }
                int totalMonths = (int) Math.round(months / r);
                return ItemFactory.getInstance().createYearMonthDurationItem(Period.ofMonths(totalMonths));
            }
            default:
                throw new UnexpectedTypeException(
                        " \""
                            + multiplicativeOperator
                            + "\": operation not possible with parameters of types yearMonthDuration and double",
                        metadata
                );
        }
    }

    private static Item processDayTimeDuration(
            Duration l,
            Duration r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        switch (multiplicativeOperator) {
            case DIV:
                return ItemFactory.getInstance()
                    .createDecimalItem(BigDecimal.valueOf(l.toNanos() / (double) r.toNanos()));
            default:
                throw new UnexpectedTypeException(
                        " \""
                            + multiplicativeOperator
                            + "\": operation not possible with parameters of types dayTimeDuration",
                        metadata
                );
        }
    }

    private static Item processDayTimeDurationDouble(
            Duration l,
            double r,
            MultiplicativeExpression.MultiplicativeOperator multiplicativeOperator,
            ExceptionMetadata metadata
    ) {
        if (Double.isNaN(r)) {
            throw new InvalidNaNOperationException("Invalid operation with NaN value.", metadata);
        }
        switch (multiplicativeOperator) {
            case MUL: {
                long duration = l.toNanos();
                return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofNanos((long) (duration * r)));
            }
            case DIV: {
                long duration = l.toNanos();
                if (r == 0) {
                    throw new DurationOverflowOrUnderflow("Division of a duration by 0.", metadata);
                }
                try {
                    return ItemFactory.getInstance().createDayTimeDurationItem(Duration.ofNanos((long) (duration / r)));
                } catch (ArithmeticException e) {
                    throw new DatetimeOverflowOrUnderflow(e.getMessage(), metadata);
                }
            }
            default:
                throw new UnexpectedTypeException(
                        " \""
                            + multiplicativeOperator
                            + "\": operation not possible with parameters of types yearMonthDuration and double",
                        metadata
                );
        }
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        NativeClauseContext leftResult = this.leftIterator.generateNativeQuery(nativeClauseContext);
        if (leftResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!leftResult.getResultingType().getArity().equals(Arity.One)) {
            return NativeClauseContext.NoNativeQuery;
        }
        NativeClauseContext rightResult = this.rightIterator.generateNativeQuery(nativeClauseContext);
        if (rightResult == NativeClauseContext.NoNativeQuery) {
            return NativeClauseContext.NoNativeQuery;
        }
        if (!rightResult.getResultingType().getArity().equals(Arity.One)) {
            return NativeClauseContext.NoNativeQuery;
        }
        ItemType resultType;
        String leftQuery = leftResult.getResultingQuery();
        String rightQuery = rightResult.getResultingQuery();
        if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)
                && rightResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!rightResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)) {
                rightQuery = "(CAST (" + rightQuery + " AS DOUBLE))";
            }
            resultType = BuiltinTypesCatalogue.doubleItem;
        } else if (
            rightResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)
                && leftResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!leftResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)) {
                leftQuery = "(CAST (" + leftQuery + " AS DOUBLE))";
            }
            resultType = BuiltinTypesCatalogue.doubleItem;
        } else if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)
                && rightResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!rightResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)) {
                rightQuery = "(CAST (" + rightQuery + " AS FLOAT))";
            }
            resultType = BuiltinTypesCatalogue.floatItem;
        } else if (
            rightResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)
                && leftResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!leftResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)) {
                leftQuery = "(CAST (" + leftQuery + " AS FLOAT))";
            }
            resultType = BuiltinTypesCatalogue.floatItem;
        } else if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.INTEGER_QM)
                && rightResult.getResultingType().isSubtypeOf(SequenceType.INTEGER_QM)
        ) {
            if (this.multiplicativeOperator.equals(MultiplicativeExpression.MultiplicativeOperator.DIV)) {
                resultType = BuiltinTypesCatalogue.decimalItem;
            } else {
                resultType = BuiltinTypesCatalogue.integerItem;
            }
        } else if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.DECIMAL_QM)
                && rightResult.getResultingType().isSubtypeOf(SequenceType.DECIMAL_QM)
        ) {
            resultType = BuiltinTypesCatalogue.decimalItem;
        } else {
            return NativeClauseContext.NoNativeQuery;
        }
        String resultingQuery;

        SequenceType.Arity resultingArity = leftResult.getResultingType()
            .getArity()
            .multiplyWith(rightResult.getResultingType().getArity());

        if (resultingArity.equals(Arity.OneOrMore) || resultingArity.equals(Arity.ZeroOrMore)) {
            throw new UnexpectedTypeException(
                    " \"+\": operation not possible with parameters of type \""
                        + this.left.getDynamicType().toString()
                        + "\" and \""
                        + this.right.getDynamicType().toString()
                        + "\"",
                    getMetadata()
            );
        }
        switch (this.multiplicativeOperator) {
            case MUL:
                resultingQuery = "( " + leftQuery + " * " + rightQuery + " )";
                return new NativeClauseContext(
                        nativeClauseContext,
                        resultingQuery,
                        new SequenceType(resultType, resultingArity)
                );
            case DIV:
                resultingQuery = "( " + leftQuery + " / " + rightQuery + " )";
                return new NativeClauseContext(
                        nativeClauseContext,
                        resultingQuery,
                        new SequenceType(resultType, resultingArity)
                );
            case MOD:
                resultingQuery = "( " + leftQuery + " % " + rightQuery + " )";
                return new NativeClauseContext(
                        nativeClauseContext,
                        resultingQuery,
                        new SequenceType(resultType, resultingArity)
                );
            default:
                return NativeClauseContext.NoNativeQuery;
        }
    }

}
