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

import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.MoreThanOneItemException;
import org.rumbledb.exceptions.NonAtomicKeyException;
import org.rumbledb.exceptions.UnexpectedTypeException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.BuiltinTypesCatalogue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


public class ComparisonIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private Item left;
    private Item right;
    private ComparisonExpression.ComparisonOperator comparisonOperator;
    private RuntimeIterator leftIterator;
    private RuntimeIterator rightIterator;


    public ComparisonIterator(
            RuntimeIterator leftIterator,
            RuntimeIterator rightIterator,
            ComparisonExpression.ComparisonOperator comparisonOperator,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(Arrays.asList(leftIterator, rightIterator), executionMode, iteratorMetadata);
        this.leftIterator = leftIterator;
        this.rightIterator = rightIterator;
        this.comparisonOperator = comparisonOperator;
    }

    public ComparisonExpression.ComparisonOperator getComparisonOperator() {
        return this.comparisonOperator;
    }

    public boolean isValueEquality() {
        return this.comparisonOperator.equals(ComparisonExpression.ComparisonOperator.VC_EQ);
    }

    public RuntimeIterator getLeftIterator() {
        return this.leftIterator;
    }

    public RuntimeIterator getRightIterator() {
        return this.rightIterator;
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext dynamicContext) {
        // value comparison may return an empty sequence
        if (this.comparisonOperator.isValueComparison()) {
            // if EMPTY SEQUENCE - eg. () or ((),())
            // this check is added here to provide lazy evaluation: eg. () eq (2,3) = () instead of exception
            try {
                this.left = this.leftIterator.materializeAtMostOneItemOrNull(
                    dynamicContext
                );
            } catch (MoreThanOneItemException e) {
                throw new UnexpectedTypeException(
                        "Invalid args. Value comparison can't be performed on sequences with more than 1 items",
                        getMetadata()
                );
            }
            if (this.left == null) {
                return null;
            }

            try {
                this.right = this.rightIterator.materializeAtMostOneItemOrNull(
                    dynamicContext
                );
            } catch (MoreThanOneItemException e) {
                throw new UnexpectedTypeException(
                        "Invalid args. Value comparison can't be performed on sequences with more than 1 items",
                        getMetadata()
                );
            }
            if (this.right == null) {
                return null;
            }
        }

        // use stored values for value comparison
        if (this.comparisonOperator.isValueComparison()) {
            return valueComparison(this.left, this.right);
        }

        // fetch all values and perform comparison
        List<Item> left = this.leftIterator.materialize(dynamicContext);
        List<Item> right = this.rightIterator.materialize(dynamicContext);

        return generalComparison(left, right);
    }

    /**
     * Function to compare two lists of items one by one with each other.
     *
     * @param left item list of left iterator
     * @param right item list of right iterator
     * @return true if a single match is found, false if no matches. Given an empty sequence, false is returned.
     */
    private Item generalComparison(List<Item> left, List<Item> right) {
        for (Item l : left) {
            for (Item r : right) {
                Item result = valueComparison(l, r);
                if (result.getBooleanValue()) {
                    return result;
                }
            }
        }
        return ItemFactory.getInstance().createBooleanItem(false);
    }

    private Item valueComparison(Item left, Item right) {

        if (left.isArray() || right.isArray()) {
            throw new NonAtomicKeyException(
                    "Invalid args. Comparison can't be performed on array type",
                    getMetadata()
            );
        } else if (left.isObject() || right.isObject()) {
            throw new NonAtomicKeyException(
                    "Invalid args. Comparison can't be performed on object type",
                    getMetadata()
            );
        } else if (left.isFunction() || right.isFunction()) {
            throw new NonAtomicKeyException(
                    "Invalid args. Comparison can't be performed on function type",
                    getMetadata()
            );
        }

        if (!left.isAtomic()) {
            throw new IteratorFlowException("Invalid comparison expression", getMetadata());
        }

        long comparison = compareItems(left, right, this.comparisonOperator, getMetadata());
        if (comparison == -Long.MIN_VALUE) {
            throw new UnexpectedTypeException(
                    " \""
                        + this.comparisonOperator
                        + "\": operation not possible with parameters of type \""
                        + left.getDynamicType().toString()
                        + "\" and \""
                        + right.getDynamicType().toString()
                        + "\"",
                    getMetadata()
            );
        }
        // NaN never compares successfully.
        if (left.isDouble() && Double.isNaN(left.getDoubleValue())) {
            return ItemFactory
                .getInstance()
                .createBooleanItem(false);
        }
        if (right.isDouble() && Double.isNaN(right.getDoubleValue())) {
            return ItemFactory
                .getInstance()
                .createBooleanItem(false);
        }
        return comparisonResultToBooleanItem(
            (int) comparison,
            this.comparisonOperator,
            getMetadata()
        );
    }

    public static long compareItems(
            Item left,
            Item right,
            ComparisonOperator comparisonOperator,
            ExceptionMetadata metadata
    ) {
        if (left.isNull() && right.isNull()) {
            return 0;
        }
        if (left.isNull() && !right.isNull()) {
            return -1;
        }
        if (!left.isNull() && right.isNull()) {
            return 1;
        }
        if (
            left.isInt()
                && right.isInt()
        ) {
            return processInt(left.getIntValue(), right.getIntValue());
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
            return processDouble(l, r);
        }
        if (right.isDouble() && left.isNumeric()) {
            double l = left.castToDoubleValue();
            double r = right.getDoubleValue();
            return processDouble(l, r);
        }
        if (right.isDouble() && left.isNumeric()) {
            double l = left.castToDoubleValue();
            double r = right.getDoubleValue();
            return processDouble(l, r);
        }
        if (left.isFloat() && right.isNumeric()) {
            float l = left.getFloatValue();
            float r = 0;
            if (right.isFloat()) {
                r = right.getFloatValue();
            } else {
                r = right.castToFloatValue();
            }
            return processFloat(l, r);
        }
        if (left.isInteger() && right.isInteger()) {
            BigInteger l = left.getIntegerValue();
            BigInteger r = right.getIntegerValue();
            return processInteger(l, r);
        }
        if (left.isDecimal() && right.isDecimal()) {
            BigDecimal l = left.getDecimalValue();
            BigDecimal r = right.getDecimalValue();
            return processDecimal(l, r);
        }
        if (left.isYearMonthDuration() && right.isYearMonthDuration()) {
            Period l = left.getDurationValue();
            Period r = right.getDurationValue();
            return processDuration(l, r);
        }
        if (left.isDayTimeDuration() && right.isDayTimeDuration()) {
            Period l = left.getDurationValue();
            Period r = right.getDurationValue();
            return processDuration(l, r);
        }
        if (left.isDuration() && right.isDuration()) {
            switch (comparisonOperator) {
                case VC_EQ:
                case GC_EQ:
                case VC_NE:
                case GC_NE:
                    Period l = left.getDurationValue();
                    Period r = right.getDurationValue();
                    return processDuration(l, r);
                default:
            }
        }
        if (left.isHexBinary() && right.isHexBinary()) {
            byte[] l = left.getBinaryValue();
            byte[] r = right.getBinaryValue();
            return processBytes(l, r);
        }
        if (left.isBase64Binary() && right.isBase64Binary()) {
            byte[] l = left.getBinaryValue();
            byte[] r = right.getBinaryValue();
            return processBytes(l, r);
        }
        if (left.isDate() && right.isDate()) {
            DateTime l = left.getDateTimeValue();
            DateTime r = right.getDateTimeValue();
            return processDateTime(l, r);
        }
        if (left.isTime() && right.isTime()) {
            DateTime l = left.getDateTimeValue();
            DateTime r = right.getDateTimeValue();
            return processDateTime(l, r);
        }
        if (left.isDateTime() && right.isDateTime()) {
            DateTime l = left.getDateTimeValue();
            DateTime r = right.getDateTimeValue();
            return processDateTime(l, r);
        }
        if (left.isBoolean() && right.isBoolean()) {
            Boolean l = left.getBooleanValue();
            Boolean r = right.getBooleanValue();
            return processBoolean(l, r);
        }
        if (left.isString() && right.isString()) {
            String l = left.getStringValue();
            String r = right.getStringValue();
            return processString(l, r);
        }
        if (left.isAnyURI() && right.isAnyURI()) {
            String l = left.getStringValue();
            String r = right.getStringValue();
            return processString(l, r);
        }
        if (left.isString() && right.isAnyURI()) {
            String l = left.getStringValue();
            String r = right.serialize();
            return processString(l, r);
        }
        if (left.isAnyURI() && right.isString()) {
            String l = left.serialize();
            String r = right.getStringValue();
            return processString(l, r);
        }
        return Long.MIN_VALUE;
    }

    private static int processDouble(
            double l,
            double r
    ) {
        return Double.compare(l, r);
    }

    private static int processFloat(
            float l,
            float r
    ) {
        return Float.compare(l, r);
    }

    private static int processDecimal(
            BigDecimal l,
            BigDecimal r
    ) {
        return l.compareTo(r);
    }

    private static int processInteger(
            BigInteger l,
            BigInteger r
    ) {
        return l.compareTo(r);
    }

    private static int processInt(
            int l,
            int r
    ) {
        return Integer.compare(l, r);
    }

    private static int processDuration(
            Period l,
            Period r
    ) {
        Instant now = new Instant();
        return l.toDurationFrom(now).compareTo(r.toDurationFrom(now));
    }

    private static int processDateTime(
            DateTime l,
            DateTime r
    ) {
        return l.compareTo(r);
    }

    private static int processBoolean(
            Boolean l,
            Boolean r
    ) {
        return Boolean.compare(l, r);
    }

    private static int processString(
            String l,
            String r
    ) {
        return l.compareTo(r);
    }

    private static int processBytes(
            byte[] l,
            byte[] r
    ) {
        int i = 0;
        while (true) {
            if (i == l.length && i == r.length) {
                return 0;
            }
            if (i == l.length) {
                return -1;
            }
            if (i == r.length) {
                return 1;
            }
            int compare = Integer.compare(Byte.toUnsignedInt(l[i]), Byte.toUnsignedInt(r[i]));
            if (compare != 0) {
                return compare;
            }
            ++i;
        }
    }

    private static Item comparisonResultToBooleanItem(
            int comparison,
            ComparisonExpression.ComparisonOperator comparisonOperator,
            ExceptionMetadata metadata
    ) {
        // Subclasses should override this method to perform additional typechecks,
        // and then invoke it on super.
        switch (comparisonOperator) {
            case VC_EQ:
            case GC_EQ: {
                return ItemFactory.getInstance().createBooleanItem(comparison == 0);
            }
            case VC_NE:
            case GC_NE: {
                return ItemFactory.getInstance().createBooleanItem(comparison != 0);
            }
            case VC_LT:
            case GC_LT: {
                return ItemFactory.getInstance().createBooleanItem(comparison < 0);
            }
            case VC_LE:
            case GC_LE: {
                return ItemFactory.getInstance().createBooleanItem(comparison <= 0);
            }
            case VC_GT:
            case GC_GT: {
                return ItemFactory.getInstance().createBooleanItem(comparison > 0);
            }
            case VC_GE:
            case GC_GE: {
                return ItemFactory.getInstance().createBooleanItem(comparison >= 0);
            }
        }
        throw new IteratorFlowException("Unrecognized operator found: " + comparisonOperator, metadata);
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext nativeClauseContext) {
        if (this.comparisonOperator.isValueComparison()) {
            NativeClauseContext leftResult = this.leftIterator.generateNativeQuery(nativeClauseContext);
            NativeClauseContext rightResult = this.rightIterator.generateNativeQuery(nativeClauseContext);

            if (leftResult == NativeClauseContext.NoNativeQuery || rightResult == NativeClauseContext.NoNativeQuery) {
                return NativeClauseContext.NoNativeQuery;
            }

            // TODO: once done type system do proper comparison
            if (
                !(leftResult.getResultingType().isNumeric() && rightResult.getResultingType().isNumeric()
                    || leftResult.getResultingType() == rightResult.getResultingType())
            ) {
                return NativeClauseContext.NoNativeQuery;
            }

            String operator = " = ";
            switch (this.comparisonOperator.name()) {
                case "eq":
                    operator = " = ";
                    break;
                case "ne":
                    operator = " <> ";
                    break;
                case "le":
                    operator = " <= ";
                    break;
                case "lt":
                    operator = " < ";
                    break;
                case "ge":
                    operator = " >= ";
                    break;
                case "gt":
                    operator = " > ";
                    break;
            }
            String query = "( " + leftResult.getResultingQuery() + operator + rightResult.getResultingQuery() + " )";
            return new NativeClauseContext(nativeClauseContext, query, BuiltinTypesCatalogue.booleanItem);
        } else {
            return NativeClauseContext.NoNativeQuery;
        }
    }
}
