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
import java.util.Arrays;

import java.time.ZonedDateTime;
import java.time.Period;
import java.time.Duration;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.SequenceType;
import org.rumbledb.types.SequenceType.Arity;


public class AdditiveOperationIterator extends AtMostOneItemLocalRuntimeIterator {

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
            RuntimeStaticContext staticContext
    ) {
        super(Arrays.asList(leftIterator, rightIterator), staticContext);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.isMinus = isMinus;
    }

    public Item materializeFirstItemOrNull(
            DynamicContext dynamicContext
    ) {
        try {
            this.left = this.leftIterator.materializeAtMostOneItemOrNull(dynamicContext);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Addition expression requires at most one item in its left input sequence.",
                    getMetadata()
            );
        }
        try {
            this.right = this.rightIterator.materializeAtMostOneItemOrNull(dynamicContext);
        } catch (MoreThanOneItemException e) {
            throw new UnexpectedTypeException(
                    "Addition expression requires at most one item in its right input sequence.",
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
                    +
                    "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                this.left.getDynamicType().toString()
            );
            throw new NonAtomicKeyException(message, getMetadata());
        }
        if (!this.right.isAtomic()) {
            String message = String.format(
                "Can not atomize an %1$s item: an %1$s has probably been passed where "
                    +
                    "an atomic value is expected (e.g., as a key, or to a function expecting an atomic item)",
                this.right.getDynamicType().toString()
            );
            throw new NonAtomicKeyException(message, getMetadata());
        }
        Item result = processItem(this.left, this.right, this.isMinus);
        if (result == null) {
            throw new UnexpectedTypeException(
                    " \"+\": operation not possible with parameters of type \""
                        + this.left.getDynamicType().toString()
                        + "\" and \""
                        + this.right.getDynamicType().toString()
                        + "\"",
                    getMetadata()
            );
        }
        return result;
    }

    public static Item processItem(
            Item left,
            Item right,
            boolean isMinus
    ) {
        // The integer 0 is considered the default neutral element for addition in sum(), even though
        // it is technically incompatible with durations. In the future, we should
        // make sure an error is thrown if an actual 0 appears in the sum with durations.
        if (!isMinus && left.isInteger() && left.getIntegerValue().equals(BigInteger.ZERO)) {
            return right;
        }
        if (!isMinus && right.isInteger() && right.getIntegerValue().equals(BigInteger.ZERO)) {
            return left;
        }
        if (
            left.isInt()
                && right.isInt()
        ) {
            if (
                right.isInt()
                    && (left.getIntValue() < Integer.MAX_VALUE / 2
                        && left.getIntValue() > -Integer.MAX_VALUE / 2
                        && right.getIntValue() > -Integer.MAX_VALUE / 2
                        && right.getIntValue() < Integer.MAX_VALUE / 2)
            ) {
                return processInt(left.getIntValue(), right.getIntValue(), isMinus);
            }
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
            return processDouble(l, r, isMinus);
        }
        if (right.isDouble() && left.isNumeric()) {
            double l = left.castToDoubleValue();
            double r = right.getDoubleValue();
            return processDouble(l, r, isMinus);
        }
        if (left.isFloat() && right.isNumeric()) {
            float l = left.getFloatValue();
            float r = 0;
            if (right.isFloat()) {
                r = right.getFloatValue();
            } else {
                r = right.castToFloatValue();
            }
            return processFloat(l, r, isMinus);
        }
        if (right.isFloat() && left.isNumeric()) {
            float l = left.castToFloatValue();
            float r = right.getFloatValue();
            return processFloat(l, r, isMinus);
        }
        if (left.isInteger() && right.isInteger()) {
            BigInteger l = left.getIntegerValue();
            BigInteger r = right.getIntegerValue();
            return processInteger(l, r, isMinus);
        }
        if (left.isDecimal() && right.isDecimal()) {
            BigDecimal l = left.getDecimalValue();
            BigDecimal r = right.getDecimalValue();
            return processDecimal(l, r, isMinus);
        }
        if (left.isYearMonthDuration() && right.isYearMonthDuration()) {
            Period l = left.getPeriodValue();
            Period r = right.getPeriodValue();
            return processYearMonthDuration(l, r, isMinus);
        }
        if (left.isDayTimeDuration() && right.isDayTimeDuration()) {
            Duration l = left.getDurationValue();
            Duration r = right.getDurationValue();
            return processDayTimeDuration(l, r, isMinus);
        }
        if (left.isDate() && right.isYearMonthDuration()) {
            ZonedDateTime l = left.getDateTimeValue();
            Duration r = right.getDurationValue();
            return processDateTimeDurationDate(l, r, isMinus, left.hasTimeZone());
        }
        if (left.isDate() && right.isDayTimeDuration()) {
            ZonedDateTime l = left.getDateTimeValue();
            Duration r = right.getDurationValue();
            return processDateTimeDurationDate(l, r, isMinus, left.hasTimeZone());
        }
        if (left.isYearMonthDuration() && right.isDate()) {
            if (!isMinus) {
                Duration l = left.getDurationValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDurationDate(r, l, isMinus, right.hasTimeZone());
            }
        }
        if (left.isDayTimeDuration() && right.isDate()) {
            if (!isMinus) {
                Duration l = left.getDurationValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDurationDate(r, l, isMinus, right.hasTimeZone());
            }
        }
        if (left.isTime() && right.isDayTimeDuration()) {
            ZonedDateTime l = left.getDateTimeValue();
            Duration r = right.getDurationValue();
            return processDateTimeDurationTime(l, r, isMinus, left.hasTimeZone());
        }
        if (left.isDayTimeDuration() && right.isTime()) {
            if (!isMinus) {
                Duration l = left.getDurationValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDurationTime(r, l, isMinus, right.hasTimeZone());
            }
        }
        if (left.isDateTime() && right.isYearMonthDuration()) {
            ZonedDateTime l = left.getDateTimeValue();
            Duration r = right.getDurationValue();
            return processDateTimeDurationDateTime(l, r, isMinus, left.hasTimeZone());
        }
        if (left.isDateTime() && right.isDayTimeDuration()) {
            ZonedDateTime l = left.getDateTimeValue();
            Duration r = right.getDurationValue();
            return processDateTimeDurationDateTime(l, r, isMinus, left.hasTimeZone());
        }
        if (left.isYearMonthDuration() && right.isDateTime()) {
            if (!isMinus) {
                Duration l = left.getDurationValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDurationDateTime(r, l, isMinus, right.hasTimeZone());
            }
        }
        if (left.isDayTimeDuration() && right.isDateTime()) {
            if (!isMinus) {
                Duration l = left.getDurationValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDurationDateTime(r, l, isMinus, right.hasTimeZone());
            }
        }
        if (left.isDate() && right.isDate()) {
            if (isMinus) {
                ZonedDateTime l = left.getDateTimeValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDayTime(l, r);
            }
        }
        if (left.isTime() && right.isTime()) {
            if (isMinus) {
                ZonedDateTime l = left.getDateTimeValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDayTime(l, r);
            }
        }
        if (left.isDateTime() && right.isDateTime()) {
            if (isMinus) {
                ZonedDateTime l = left.getDateTimeValue();
                ZonedDateTime r = right.getDateTimeValue();
                return processDateTimeDayTime(l, r);
            }
        }
        return null;
    }

    private static Item processDouble(
            double l,
            double r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createDoubleItem(l - r);
        } else {
            return ItemFactory.getInstance().createDoubleItem(l + r);
        }
    }

    private static Item processFloat(
            float l,
            float r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createFloatItem(l - r);
        } else {
            return ItemFactory.getInstance().createFloatItem(l + r);
        }
    }

    private static Item processDecimal(
            BigDecimal l,
            BigDecimal r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createDecimalItem(l.subtract(r));
        } else {
            return ItemFactory.getInstance().createDecimalItem(l.add(r));
        }
    }

    private static Item processInteger(
            BigInteger l,
            BigInteger r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createIntegerItem(l.subtract(r));
        } else {
            return ItemFactory.getInstance().createIntegerItem(l.add(r));
        }
    }

    private static Item processInt(
            int l,
            int r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createIntItem(l - r);
        } else {
            return ItemFactory.getInstance().createIntItem(l + r);
        }
    }

    private static Item processYearMonthDuration(
            Period l,
            Period r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createYearMonthDurationItem(l.minus(r));
        } else {
            return ItemFactory.getInstance().createYearMonthDurationItem(l.plus(r));
        }
    }

    private static Item processDayTimeDuration(
            Duration l,
            Duration r,
            boolean isMinus
    ) {
        if (isMinus) {
            return ItemFactory.getInstance().createDayTimeDurationItem(l.minus(r));
        } else {
            return ItemFactory.getInstance().createDayTimeDurationItem(l.plus(r));
        }
    }

    private static Item processDateTimeDayTime(
            ZonedDateTime l,
            ZonedDateTime r
    ) {
        return ItemFactory.getInstance()
            .createDayTimeDurationItem(Duration.between(r, l));
    }

    private static Item processDateTimeDurationDate(
            ZonedDateTime l,
            Duration r,
            boolean isMinus,
            boolean timeZone
    ) {
        if (isMinus) {
            return ItemFactory.getInstance()
                .createDateItem(l.minus(r).toOffsetDateTime(), timeZone);
        } else {
            return ItemFactory.getInstance()
                .createDateItem(l.plus(r).toOffsetDateTime(), timeZone);
        }
    }

    private static Item processDateTimeDurationTime(
            ZonedDateTime l,
            Duration r,
            boolean isMinus,
            boolean timeZone
    ) {
        if (isMinus) {
            return ItemFactory.getInstance()
                .createTimeItem(l.minus(r).toOffsetDateTime().toOffsetTime(), timeZone);
        } else {
            return ItemFactory.getInstance()
                .createTimeItem(l.plus(r).toOffsetDateTime().toOffsetTime(), timeZone);
        }
    }

    private static Item processDateTimeDurationDateTime(
            ZonedDateTime l,
            Duration r,
            boolean isMinus,
            boolean timeZone
    ) {
        if (isMinus) {
            return ItemFactory.getInstance()
                .createDateTimeItem(l.minus(r), timeZone);
        } else {
            return ItemFactory.getInstance()
                .createDateTimeItem(l.plus(r), timeZone);
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
        ItemType resultType = null;
        String leftQuery = leftResult.getResultingQuery();
        String rightQuery = rightResult.getResultingQuery();
        if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)
                &&
                rightResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!rightResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)) {
                rightQuery = "(CAST (" + rightQuery + " AS DOUBLE))";
            }
            resultType = BuiltinTypesCatalogue.doubleItem;
        } else if (
            rightResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)
                &&
                leftResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!leftResult.getResultingType().isSubtypeOf(SequenceType.DOUBLE_QM)) {
                leftQuery = "(CAST (" + leftQuery + " AS DOUBLE))";
            }
            resultType = BuiltinTypesCatalogue.doubleItem;
        } else if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)
                &&
                rightResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!rightResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)) {
                rightQuery = "(CAST (" + rightQuery + " AS FLOAT))";
            }
            resultType = BuiltinTypesCatalogue.floatItem;
        } else if (
            rightResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)
                &&
                leftResult.getResultingType().getItemType().isNumeric()
        ) {
            if (!leftResult.getResultingType().isSubtypeOf(SequenceType.FLOAT_QM)) {
                leftQuery = "(CAST (" + leftQuery + " AS FLOAT))";
            }
            resultType = BuiltinTypesCatalogue.floatItem;
        } else if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.INTEGER_QM)
                &&
                rightResult.getResultingType().isSubtypeOf(SequenceType.INTEGER_QM)
        ) {
            resultType = BuiltinTypesCatalogue.integerItem;
        } else if (
            leftResult.getResultingType().isSubtypeOf(SequenceType.DECIMAL_QM)
                &&
                rightResult.getResultingType().isSubtypeOf(SequenceType.DECIMAL_QM)
        ) {
            resultType = BuiltinTypesCatalogue.decimalItem;
        } else {
            return NativeClauseContext.NoNativeQuery;
        }

        SequenceType.Arity resultingArity =
            leftResult.getResultingType()
                .getArity()
                .multiplyWith(
                    rightResult.getResultingType().getArity()
                );

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
        if (this.isMinus) {
            String resultingQuery = "( "
                + leftQuery
                + " - "
                + rightQuery
                + " )";
            return new NativeClauseContext(
                    nativeClauseContext,
                    resultingQuery,
                    new SequenceType(resultType, resultingArity)
            );
        } else {
            String resultingQuery = "( "
                + leftQuery
                + " + "
                + rightQuery
                + " )";
            return new NativeClauseContext(
                    nativeClauseContext,
                    resultingQuery,
                    new SequenceType(resultType, resultingArity)
            );
        }
    }
}
