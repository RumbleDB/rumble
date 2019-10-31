/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

package org.rumbledb.api;

import org.joda.time.Period;
import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.compiler.translator.expr.operational.base.OperationalExpressionBase;
import sparksoniq.jsoniq.runtime.metadata.IteratorMetadata;
import sparksoniq.semantics.types.ItemType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * An instance of this class is an item in the JSONiq data model.
 * 
 * JSONiq manipulates sequences of items.
 * 
 * An item can be structured or atomic.
 * 
 * Structured items include objects and arrays. Objects are mappings from strings (keys) to items. Arrays are ordered lists of items.
 * 
 * Atomic items have a lexical value and a type. Currently, Rumble only supports strings, numbers, booleans and null.
 * 
 * Numbers can be decimals, integers or doubles.
 * 
 * This class provides methods to identify what kind of item the instance is, and to access its properties.
 *
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public abstract class Item implements SerializableItem {

    private static final long serialVersionUID = 1L;

    /**
     * Please do not use. Items are produced by a JSONiq query via the Rumble API.
     */
    protected Item() {
    }

    /**
     * Tests whether the item is a number (integer, decimal or double).
     *
     * @return true if it is a number, false otherwise.
     */
    public boolean isNumeric() {
        return this.isInteger() || this.isDecimal() || this.isDouble();
    }

    /**
     * Casts the item to a double value.
     * @return the double value.
     */
    public double castToDoubleValue() {
        throw new IteratorFlowException("Cannot call castToDouble on non numeric");
    }

    /**
     * Casts the item to a decimal value.
     * @return the BigDecimal value.
     */
    public BigDecimal castToDecimalValue() {
        throw new IteratorFlowException("Cannot call castToDecimal on non numeric");
    }

    /**
     * Casts the item to an integer value.
     * @return the int value.
     */
    public int castToIntegerValue() {
        throw new IteratorFlowException("Cannot call castToDouble on non numeric");
    }

    /**
     * Returns the effective boolean value of the item, if atomic.
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
        if (other.isNull()) return 1;
        return this.serialize().compareTo(other.serialize());
    }

    /**
     * Function that compare two items according to the operator defined for the comparison.
     * @param other another Item
     * @param operator the operator used for the comparison
     * @param metadata Metadata useful for throwing exceptions
     * @return BooleanItem result of the comparison
     */
    public Item compareItem(Item other, OperationalExpressionBase.Operator operator, IteratorMetadata metadata) {
        return operator.apply(this, other);
    }

    /**
     * Returns the members of the item if it is an array.
     *
     * @return the list of the array members.
     */
    public List<Item> getItems() {
        throw new RuntimeException("Item is not an array.");
    }

    /**
     * Returns the member of the item at the specified position if it is an array.
     *
     * @param position a position.
     * @return the member at position position.
     */
    public Item getItemAt(int position) {
        throw new RuntimeException("Item is not an array.");
    }

    /**
     * Appends an item, if it is an array.
     *
     * @param item an item.
     */
    public void putItem(Item item) {
        throw new RuntimeException("Item is not an array.");
    }

    /**
     * Returns the keys of the item, if it is an object item.
     *
     * @return the list of the keys.
     */
    public List<String> getKeys() {
        throw new RuntimeException("Item is not an object.");
    }

    /**
     * Returns the values of the item, if it is an object item.
     *
     * @return the list of the value items.
     */
    public List<Item> getValues() {
        throw new RuntimeException("Item is not an object.");
    }

    /**
     * Returns the value associated with a specific key, if it is an object item.
     *
     * @param key a key.
     * @return the value associated with key.
     */
    public Item getItemByKey(String key) {
        throw new RuntimeException("Item is not an object.");
    }

    /**
     * Adds a key-value pair, if it is an object item.
     *
     * @param key a key.
     * @param value a value.
     */
    public void putItemByKey(String key, Item value) {
        throw new RuntimeException("Item is not an object.");
    }

    /**
     * Returns the size of the item, if it is an array item.
     *
     * @return the size as an int.
     */
    public int getSize() {
        throw new RuntimeException("Item is not an array.");
    }

    /**
     * Returns the string value of the item, if it is a atomic item of type string.
     *
     * @return the string value.
     */
    public String getStringValue() {
        throw new RuntimeException("Item is not a string.");
    }

    /**
     * Returns the boolean value of the item, if it is a atomic item of type boolean.
     *
     * @return the boolean value.
     */
    public boolean getBooleanValue() {
        throw new RuntimeException("Item is not a boolean.");
    }

    /**
     * Returns the double value of the item, if it is a atomic item of type double.
     *
     * @return the double value.
     */
    public double getDoubleValue() {
        throw new RuntimeException("Item is not a double.");
    }

    /**
     * Returns the integer value of the item, if it is a atomic item of type integer.
     *
     * @return the integer value as an int.
     */
    public int getIntegerValue() {
        throw new RuntimeException("Item is not an integer.");
    }

    /**
     * Returns the decimal value of the item, if it is a atomic item of type decimal.
     *
     * @return the decimal value as a BigDecimal.
     */
    public BigDecimal getDecimalValue() {
        throw new RuntimeException("Item is not a big decimal.");
    }

    /**
     * Returns the period value of the item, if it is a atomic item of type duration.
     *
     * @return the period value as a Period.
     */
    public Period getDurationValue() {
        throw new RuntimeException("Item is not a duration.");
    }

    /**
     * Returns the byte[] value of the item, if it is a atomic item of type hexBinary or Base64Binary.
     *
     * @return the binary value as an array of bytes.
     */
    public byte[] getBinaryValue() {
        throw new RuntimeException("Item is not a hexBinary.");
    }

    /**
     * Please do not use, item type API not publicly released yet.
     * @param type an ItemType.
     * @return true if it matches the item type.
     */
    public abstract boolean isTypeOf(ItemType type);

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
     * Tests whether the item is an atomic item of type double.
     * 
     * @return true if it is an atomic item of type double, false otherwise.
     */
    public boolean isDouble() {
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

    private void readObject(ObjectInputStream aInputStream)
            throws ClassNotFoundException, IOException {
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


    public Item add(Item other) { throw new UnsupportedOperationException("Operation not defined"); }
    public Item subtract(Item other) { throw new UnsupportedOperationException("Operation not defined"); }
    public Item multiply(Item other) { throw new UnsupportedOperationException("Operation not defined"); }
    public Item divide(Item other) { throw new UnsupportedOperationException("Operation not defined"); }
    public Item modulo(Item other) { throw new UnsupportedOperationException("Operation not defined"); }
    public Item idivide(Item other) { throw new UnsupportedOperationException("Operation not defined"); }
}
