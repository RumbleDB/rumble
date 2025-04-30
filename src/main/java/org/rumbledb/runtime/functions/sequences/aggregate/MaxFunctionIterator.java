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
import java.time.Duration;
import java.time.Period;
import java.time.OffsetDateTime;
import java.time.Instant;

import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.exceptions.*;
import org.rumbledb.items.DurationItem;
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

public class MaxFunctionIterator extends AtMostOneItemLocalRuntimeIterator {


    private static final long serialVersionUID = 1L;
    private RuntimeIterator iterator;
    private transient boolean currentMinIsNullItem = false; // Only happens if all elements are null
    private transient double currentMaxDouble;
    private transient float currentMaxFloat;
    private transient BigDecimal currentMaxDecimal;
    private transient long currentMaxLong;
    private transient String currentMaxURI;
    private transient String currentMaxString;
    private transient boolean currentMaxBoolean;
    private transient boolean hasTimeZone = false;
    private transient OffsetDateTime currentMaxDate;
    private transient OffsetDateTime currentMaxDateTime;
    private transient Duration currentMaxDayTimeDuration;
    private transient Period currentMaxYearMonthDuration;
    private transient OffsetDateTime currentMaxTime;
    private transient byte activeType = 0;
    private transient ItemType returnType;
    private transient Item result;
    private ItemComparator comparator;


    public MaxFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
        this.iterator = this.children.get(0);
        this.comparator = new ItemComparator(
                false,
                new InvalidArgumentTypeException(
                        "Max expression input error. Input has to be non-null atomics of matching types",
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
        Duration candidateDuration;
        Duration currentMaxYearMonthDurationVar;
        Period candidatePeriod;
        Period currentMaxYearMonthPeriodVar;


        this.currentMinIsNullItem = false;
        this.currentMaxDouble = 0;
        this.currentMaxFloat = 0;
        this.currentMaxDecimal = null;
        this.currentMaxLong = 0;
        this.currentMaxURI = null;
        this.currentMaxString = null;
        this.currentMaxBoolean = false;
        this.hasTimeZone = false;
        this.currentMaxDate = null;
        this.currentMaxDateTime = null;
        this.currentMaxDayTimeDuration = null;
        this.currentMaxYearMonthDuration = null;
        this.currentMaxTime = null;
        this.activeType = 0;
        if (!this.iterator.isRDDOrDataFrame()) {
            this.iterator.open(context);
            Item candidateItem = null;
            ItemType candidateType = null;
            Instant now = null;
            while (this.iterator.hasNext()) {
                candidateItem = this.iterator.next();
                if (candidateItem.isNull()) {
                    this.currentMinIsNullItem = true;
                    continue;
                }
                candidateType = candidateItem.getDynamicType();
                switch (this.activeType) {
                    case 0:
                        if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.longItem)) {
                            this.activeType = 1;
                            this.currentMaxLong = candidateItem.castToDecimalValue().longValue();
                        } else if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.decimalItem)) {
                            this.activeType = 2;
                            this.currentMaxDecimal = candidateItem.castToDecimalValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.floatItem)) {
                            this.activeType = 3;
                            this.currentMaxFloat = candidateItem.castToFloatValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.doubleItem)) {
                            this.activeType = 4;
                            this.currentMaxDouble = candidateItem.castToDoubleValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.anyURIItem)) {
                            this.activeType = 5;
                            this.currentMaxURI = candidateItem.getStringValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.stringItem)) {
                            this.activeType = 6;
                            this.currentMaxString = candidateItem.getStringValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.booleanItem)) {
                            this.activeType = 7;
                            this.currentMaxBoolean = candidateItem.getBooleanValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.dateItem)) {
                            this.activeType = 8;
                            this.currentMaxDate = candidateItem.getDateTimeValue();
                            if (candidateItem.hasTimeZone()) {
                                this.hasTimeZone = true;
                            }
                        } else if (candidateType.isSubtypeOf(BuiltinTypesCatalogue.dateTimeItem)) {
                            this.activeType = 9;
                            this.currentMaxDateTime = candidateItem.getDateTimeValue();
                            if (candidateItem.hasTimeZone()) {
                                this.hasTimeZone = true;
                            }
                        } else if (candidateType.equals(BuiltinTypesCatalogue.dayTimeDurationItem)) {
                            this.activeType = 10;
                            this.currentMaxDayTimeDuration = candidateItem.getDurationValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                            this.activeType = 11;
                            this.currentMaxYearMonthDuration = candidateItem.getPeriodValue();
                        } else if (candidateType.equals(BuiltinTypesCatalogue.timeItem)) {
                            this.activeType = 12;
                            this.currentMaxTime = candidateItem.getDateTimeValue();
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
                            if (candidateItemLong > this.currentMaxLong) {
                                this.currentMaxLong = candidateItemLong;
                                this.returnType = candidateType;
                            }
                        }
                        if (candidateItem.isDecimal()) {
                            this.activeType = 1;
                            this.currentMaxDecimal = new BigDecimal(this.currentMaxLong);
                            BigDecimal candidateItemDecimal = candidateItem.getDecimalValue();
                            if (candidateItemDecimal.compareTo(this.currentMaxDecimal) > 0) {
                                this.currentMaxDecimal = candidateItemDecimal;
                                this.returnType = candidateType;
                                this.activeType = 2;
                            }
                        } else if (candidateItem.isFloat()) {
                            this.activeType = 3;
                            this.returnType = BuiltinTypesCatalogue.floatItem;
                            this.currentMaxFloat = this.currentMaxLong;
                            float candidateItemFloat = candidateItem.getFloatValue();
                            if (Float.isNaN(candidateItemFloat)) {
                                this.currentMaxFloat = Float.NaN;
                            } else if (candidateItemFloat > this.currentMaxFloat) {
                                this.currentMaxFloat = candidateItemFloat;
                            }
                        } else if (candidateItem.isDouble()) {
                            this.activeType = 4;
                            this.returnType = BuiltinTypesCatalogue.doubleItem;
                            this.currentMaxDouble = this.currentMaxLong;
                            double candidateItemDouble = candidateItem.getDoubleValue();
                            if (Double.isNaN(candidateItemDouble)) {
                                this.currentMaxDouble = Double.NaN;
                            } else if (candidateItemDouble > this.currentMaxDouble) {
                                this.currentMaxDouble = candidateItemDouble;
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
                            this.currentMaxFloat = this.currentMaxDecimal.floatValue();
                            float candidateItemFloat = candidateItem.getFloatValue();
                            if (Float.isNaN(candidateItemFloat)) {
                                this.currentMaxFloat = Float.NaN;
                            } else if (candidateItemFloat > this.currentMaxFloat) {
                                this.currentMaxFloat = candidateItemFloat;
                            }
                        } else if (candidateItem.isDouble()) {
                            this.activeType = 4;
                            this.returnType = BuiltinTypesCatalogue.doubleItem;
                            this.currentMaxDouble = this.currentMaxDecimal.doubleValue();
                            double candidateItemDouble = candidateItem.getDoubleValue();
                            if (Double.isNaN(candidateItemDouble)) {
                                this.currentMaxDouble = Double.NaN;
                            } else if (candidateItemDouble > this.currentMaxDouble) {
                                this.currentMaxDouble = candidateItemDouble;
                            }
                        } else {
                            BigDecimal candidateItemDecimal = candidateItem.castToDecimalValue();
                            if (candidateItemDecimal.compareTo(this.currentMaxDecimal) > 0) {
                                this.currentMaxDecimal = candidateItemDecimal;
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
                            this.currentMaxDouble = this.currentMaxFloat;
                            double candidateItemDouble = candidateItem.getDoubleValue();
                            if (Double.isNaN(candidateItemDouble) || candidateItemDouble > this.currentMaxDouble) {
                                this.currentMaxDouble = candidateItemDouble;
                            }
                        } else {
                            if (!Float.isNaN(this.currentMaxFloat)) {
                                float candidateItemFloat = candidateItem.castToFloatValue();
                                if (candidateItemFloat > this.currentMaxFloat) {
                                    this.currentMaxFloat = candidateItemFloat;
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
                        if (!Double.isNaN(this.currentMaxDouble)) {
                            double candidateItemDouble = candidateItem.castToDoubleValue();
                            if (candidateItemDouble > this.currentMaxDouble) {
                                this.currentMaxDouble = candidateItemDouble;
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
                            this.currentMaxString = this.currentMaxURI;
                            if (candidateItem.getStringValue().compareTo(this.currentMaxURI) > 0) {
                                this.currentMaxString = candidateItem.getStringValue();
                            }
                        } else if (candidateItem.getStringValue().compareTo(this.currentMaxURI) > 0) {
                            this.currentMaxURI = candidateItem.getStringValue();

                        }
                        break;
                    case 6:
                        if (!candidateItem.isString() && !candidateItem.isAnyURI()) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare string with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getStringValue().compareTo(this.currentMaxString) > 0) {
                            this.currentMaxString = candidateItem.getStringValue();
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
                        if (Boolean.compare(candidateItem.getBooleanValue(), this.currentMaxBoolean) > 0) {
                            this.currentMaxBoolean = candidateItem.getBooleanValue();
                        }
                        break;
                    case 8:
                        if (!candidateType.equals(BuiltinTypesCatalogue.dateItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getDateTimeValue().compareTo(this.currentMaxDate) > 0) {
                            this.currentMaxDate = candidateItem.getDateTimeValue();
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
                        if (candidateItem.getDateTimeValue().compareTo(this.currentMaxDateTime) > 0) {
                            this.currentMaxDateTime = candidateItem.getDateTimeValue();
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
                        candidatePeriod = candidateItem.getPeriodValue();
                        currentMaxYearMonthPeriodVar = this.currentMaxYearMonthDuration;
                        if (DurationItem.periodComparator.compare(currentMaxYearMonthPeriodVar, candidatePeriod) > 0) {
                            this.currentMaxYearMonthDuration = Period.from(candidatePeriod);
                        }
                        break;
                    case 11:
                        if (!candidateType.equals(BuiltinTypesCatalogue.yearMonthDurationItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        candidatePeriod = candidateItem.getPeriodValue();
                        currentMaxYearMonthPeriodVar = this.currentMaxYearMonthDuration;
                        if (DurationItem.periodComparator.compare(currentMaxYearMonthPeriodVar, candidatePeriod) > 0) {
                            this.currentMaxYearMonthDuration = Period.from(candidatePeriod);
                        }
                        break;
                    case 12:
                        if (!candidateType.equals(BuiltinTypesCatalogue.timeItem)) {
                            throw new InvalidArgumentTypeException(
                                    "Cannot compare " + this.returnType + " with " + candidateType,
                                    getMetadata()
                            );
                        }
                        if (candidateItem.getDateTimeValue().compareTo(this.currentMaxTime) > 0) {
                            this.currentMaxTime = candidateItem.getDateTimeValue();
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
                    if (this.currentMinIsNullItem) {
                        return ItemFactory.getInstance().createNullItem();
                    }
                    return null;
                case 1:
                    itemResult = ItemFactory.getInstance().createLongItem(this.currentMaxLong);
                    break;
                case 2:
                    itemResult = ItemFactory.getInstance().createDecimalItem(this.currentMaxDecimal);
                    break;
                case 3:
                    itemResult = ItemFactory.getInstance().createFloatItem(this.currentMaxFloat);
                    break;
                case 4:
                    itemResult = ItemFactory.getInstance().createDoubleItem(this.currentMaxDouble);
                    break;
                case 5:
                    itemResult = ItemFactory.getInstance().createAnyURIItem(this.currentMaxURI);
                    break;
                case 6:
                    itemResult = ItemFactory.getInstance().createStringItem(this.currentMaxString);
                    break;
                case 7:
                    itemResult = ItemFactory.getInstance().createBooleanItem(this.currentMaxBoolean);
                    break;
                case 8:
                    itemResult = ItemFactory.getInstance().createDateItem(this.currentMaxDate, this.hasTimeZone);
                    break;
                case 9:
                    itemResult = ItemFactory.getInstance().createDateTimeItem(this.currentMaxDateTime, this.hasTimeZone);
                    break;
                case 10:
                    itemResult = ItemFactory.getInstance().createDayTimeDurationItem(this.currentMaxDayTimeDuration);
                    break;
                case 11:
                    itemResult = ItemFactory.getInstance().createYearMonthDurationItem(this.currentMaxYearMonthDuration);
                    break;
                case 12:
                    itemResult = ItemFactory.getInstance().createTimeItem(this.currentMaxTime.toOffsetTime(), this.hasTimeZone);
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
            ItemType maxType;
            if (
                df.getItemType().isObjectItemType()
                    && df.getItemType().getObjectContentFacet().containsKey("tableLocation")
            ) {
                maxType = df.getItemType()
                    .getObjectContentFacet()
                    .get(SparkSessionManager.atomicJSONiqItemColumnName)
                    .getType();
            } else {
                maxType = df.getItemType();
            }
            String input = FlworDataFrameUtils.createTempView(df.getDataFrame());
            JSoundDataFrame maxDF = df.evaluateSQL(
                String.format(
                    "SELECT MAX(`%s`) as `%s` FROM %s",
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    SparkSessionManager.atomicJSONiqItemColumnName,
                    input
                ),
                maxType
            );
            return itemTypePromotion(maxDF.getExactlyOneItem());
        }

        JavaRDD<Item> rdd = this.iterator.getRDD(context);
        if (rdd.isEmpty()) {
            return null;
        }
        this.result = rdd.max(this.comparator);
        return this.result;

    }

    public Map<Name, DynamicContext.VariableDependency> getVariableDependencies() {
        if (this.children.get(0) instanceof VariableReferenceIterator) {
            VariableReferenceIterator expr = (VariableReferenceIterator) this.children.get(0);
            Map<Name, DynamicContext.VariableDependency> result =
                new TreeMap<Name, DynamicContext.VariableDependency>();
            result.put(expr.getVariableName(), DynamicContext.VariableDependency.MAX);
            return result;
        } else {
            return super.getVariableDependencies();
        }
    }

    private Item itemTypePromotion(Item item) {
        if (item.isAnyURI()) {
            return ItemFactory.getInstance().createStringItem(item.getStringValue());
        }
        if (item.isFloat()) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        if (item.isDecimal()) {
            return ItemFactory.getInstance().createDoubleItem(item.castToDoubleValue());
        }
        return item;
    }
}
