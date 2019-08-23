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
 * Authors: Stefan Irimescu, Can Berker Cikis
 *
 */

package org.rumbledb.api;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.DecimalItem;
import sparksoniq.jsoniq.item.DoubleItem;
import sparksoniq.jsoniq.item.IntegerItem;
import sparksoniq.jsoniq.item.base.SerializableItem;
import sparksoniq.semantics.types.ItemType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * An instance of this class is an item in the JSONiq data model.
 * JSONiq manipulates sequences of items.
 */
public abstract class Item implements SerializableItem {

	private static final long serialVersionUID = 1L;

	protected Item() {
    }

	/**
	 * @deprecated use isNumber()
	 */
    public boolean isNumeric() {
        return this.isInteger() || this.isDecimal() || this.isDouble();
    }

    //performs conversions for binary operations with a numeric return type
    //(int,double) -> double
    //(int,decimal) -> decimal
    //(decimal,double) -> double
    public static Type getNumericResultType(Item left, Item right) {
        if (left.isDouble() || right.isDouble()) {
            return DoubleItem.class;
        }
        if (left.isDecimal() || right.isDecimal()) {
            return DecimalItem.class;
        }
        return IntegerItem.class;
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
        throw new IteratorFlowException("Cannot call castToDouble on non numeric");
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
     * @return -1 if this < other; 0 if this == other; 1 if this > other;
     */
    public int compareTo(Item other) {
        int result;
        if (this.isNumeric() && other.isNumeric()) {
            BigDecimal value1 = this.castToDecimalValue();
            BigDecimal value2 = other.castToDecimalValue();
            result = value1.compareTo(value2);
        } else if (this.isBoolean() && other.isBoolean()) {
            Boolean value1 = this.getBooleanValue();
            Boolean value2 = other.getBooleanValue();
            result = value1.compareTo(value2);
        } else if (this.isString() && other.isString()) {
            String value1 = this.getStringValue();
            String value2 = other.getStringValue();
            result = value1.compareTo(value2);
        } else if (this.isNull() || other.isNull()) {
            // null equals null
            if (this.isNull() && other.isNull()) {
                result = 0;
            } else if (this.isNull()) {
                result = -1;
            } else {
                result = 1;
            }
        } else {
            result = this.serialize().compareTo(other.serialize());
        }
        return result;
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
    public Collection<? extends Item> getValues() {
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
     * @param item a value.
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
	 * Tests whether the item is an array.
	 * 
	 * @return true if it is an array, false otherwise.
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
	 * Tests whether the item is a number (integer, decimal or double).
	 * 
	 * @return true if it is a number, false otherwise.
	 */
    public boolean isNumber() {
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
    public abstract boolean equals(Object otherItem);

    /**
	 * Computes a hash code.
	 * 
	 * @return a hash code as an int.
	 */
    public abstract int hashCode();
}
