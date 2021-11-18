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

package org.rumbledb.runtime.functions.sequences.aggregate;

import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.*;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.items.ItemComparator;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.AtMostOneItemLocalRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.FlworDataFrameUtils;
import org.rumbledb.runtime.primary.VariableReferenceIterator;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import sparksoniq.spark.SparkSessionManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MinFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private double currentMinDouble;
    private float currentMinFloat;
    private BigDecimal currentMinDecimal;
    private long currentMinLong;
    private String currentMinURI;
    private String currentMinString;
    private boolean currentMinBoolean;
    private boolean hasTimeZone = false;
    private DateTime currentMinDate; // TODO: Change to Date type but had issues with Java compiler
    private DateTime currentMinDateTime;
    private Period currentMinDayTimeDuration;
    private Period currentMinYearMonthDuration;
    private DateTime currentMinTime;
    private byte activeType = 0;
    private ItemType returnType;
    private Item result;
    private ItemComparator comparator;


    public MinFunctionIterator(
            List<RuntimeIterator> arguments,
            ExecutionMode executionMode,
            ExceptionMetadata iteratorMetadata
    ) {
        super(arguments, executionMode, iteratorMetadata);
        this.iterator = this.children.get(0);
        this.comparator = new ItemComparator(
                true,
                new InvalidArgumentTypeException(
                        "Min expression input error. Input has to be non-null atomics of matching types",
                        getMetadata()
                )
        );
    }

    @Override
    public Item materializeFirstItemOrNull(DynamicContext context) {
        if (this.children.size() == 2) {
            String collation = this.children.get(1).materializeFirstItemOrNull(context).getStringValue();
            if (!collation.equals("http://www.w3.org/2005/xpath-functions/collation/codepoint")) {
                throw new UnsupportedCollationException("Wrong collation parameter", getMetadata());
            }
        }

        if (!this.iterator.isRDDOrDataFrame()) {
            this.iterator.open(context);
            Item candidateItem = null;
            ItemType candidateType = null;
            Instant now = null;
            while (this.iterator.hasNext()) {
                candidateItem = this.iterator.next();
                if (candidateItem.isNull()) {
                    return ItemFactory.getInstance().createNullItem();
                }
                candidateType = candidateItem.getDynamicType();
                switch (this.activeType) {
                    case 0:
                        if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.longItem)) {
                            this.activeType = 1;
                            this.currentMinLong = candidateItem.castToDecimalValue().longValue();
                        } else if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
                            this.activeType = 2;
                            this.currentMinDecimal = candidateItem.castToDecimalValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.floatItem)) {
                            this.activeType = 3;
                            this.currentMinFloat = candidateItem.castToFloatValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.doubleItem)) {
                            this.activeType = 4;
                            this.currentMinDouble = candidateItem.castToDoubleValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.anyURIItem)) {
                            this.activeType = 5;
                            this.currentMinURI = candidateItem.getStringValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.stringItem)) {
                            this.activeType = 6;
                            this.currentMinString = candidateItem.getStringValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.booleanItem)) {
                            this.activeType = 7;
                            this.currentMinBoolean = candidateItem.getBooleanValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.dateItem)) {
                            this.activeType = 8;
                            this.currentMinDate = candidateItem.getDateTimeValue();
                            if (candidateItem.hasTimeZone()) {
                                this.hasTimeZone = true;
                            }
                        } else if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)) {
                            this.activeType = 9;
                            this.currentMinDateTime = candidateItem.getDateTimeValue();
                            if (candidateItem.hasTimeZone()) {
                                this.hasTimeZone = true;
                            }
                        } else if (candidateType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                            this.activeType = 10;
                            this.currentMinDayTimeDuration = candidateItem.getDurationValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                            this.activeType = 11;
                            this.currentMinYearMonthDuration = candidateItem.getDurationValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.timeItem)) {
                            this.activeType = 12;
                            this.currentMinTime = candidateItem.getDateTimeValue();
                            if (candidateItem.hasTimeZone()) {
                                this.hasTimeZone = true;
                            }
                        } else {
                            throw new OurBadException("Inconsistent state in state iteration");
                        }
                        this.returnType = candidateType;
                        break;
                    case 1:
                        // long
                        if (!candidateItem.isNumeric()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.longItem)) {
                            long candidateItemLong = candidateItem.castToDecimalValue().longValue();
                            if (candidateItemLong < this.currentMinLong) {
                                this.currentMinLong = candidateItemLong;
                                this.returnType = candidateType;
                            }
                        }
                        if (candidateItem.isDecimal()) {
                            this.activeType = 1;
                            this.currentMinDecimal = new BigDecimal(this.currentMinLong);
                            BigDecimal candidateItemDecimal = candidateItem.getDecimalValue();
                            if (candidateItemDecimal.compareTo(this.currentMinDecimal) < 0) {
                                this.currentMinDecimal = candidateItemDecimal;
                                this.returnType = candidateType;
                                this.activeType = 2;
                            }
                        } else if (candidateItem.isFloat()) {
                            this.activeType = 3;
                            this.returnType = BuiltinTypesCatalogue.floatItem;
                            this.currentMinFloat = this.currentMinLong;
                            float candidateItemFloat = candidateItem.getFloatValue();
                            if (Float.isNaN(candidateItemFloat)) {
                                this.currentMinFloat = Float.NaN;
                            } else if (candidateItemFloat < this.currentMinFloat) {
                                this.currentMinFloat = candidateItemFloat;
                            }
                        } else if (candidateItem.isDouble()) {
                            this.activeType = 4;
                            this.returnType = BuiltinTypesCatalogue.doubleItem;
                            this.currentMinDouble = this.currentMinLong;
                            double candidateItemDouble = candidateItem.getDoubleValue();
                            if (Double.isNaN(candidateItemDouble)) {
                                this.currentMinDouble = Double.NaN;
                            } else if (candidateItemDouble < this.currentMinDouble) {
                                this.currentMinDouble = candidateItemDouble;
                            }
                        }
                        break;
                    case 2:
                        // decimal
                        if (!candidateItem.isNumeric()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare decimal with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.isFloat()) {
                            this.activeType = 3;
                            this.returnType = BuiltinTypesCatalogue.floatItem;
                            this.currentMinFloat = this.currentMinDecimal.floatValue();
                            float candidateItemFloat = candidateItem.getFloatValue();
                            if (Float.isNaN(candidateItemFloat)) {
                                this.currentMinFloat = Float.NaN;
                            } else if (candidateItemFloat < this.currentMinFloat) {
                                this.currentMinFloat = candidateItemFloat;
                            }
                        } else if (candidateItem.isDouble()) {
                            this.activeType = 4;
                            this.returnType = BuiltinTypesCatalogue.doubleItem;
                            this.currentMinDouble = this.currentMinDecimal.doubleValue();
                            double candidateItemDouble = candidateItem.getDoubleValue();
                            if (Double.isNaN(candidateItemDouble)) {
                                this.currentMinDouble = Double.NaN;
                            } else if (candidateItemDouble < this.currentMinDouble) {
                                this.currentMinDouble = candidateItemDouble;
                            }
                        } else {
                            BigDecimal candidateItemDecimal = candidateItem.castToDecimalValue();
                            if (candidateItemDecimal.compareTo(this.currentMinDecimal) < 0) {
                                this.currentMinDecimal = candidateItemDecimal;
                                this.returnType = candidateType;
                            }
                        }

                        break;
                    case 3:
                        if (!candidateItem.isNumeric()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare float with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.isDouble()) {
                            this.activeType = 4;
                            this.returnType = BuiltinTypesCatalogue.doubleItem;
                            this.currentMinDouble = this.currentMinFloat;
                            double candidateItemDouble = candidateItem.getDoubleValue();
                            if (Double.isNaN(candidateItemDouble) || candidateItemDouble < this.currentMinDouble) {
                                this.currentMinDouble = candidateItemDouble;
                            }
                        } else {
                            if (!Float.isNaN(this.currentMinFloat)) {
                                float candidateItemFloat = candidateItem.castToFloatValue();
                                if (candidateItemFloat < this.currentMinFloat) {
                                    this.currentMinFloat = candidateItemFloat;
                                }
                            }
                        }

                        break;
                    case 4:
                        if (!candidateItem.isNumeric()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare double with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (!Double.isNaN(this.currentMinDouble)) {
                            double candidateItemDouble = candidateItem.castToDoubleValue();
                            if (candidateItemDouble < this.currentMinDouble) {
                                this.currentMinDouble = candidateItemDouble;
                            }
                        }
                        break;
                    case 5:
                        if (!candidateItem.isString() && !candidateItem.isAnyURI()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare anyURI with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.isString()) {
                            this.activeType = 6;
                            this.returnType = BuiltinTypesCatalogue.stringItem;
                            this.currentMinString = this.currentMinURI;
                            if (candidateItem.getStringValue().compareTo(this.currentMinURI) < 0) {
                                this.currentMinString = candidateItem.getStringValue();
                            }
                        } else if (candidateItem.getStringValue().compareTo(this.currentMinURI) < 0) {
                            this.currentMinURI = candidateItem.getStringValue();

                        }
                        break;
                    case 6:
                        if (!candidateItem.isString() && !candidateItem.isAnyURI()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare string with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getStringValue().compareTo(this.currentMinString) < 0) {
                            this.currentMinString = candidateItem.getStringValue();
                            this.returnType = candidateType;
                        }
                        break;
                    case 7:
                        if (!candidateType.equals(BuiltinTypesCatalogue.booleanItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (Boolean.compare(candidateItem.getBooleanValue(), this.currentMinBoolean) < 0) {
                            this.currentMinBoolean = candidateItem.getBooleanValue();
                        }
                        break;
                    case 8:
                        if (!candidateType.equals(BuiltinTypesCatalogue.dateItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getDateTimeValue().toDate().compareTo(this.currentMinDate.toDate()) < 0) {
                            this.currentMinDate = candidateItem.getDateTimeValue();
                            this.hasTimeZone = candidateItem.hasTimeZone();
                        }
                        break;
                    case 9:
                        if (!candidateType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getDateTimeValue().compareTo(this.currentMinDateTime) < 0) {
                            this.currentMinDateTime = candidateItem.getDateTimeValue();
                            this.hasTimeZone = candidateItem.hasTimeZone();
                        }
                        break;
                    case 10:
                        if (!candidateType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        now = new Instant();
                        if (
                            candidateItem.getDurationValue()
                                .toDurationFrom(now)
                                .compareTo(this.currentMinDayTimeDuration.toDurationFrom(now)) < 0
                        ) {
                            this.currentMinDayTimeDuration = candidateItem.getDurationValue();
                        }
                        break;
                    case 11:
                        if (!candidateType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        now = new Instant();
                        if (
                            candidateItem.getDurationValue()
                                .toDurationFrom(now)
                                .compareTo(this.currentMinYearMonthDuration.toDurationFrom(now)) < 0
                        ) {
                            this.currentMinYearMonthDuration = candidateItem.getDurationValue();
                        }
                        break;
                    case 12:
                        if (!candidateType.equals(BuiltinTypesCatalogue.timeItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getDateTimeValue().compareTo(this.currentMinTime) < 0) {
                            this.currentMinTime = candidateItem.getDateTimeValue();
                            this.hasTimeZone = candidateItem.hasTimeZone();
                        }
                        break;
                    default:
                        throw new OurBadException("Inconsistent state in state iteration");
                }

            }

            this.iterator.close();

            Item itemResult;
            switch (this.activeType) {
                case 0:
                    return null;
                case 1:
                    itemResult = ItemFactory.getInstance().createLongItem(this.currentMinLong);
                    break;
                case 2:
                    itemResult = ItemFactory.getInstance().createDecimalItem(this.currentMinDecimal);
                    break;
                case 3:
                    itemResult = ItemFactory.getInstance().createFloatItem(this.currentMinFloat);
                    break;
                case 4:
                    itemResult = ItemFactory.getInstance().createDoubleItem(this.currentMinDouble);
                    break;
                case 5:
                    itemResult = ItemFactory.getInstance().createAnyURIItem(this.currentMinURI);
                    break;
                case 6:
                    itemResult = ItemFactory.getInstance().createStringItem(this.currentMinString);
                    break;
                case 7:
                    itemResult = ItemFactory.getInstance().createBooleanItem(this.currentMinBoolean);
                    break;
                case 8:
                    itemResult = ItemFactory.getInstance().createDateItem(this.currentMinDate, this.hasTimeZone);
                    break;
                case 9:
                    itemResult = ItemFactory.getInstance()
                        .createDateTimeItem(this.currentMinDateTime, this.hasTimeZone);
                    break;
                case 10:
                    itemResult = ItemFactory.getInstance().createDayTimeDurationItem(this.currentMinDayTimeDuration);
                    break;
                case 11:
                    itemResult = ItemFactory.getInstance()
                        .createYearMonthDurationItem(this.currentMinYearMonthDuration);
                    break;
                case 12:
                    itemResult = ItemFactory.getInstance().createTimeItem(this.currentMinTime, this.hasTimeZone);
                    break;
                default:
                    throw new OurBadException("Inconsistent state in state iteration");
            }
            return CastIterator.castItemToType(itemResult, this.returnType, getMetadata());

        }

        if (this.iterator.isDataFrame()) {
            JSoundDataFrame df = this.iterator.getDataFrame(context);
            if (df.isEmptySequence()) {
                return null;
            }
            String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
            JSoundDataFrame minDF = df.evaluateSQL(
                String.format(
                    "SELECT MIN(`%s`) as `%s` FROM %s",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    input
                ),
                df.getItemType()
            );
            return minDF.getExactlyOneItem();
        }

        JavaRDD<Item> rdd = this.iterator.getRDD(context);
        if (rdd.isEmpty()) {
            return null;
        }
        this.result = rdd.min(this.comparator);
        return this.result;
    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.MIN);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }

}
