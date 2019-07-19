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

package sparksoniq.jsoniq.item;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.base.SerializableItem;
import sparksoniq.semantics.types.ItemType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

//TODO serialize with indentation
public abstract class Item implements SerializableItem {
    protected Item() {
    }

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

    public <T> T getNumericValue(Class<T> type) {
        throw new IteratorFlowException("Cannot call getNumericValue on non numeric");
    }

    //returns an effective boolean value of any item type
    public boolean getEffectiveBooleanValue() {
        if (this.isBoolean())
            return this.getBooleanValue();
        else if (this.isNumeric()) {
            if (this.isInteger())
                return this.getIntegerValue() != 0;
            else if (this.isDouble())
                return this.getDoubleValue() != 0;
            else if (this.isDecimal())
                return !this.getDecimalValue().equals(0);
        } else if (this.isNull())
            return false;
        else if (this.isString())
            return !this.getStringValue().isEmpty();
        else if (this.isObject())
            return true;
        else if (this.isArray())
            return true;

        return true;
    }

    /**
     * Function that compares 2 items.
     * Non-atomics can't be compared.
     * Items have to be of the same type or one them has to be null.
     *
     * @return -1 if this < other; 0 if this == other; 1 if this > other;
     */
    public int compareTo(Item other) {
        int result;
        if (this.isNumeric() && other.isNumeric()) {
            BigDecimal value1 = this.getNumericValue(BigDecimal.class);
            BigDecimal value2 = other.getNumericValue(BigDecimal.class);
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

    public List<Item> getItems() {
        throw new RuntimeException("Item is not an array.");
    }

    public Item getItemAt(int i) {
        throw new RuntimeException("Item is not an array.");
    }

    public void putItem(Item value) {
        throw new RuntimeException("Item is not an array.");
    }

    public List<String> getKeys() {
        throw new RuntimeException("Item is not an object.");
    }

    public Collection<? extends Item> getValues() {
        throw new RuntimeException("Item is not an object.");
    }

    public Item getItemByKey(String s) {
        throw new RuntimeException("Item is not an object.");
    }

    public void putItemByKey(String s, Item value) {
        throw new RuntimeException("Item is not an object.");
    }

    public int getSize() {
        throw new RuntimeException("Item is not an array.");
    }

    public String getStringValue() {
        throw new RuntimeException("Item is not a string.");
    }

    public boolean getBooleanValue() {
        throw new RuntimeException("Item is not a boolean.");
    }

    public double getDoubleValue() {
        throw new RuntimeException("Item is not a double.");
    }

    public int getIntegerValue() {
        throw new RuntimeException("Item is not an integer.");
    }

    public BigDecimal getDecimalValue() {
        throw new RuntimeException("Item is not a big decimal.");
    }

    public abstract boolean isTypeOf(ItemType type);

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isAtomic() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isString() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isInteger() {
        return false;
    }

    public boolean isDouble() {
        return false;
    }

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
    
    public abstract boolean equals(Object otherItem);

    public abstract int hashCode();
}
