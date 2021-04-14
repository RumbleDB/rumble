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

package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.comparison.ComparisonExpression.ComparisonOperator;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;

public class NullItem implements Item {


    private static final long serialVersionUID = 1L;

    public NullItem() {
        super();
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof Item) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return false;
    }

    @Override
    public String serialize() {
        return "null";
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObjectOrNull(output, null, Item.class);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        kryo.readObjectOrNull(input, Item.class);

    }

    public int hashCode() {
        return 0;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.nullItem;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isFunction() {
        return false;
    }

    @Override
    public boolean isBoolean() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isDecimal() {
        return false;
    }

    @Override
    public boolean isInteger() {
        return false;
    }

    @Override
    public boolean isFloat() {
        return false;
    }

    @Override
    public boolean isInt() {
        return false;
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public boolean isDuration() {
        return false;
    }

    @Override
    public boolean isYearMonthDuration() {
        return false;
    }

    @Override
    public boolean isDayTimeDuration() {
        return false;
    }

    @Override
    public boolean isDateTime() {
        return false;
    }

    @Override
    public boolean isDate() {
        return false;
    }

    @Override
    public boolean isTime() {
        return false;
    }

    @Override
    public boolean isAnyURI() {
        return false;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isHexBinary() {
        return false;
    }

    @Override
    public boolean isBase64Binary() {
        return false;
    }

    @Override
    public List<Item> getItems() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public Item getItemAt(int position) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public List<String> getKeys() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public List<Item> getValues() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public Item getItemByKey(String key) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public int getSize() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public boolean getBooleanValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public double getDoubleValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public float getFloatValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public int getIntValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public BigInteger getIntegerValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public BigDecimal getDecimalValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public Period getDurationValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public DateTime getDateTimeValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public byte[] getBinaryValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public FunctionIdentifier getIdentifier() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public List<Name> getParameterNames() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public FunctionSignature getSignature() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public boolean hasTimeZone() {
        return false;
    }

    @Override
    public boolean hasDateTime() {
        return false;
    }

    @Override
    public void putItem(Item item) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public void append(Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public void putItemByKey(String key, Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public RuntimeIterator getBodyIterator() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public Map<Name, Dataset<Row>> getDFVariablesInClosure() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public DynamicContext getModuleDynamicContext() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public boolean isString() {
        return false;
    }

    @Override
    public String getStringValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public double castToDoubleValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public float castToFloatValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public BigDecimal castToDecimalValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public BigInteger castToIntegerValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }

    @Override
    public int castToIntValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is a null!");
    }
}
