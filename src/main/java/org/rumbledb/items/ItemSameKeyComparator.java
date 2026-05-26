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
 * Authors: Ghislain Fourny
 *
 */

package org.rumbledb.items;

import java.util.Arrays;
import java.util.Comparator;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.types.BuiltinTypesCatalogue;

/**
 * This class implements the same key comparison as defined in XQuery & XPath Functions 3.1, section 17.1.1.
 * It is used for map lookups.
 */
public class ItemSameKeyComparator implements Comparator<Item> {

    @Override
    public int compare(Item o1, Item o2) {
        CATEGORY category1 = getCategory(o1);
        CATEGORY category2 = getCategory(o2);
        if (category1 == category2) {
            switch (category1) {
                case STRING:
                    return o1.getStringValue().compareTo(o2.getStringValue());
                case NAN:
                    return 0;
                case POSITIVE_INFINITY:
                    return 0;
                case NEGATIVE_INFINITY:
                    return 0;
                case DECIMAL_DOUBLE_FLOAT:
                    return Double.compare(o1.getDoubleValue(), o2.getDoubleValue());
                case DATE_WITH_TIMEZONE:
                    return o1.getDateTimeValue().compareTo(o2.getDateTimeValue());
                case DATE_WITHOUT_TIMEZONE:
                    return o1.getDateTimeValue().compareTo(o2.getDateTimeValue());
                case TIME_WITH_TIMEZONE:
                    return o1.getDateTimeValue().compareTo(o2.getDateTimeValue());
                case DATETIME_WITH_TIMEZONE:
                    return o1.getDateTimeValue().compareTo(o2.getDateTimeValue());
                case DATETIME_WITHOUT_TIMEZONE:
                    return o1.getDateTimeValue().compareTo(o2.getDateTimeValue());
                case GYEAR_WITH_TIMEZONE:
                    return Integer.compare(o1.getYear(), o2.getYear());
                case GYEAR_WITHOUT_TIMEZONE:
                    return Integer.compare(o1.getYear(), o2.getYear());
                case GMONTH_WITH_TIMEZONE:
                    return Integer.compare(o1.getMonth(), o2.getMonth());
                case GMONTH_WITHOUT_TIMEZONE:
                    return Integer.compare(o1.getMonth(), o2.getMonth());
                case GDAY_WITH_TIMEZONE:
                    return Integer.compare(o1.getDay(), o2.getDay());
                case GDAY_WITHOUT_TIMEZONE:
                    return Integer.compare(o1.getDay(), o2.getDay());
                case GYEARMONTH_WITH_TIMEZONE:
                    return Integer.compare(o1.getYear() * 12 + o1.getMonth(), o2.getYear() * 12 + o2.getMonth());
                case GYEARMONTH_WITHOUT_TIMEZONE:
                    return Integer.compare(o1.getYear() * 12 + o1.getMonth(), o2.getYear() * 12 + o2.getMonth());
                case GMONTHDAY_WITH_TIMEZONE:
                    return Integer.compare(o1.getMonth() * 31 + o1.getDay(), o2.getMonth() * 31 + o2.getDay());
                case GMONTHDAY_WITHOUT_TIMEZONE:
                    return Integer.compare(o1.getMonth() * 31 + o1.getDay(), o2.getMonth() * 31 + o2.getDay());
                case BOOLEAN:
                    return Boolean.compare(o1.getBooleanValue(), o2.getBooleanValue());
                case BINARY:
                    return Arrays.compare(o1.getBinaryValue(), o2.getBinaryValue());
                case DURATION:
                    return o1.getDurationValue().compareTo(o2.getDurationValue());
                case QNAME:
                    return o1.getQNameValue().compareTo(o2.getQNameValue());
                case NOTATION:
                    return o1.getStringValue().compareTo(o2.getStringValue());
            }
        }
        return category1.ordinal() < category2.ordinal() ? -1 : 1;
    }

    enum CATEGORY {
        STRING,
        NAN,
        POSITIVE_INFINITY,
        NEGATIVE_INFINITY,
        DECIMAL_DOUBLE_FLOAT,
        DATE_WITH_TIMEZONE,
        DATE_WITHOUT_TIMEZONE,
        TIME_WITH_TIMEZONE,
        DATETIME_WITH_TIMEZONE,
        DATETIME_WITHOUT_TIMEZONE,
        GYEAR_WITH_TIMEZONE,
        GYEAR_WITHOUT_TIMEZONE,
        GMONTH_WITH_TIMEZONE,
        GMONTH_WITHOUT_TIMEZONE,
        GDAY_WITH_TIMEZONE,
        GDAY_WITHOUT_TIMEZONE,
        GYEARMONTH_WITH_TIMEZONE,
        GYEARMONTH_WITHOUT_TIMEZONE,
        GMONTHDAY_WITH_TIMEZONE,
        GMONTHDAY_WITHOUT_TIMEZONE,
        BOOLEAN,
        BINARY,
        DURATION,
        QNAME,
        NOTATION
    }

    public static CATEGORY getCategory(Item o1) {
        if (o1.isString() || o1.isAnyURI() || o1.isUntypedAtomic()) {
            return CATEGORY.STRING;
        } else if (
            (o1.isDouble() && Double.isNaN(o1.getDoubleValue())) || (o1.isFloat() && Float.isNaN(o1.getFloatValue()))
        ) {
            return CATEGORY.NAN;
        } else if (
            (o1.isDouble() && o1.getDoubleValue() == Double.POSITIVE_INFINITY)
                || (o1.isFloat() && o1.getFloatValue() == Float.POSITIVE_INFINITY)
        ) {
            return CATEGORY.POSITIVE_INFINITY;
        } else if (
            (o1.isDouble() && o1.getDoubleValue() == Double.NEGATIVE_INFINITY)
                || (o1.isFloat() && o1.getFloatValue() == Float.NEGATIVE_INFINITY)
        ) {
            return CATEGORY.NEGATIVE_INFINITY;
        } else if (o1.isDecimal() || o1.isDouble() || o1.isFloat()) {
            return CATEGORY.DECIMAL_DOUBLE_FLOAT;
        } else if (o1.isDate() && o1.hasTimeZone()) {
            return CATEGORY.DATE_WITH_TIMEZONE;
        } else if (o1.isDate()) {
            return CATEGORY.DATE_WITHOUT_TIMEZONE;
        } else if (o1.isTime() && o1.hasTimeZone()) {
            return CATEGORY.TIME_WITH_TIMEZONE;
        } else if (o1.isDateTime() && o1.hasTimeZone()) {
            return CATEGORY.DATETIME_WITH_TIMEZONE;
        } else if (o1.isDateTime()) {
            return CATEGORY.DATETIME_WITHOUT_TIMEZONE;
        } else if (o1.isGYear() && o1.hasTimeZone()) {
            return CATEGORY.GYEAR_WITH_TIMEZONE;
        } else if (o1.isGYear()) {
            return CATEGORY.GYEAR_WITHOUT_TIMEZONE;
        } else if (o1.isGMonth() && o1.hasTimeZone()) {
            return CATEGORY.GMONTH_WITH_TIMEZONE;
        } else if (o1.isGMonth()) {
            return CATEGORY.GMONTH_WITHOUT_TIMEZONE;
        } else if (o1.isGDay() && o1.hasTimeZone()) {
            return CATEGORY.GDAY_WITH_TIMEZONE;
        } else if (o1.isGDay()) {
            return CATEGORY.GDAY_WITHOUT_TIMEZONE;
        } else if (o1.isGYearMonth() && o1.hasTimeZone()) {
            return CATEGORY.GYEARMONTH_WITH_TIMEZONE;
        } else if (o1.isGYearMonth()) {
            return CATEGORY.GYEARMONTH_WITHOUT_TIMEZONE;
        } else if (o1.isGMonthDay() && o1.hasTimeZone()) {
            return CATEGORY.GMONTHDAY_WITH_TIMEZONE;
        } else if (o1.isGMonthDay()) {
            return CATEGORY.GMONTHDAY_WITHOUT_TIMEZONE;
        } else if (o1.isBoolean()) {
            return CATEGORY.BOOLEAN;
        } else if (o1.isBinary()) {
            return CATEGORY.BINARY;
        } else if (o1.isDuration()) {
            return CATEGORY.DURATION;
        } else if (o1.isQName()) {
            return CATEGORY.QNAME;
        } else if (o1.getDynamicType().getPrimitiveType().equals(BuiltinTypesCatalogue.NOTATIONItem)) {
            return CATEGORY.NOTATION;
        } else {
            throw new OurBadException(
                    "Unexpected item type for same key comparison: " + o1.getDynamicType().toString()
            );
        }
    }
}
