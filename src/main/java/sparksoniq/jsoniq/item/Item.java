/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Author: Stefan Irimescu
 *
 */
package sparksoniq.jsoniq.item;

import sparksoniq.exceptions.IteratorFlowException;
import sparksoniq.jsoniq.item.base.SerializableItem;
import sparksoniq.jsoniq.item.metadata.ItemMetadata;
import sparksoniq.semantics.types.ItemType;

import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

//TODO serialize with indentation
public abstract class Item implements SerializableItem, Comparable<Item> {

    public boolean isNumeric() {
        return this instanceof IntegerItem || this instanceof DecimalItem || this instanceof DoubleItem;
    }

    //performs conversions for binary operations with a numeric return type
    //(int,double) -> double
    //(int,decimal) -> decimal
    //(decimal,double) -> double
    public Type getNumericResultType(Item other) {
        if (this instanceof DecimalItem)
            return DecimalItem.class;
        if (this instanceof DoubleItem) {
            if (other instanceof DecimalItem)
                return DecimalItem.class;
            return DoubleItem.class;
        }
        if (this instanceof IntegerItem) {
            if (other instanceof DoubleItem)
                return DoubleItem.class;
            if (other instanceof DecimalItem)
                return DecimalItem.class;
            return IntegerItem.class;
        }
        return DecimalItem.class;
    }

    public <T> T getNumericValue(Class<T> type) {
        if (this.isNumeric()) {
            if (this instanceof DoubleItem) {
                Double result = ((DoubleItem) this).getDoubleValue();
                if (type.equals(BigDecimal.class))
                    return (T) BigDecimal.valueOf(result);
                if (type.equals(Integer.class))
                    return (T) new Integer(result.intValue());
                return (T) result;
            }
            if (this instanceof IntegerItem) {
                Integer result = ((IntegerItem) this).getIntegerValue();
                if (type.equals(BigDecimal.class))
                    return (T) BigDecimal.valueOf(result);
                if (type.equals(Double.class))
                    return (T) new Double(result.doubleValue());
                return (T) result;
            }
            if (this instanceof DecimalItem) {
                BigDecimal result = ((DecimalItem) this).getDecimalValue();
                if (type.equals(Integer.class))
                    return (T) new Integer(result.intValue());
                if (type.equals(Double.class))
                    return (T) new Double(result.doubleValue());
                return (T) result;
            }

        }
        throw new IteratorFlowException("Cannot call getNumericValue on non numeric", this.getItemMetadata());
    }

    //returns an effective boolean value of any item type
    public boolean getEffectiveBooleanValue() {
        if (this instanceof BooleanItem)
            return ((BooleanItem) this).getBooleanValue();
        if (this.isNumeric()) {
            if (this instanceof IntegerItem)
                return ((IntegerItem) this).getIntegerValue() != 0;
            if (this instanceof DoubleItem)
                return ((DoubleItem) this).getDoubleValue() != 0;
            if (this instanceof DecimalItem)
                return !((DecimalItem) this).getDecimalValue().equals(0);
        }
        if (this instanceof NullItem)
            return false;
        if (this instanceof StringItem)
            return !((StringItem) this).getStringValue().isEmpty();
        if (this instanceof ObjectItem)
            return ((ObjectItem) this).getKeys() != null && !((ObjectItem) this).getKeys().isEmpty();
        if (this instanceof ArrayItem)
            return ((ArrayItem) this).getItems() != null && !((ArrayItem) this).getItems().isEmpty();

        return true;
    }

    /**
     * Function that compares 2 items.
     * Non-atomics can't be compared.
     * Items have to be of the same type or one them has to be null.
     * @return -1 if this < other; 0 if this == other; 1 if this > other;
     */
    public int compareTo(Item other) {
        int result;
        if (this.isNumeric() && other.isNumeric()) {
            BigDecimal value1 = this.getNumericValue(BigDecimal.class);
            BigDecimal value2 = other.getNumericValue(BigDecimal.class);
            result = value1.compareTo(value2);
        } else if (this instanceof BooleanItem && other instanceof BooleanItem) {
            Boolean value1 = new Boolean(((BooleanItem) this).getBooleanValue());
            Boolean value2 = new Boolean(((BooleanItem) other).getBooleanValue());
            result = value1.compareTo(value2);
        } else if (this instanceof StringItem&& other instanceof StringItem) {
            String value1 = ((StringItem) this).getStringValue();
            String value2 = ((StringItem) other).getStringValue();
            result = value1.compareTo(value2);
        }
        else if (this instanceof NullItem || other instanceof NullItem) {
            // null equals null
            if (this instanceof NullItem && other instanceof NullItem) {
                result = 0;
            }
            else if (this instanceof NullItem) {
                result = -1;
            }
            else{
                result = 1;
            }
        } else
            result = this.serialize().compareTo(other.serialize());
        return result;
    }

    public boolean checkEquality(Item other) {
        return this.compareTo(other) == 0;
    }

    public abstract List<Item> getItems() throws OperationNotSupportedException;

    public abstract Item getItemAt(int i) throws OperationNotSupportedException;

    public abstract void putItem(Item value) throws OperationNotSupportedException;

    public abstract List<String> getKeys() throws OperationNotSupportedException;

    public abstract Collection<? extends Item> getValues() throws OperationNotSupportedException;

    public abstract Item getItemByKey(String s) throws OperationNotSupportedException;

    public abstract void putItemByKey(String s, Item value) throws OperationNotSupportedException;

    public abstract int getSize() throws OperationNotSupportedException;

    public abstract String getStringValue() throws OperationNotSupportedException;

    public abstract boolean getBooleanValue() throws OperationNotSupportedException;

    public abstract double getDoubleValue() throws OperationNotSupportedException;

    public abstract int getIntegerValue() throws OperationNotSupportedException;

    public abstract BigDecimal getDecimalValue() throws OperationNotSupportedException;

    public abstract boolean isTypeOf(ItemType type);

    public boolean isAtomic() {
        return false;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isObject() {
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

    public ItemMetadata getItemMetadata() {
        return itemMetadata;
    }

    protected Item(ItemMetadata itemMetadata) {
        this.itemMetadata = itemMetadata;
    }

    private void readObject(ObjectInputStream aInputStream)
            throws ClassNotFoundException, IOException {
        aInputStream.defaultReadObject();
    }

    private final ItemMetadata itemMetadata;


    private void writeObject(ObjectOutputStream aOutputStream)
            throws IOException {
        aOutputStream.defaultWriteObject();
    }
}
