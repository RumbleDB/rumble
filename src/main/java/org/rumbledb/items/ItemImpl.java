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
 * Authors: Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.items;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.IteratorFlowException;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

abstract class ItemImpl implements Item {

    private static final long serialVersionUID = 1L;

    /**
     * Please do not use. Items are produced by a JSONiq query via the Rumble API.
     */
    protected ItemImpl() {
    }

    /**
     * Tests whether the item is a number (integer, decimal or double).
     *
     * @return true if it is a number, false otherwise.
     */
    public boolean isNumeric() {
        return false;
    }

    public double castToDoubleValue() {
        throw new IteratorFlowException("Cannot call castToDouble on non numeric");
    }

    public float castToFloatValue() {
        throw new IteratorFlowException("Cannot call castToFloat on non numeric");
    }

    /**
     * Casts the item to a decimal value.
     *
     * @return the BigDecimal value.
     */
    public BigDecimal castToDecimalValue() {
        throw new IteratorFlowException("Cannot call castToDecimal on non numeric");
    }

    /**
     * Casts the item to a big integer value.
     *
     * @return the BigInteger value.
     */
    public BigInteger castToIntegerValue() {
        throw new IteratorFlowException("Cannot call castToInteger on non numeric");
    }

    /**
     * Casts the item to an integer value.
     *
     * @return the int value.
     */
    public int castToIntValue() {
        throw new IteratorFlowException("Cannot call castToInt on non numeric");
    }

    /**
     * Returns the effective boolean value of the item, if atomic.
     *
     * @return the effective boolean value.
     */
    public abstract boolean getEffectiveBooleanValue();

    /**
     * Function that compares 2 items.
     * Non-atomics can't be compared.
     * Items have to be of the same type or one them has to be null.
     *
     * @param other another item.
     * @return -1 if this &lt; other; 0 if this == other; 1 if this &gt; other;
     */
    public int compareTo(Item other) {
        if (other.isNull()) {
            return 1;
        }
        return this.serialize().compareTo(other.serialize());
    }

    /**
     * Returns the members of the item if it is an array.
     *
     * @return the list of the array members.
     */
    public List<Item> getItems() {
        throw new OurBadException("Item '" + this.serialize() + "' is not an array.");
    }

    /**
     * Returns the member of the item at the specified position if it is an array.
     *
     * @param position a position.
     * @return the member at position position.
     */
    public Item getItemAt(int position) {
        throw new OurBadException("Item '" + this.serialize() + "' is not an array.");
    }

    /**
     * Appends an item, if it is an array.
     *
     * @param item an item.
     */
    public void putItem(Item item) {
        throw new OurBadException("Item '" + this.serialize() + "' is not an array.");
    }

    /**
     * Returns the keys of the item, if it is an object item.
     *
     * @return the list of the keys.
     */
    public List<String> getKeys() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an object.");
    }

    /**
     * Returns the values of the item, if it is an object item.
     *
     * @return the list of the value items.
     */
    public List<Item> getValues() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an object.");
    }

    /**
     * Returns the value associated with a specific key, if it is an object item.
     *
     * @param key a key.
     * @return the value associated with key.
     */
    public Item getItemByKey(String key) {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an object.");
    }

    /**
     * Adds a value pair, if it is an array item.
     *
     * @param value a value.
     */
    public void append(Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an array.");
    }

    /**
     * Adds a key-value pair, if it is an object item.
     *
     * @param key a key.
     * @param value a value.
     */
    public void putItemByKey(String key, Item value) {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an object.");
    }

    /**
     * Returns the size of the item, if it is an array item.
     *
     * @return the size as an int.
     */
    public int getSize() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an array.");
    }

    /**
     * Returns the string value of the item, if it is a atomic item of type string.
     *
     * @return the string value.
     */
    public String getStringValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a string.");
    }

    /**
     * Returns the boolean value of the item, if it is a atomic item of type boolean.
     *
     * @return the boolean value.
     */
    public boolean getBooleanValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a boolean.");
    }

    public double getDoubleValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a double.");
    }

    public float getFloatValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a float.");
    }

    /**
     * Returns the integer value of the item, if it is a atomic item of type integer within the required range.
     *
     * @return the integer value as an int.
     */
    public int getIntValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an int.");
    }

    /**
     * Returns the integer value of the item as a bit integer, if it is a atomic item of type integer.
     *
     * @return the integer value as a BigInteger.
     */
    public BigInteger getIntegerValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not an integer.");
    }

    /**
     * Returns the decimal value of the item, if it is a atomic item of type decimal.
     *
     * @return the decimal value as a BigDecimal.
     */
    public BigDecimal getDecimalValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a big decimal.");
    }

    /**
     * Returns the period value of the item, if it is a atomic item of type duration.
     *
     * @return the period value as a Period.
     */
    public Period getDurationValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a duration.");
    }

    /**
     * Returns the dateTime value of the item, if it is a atomic item of type dateTimeItem or dateItem or timeItem.
     *
     * @return the dateTime value as a DateTime.
     */
    public DateTime getDateTimeValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' does not have a DateTime.");
    }

    /**
     * Returns the byte[] value of the item, if it is a atomic item of type hexBinary or Base64Binary.
     *
     * @return the binary value as an array of bytes.
     */
    public byte[] getBinaryValue() {
        throw new OurBadException(" Item '" + this.serialize() + "' is not a hexBinary.");
    }

    /**
     * @return true if the Item has a timeZone, false otherwise
     */
    public boolean hasTimeZone() {
        return false;
    }


    public Item promoteTo(ItemType type) {
        return this;
    }

    /**
     * Tests whether the item is a function.
     *
     * @return true if it is a function, false otherwise
     */
    public boolean isFunction() {
        return false;
    }

    /**
     * Tests whether the item is an array.
     *
     * @return true if it is an array, false otherwise.
     */
    public boolean isArray() {
        return false;
    }

    /**
     * Tests whether the item is an object.
     *
     * @return true if it is an object, false otherwise.
     */
    public boolean isObject() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item.
     *
     * @return true if it is an atomic item, false otherwise.
     */
    public boolean isAtomic() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type string.
     *
     * @return true if it is an atomic item of type string, false otherwise.
     */
    public boolean isString() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type boolean.
     *
     * @return true if it is an atomic item of type boolean, false otherwise.
     */
    public boolean isBoolean() {
        return false;
    }

    /**
     * Tests whether the item is the null item.
     *
     * @return true if it is the null item, false otherwise.
     */
    public boolean isNull() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type integer.
     *
     * @return true if it is an atomic item of type integer, false otherwise.
     */
    public boolean isInteger() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type int.
     *
     * @return true if it is an atomic item of type int, false otherwise.
     */
    public boolean isInt() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

    public boolean isFloat() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type decimal.
     *
     * @return true if it is an atomic item of type decimal, false otherwise.
     */
    public boolean isDecimal() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type duration.
     *
     * @return true if it is an atomic item of type duration, false otherwise.
     */
    public boolean isDuration() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type yearMonthDuration.
     *
     * @return true if it is an atomic item of type yearMonthDuration, false otherwise.
     */
    public boolean isYearMonthDuration() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type dayTimeDuration.
     *
     * @return true if it is an atomic item of type dayTimeDuration, false otherwise.
     */
    public boolean isDayTimeDuration() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type dateTime.
     *
     * @return true if it is an atomic item of type dateTime, false otherwise.
     */
    public boolean isDateTime() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type date.
     *
     * @return true if it is an atomic item of type date, false otherwise.
     */
    public boolean isDate() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type time.
     *
     * @return true if it is an atomic item of type time, false otherwise.
     */
    public boolean isTime() {
        return false;
    }

    /**
     * Tests whether the item contains a representation of date or time (or both).
     *
     * @return true if it is an atomic item of type time, date or dateTime, false otherwise.
     */
    public boolean hasDateTime() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type anyURI.
     *
     * @return true if it is an atomic item of type anyURI, false otherwise.
     */
    public boolean isAnyURI() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type hexBinary.
     *
     * @return true if it is an atomic item of type hexBinary, false otherwise.
     */
    public boolean isHexBinary() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type base64Binary.
     *
     * @return true if it is an atomic item of type base64Binary, false otherwise.
     */
    public boolean isBase64Binary() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type base64Binary or hexBinary.
     *
     * @return true if it is an atomic item of type base64Binary or hexBinary, false otherwise.
     */
    public boolean isBinary() {
        return this.isHexBinary() || this.isBase64Binary();
    }

    private void readObject(ObjectInputStream aInputStream)
            throws ClassNotFoundException,
                IOException {
        aInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream)
            throws IOException {
        aOutputStream.defaultWriteObject();
    }

    /**
     * Tests for logical equality.
     *
     * @param other another item.
     * @return true it is equal to other, false otherwise.
     */
    @Override
    public abstract boolean equals(Object other);

    /**
     * Computes a hash code.
     *
     * @return a hash code as an int.
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns the dynamic type of the item (for error message purposes).
     * 
     * @return the dynamic type as an item type.
     */
    public ItemType getDynamicType() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public FunctionIdentifier getIdentifier() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public List<Name> getParameterNames() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public FunctionSignature getSignature() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public RuntimeIterator getBodyIterator() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public Map<Name, Dataset<Row>> getDFVariablesInClosure() {
        throw new UnsupportedOperationException("Operation not defined");
    }

    public DynamicContext getDynamicModuleContext() {
        throw new UnsupportedOperationException("Operation not defined");
    }
}
