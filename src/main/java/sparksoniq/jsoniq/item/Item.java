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
import java.util.List;

//TODO serialize with indentation
public abstract class Item implements SerializableItem {
    public static boolean isAtomic(Item resultItem) {
        return resultItem instanceof AtomicItem;
    }

    public static boolean isNumeric(Item item) {
        return item instanceof IntegerItem || item instanceof DecimalItem || item instanceof DoubleItem;
    }

    //performs conversions for binary operations with a numeric return type
    //(int,double) -> double
    //(int,decimal) -> decimal
    //(decimal,double) -> double
    public static Type getNumericResultType(Item left, Item right) {
        if (left instanceof DecimalItem)
            return DecimalItem.class;
        if (left instanceof DoubleItem) {
            if (right instanceof DecimalItem)
                return DecimalItem.class;
            return DoubleItem.class;
        }
        if (left instanceof IntegerItem) {
            if (right instanceof DoubleItem)
                return DoubleItem.class;
            if (right instanceof DecimalItem)
                return DecimalItem.class;
            return IntegerItem.class;
        }
        return DecimalItem.class;
    }

    public static <T> T getNumericValue(Item item, Class<T> type) {
        if (isNumeric(item)) {
            if (item instanceof DoubleItem) {
                Double result = ((DoubleItem) item).getDoubleValue();
                if (type.equals(BigDecimal.class))
                    return (T) BigDecimal.valueOf(result);
                if (type.equals(Integer.class))
                    return (T) new Integer(result.intValue());
                return (T) result;
            }
            if (item instanceof IntegerItem) {
                Integer result = ((IntegerItem) item).getIntegerValue();
                if (type.equals(BigDecimal.class))
                    return (T) BigDecimal.valueOf(result);
                if (type.equals(Double.class))
                    return (T) new Double(result.doubleValue());
                return (T) result;
            }
            if (item instanceof DecimalItem) {
                BigDecimal result = ((DecimalItem) item).getDecimalValue();
                if (type.equals(Integer.class))
                    return (T) new Integer(result.intValue());
                if (type.equals(Double.class))
                    return (T) new Double(result.doubleValue());
                return (T) result;
            }

        }
        throw new IteratorFlowException("Cannot call getNumericValue on non numeric", item.getItemMetadata());
    }

    //returns an effective boolean value of any item type
    public static boolean getEffectiveBooleanValue(Item item) {
        if (item == null)
            return false;
        if (item instanceof BooleanItem)
            return ((BooleanItem) item).getBooleanValue();
        if (isNumeric(item)) {
            if (item instanceof IntegerItem)
                return ((IntegerItem) item).getIntegerValue() != 0;
            if (item instanceof DoubleItem)
                return ((DoubleItem) item).getDoubleValue() != 0;
            if (item instanceof DecimalItem)
                return !((DecimalItem) item).getDecimalValue().equals(0);
        }
        if (item instanceof NullItem)
            return false;
        if (item instanceof StringItem)
            return !((StringItem) item).getStringValue().isEmpty();
        if (item instanceof ObjectItem)
            return ((ObjectItem) item).getKeys() != null && !((ObjectItem) item).getKeys().isEmpty();
        if (item instanceof ArrayItem)
            return ((ArrayItem) item).getItems() != null && !((ArrayItem) item).getItems().isEmpty();

        return true;
    }

    /**
     * Function that compares 2 items.
     * Non-atomics can't be compared.
     * Items have to be of the same type or one them has to be null.
     * @return -1 if v1 < v2; 0 if v1 == v2; 1 if v1 > v2;
     */
    public static int compareItems(Item v1, Item v2) {
        int result;
        if (Item.isNumeric(v1) && Item.isNumeric(v2)) {
            BigDecimal value1 = Item.getNumericValue(v1, BigDecimal.class);
            BigDecimal value2 = Item.getNumericValue(v2, BigDecimal.class);
            result = value1.compareTo(value2);
        } else if (v1 instanceof BooleanItem && v2 instanceof BooleanItem) {
            Boolean value1 = new Boolean(((BooleanItem) v1).getBooleanValue());
            Boolean value2 = new Boolean(((BooleanItem) v2).getBooleanValue());
            result = value1.compareTo(value2);
        } else if (v1 instanceof StringItem&& v2 instanceof StringItem) {
            String value1 = ((StringItem) v1).getStringValue();
            String value2 = ((StringItem) v2).getStringValue();
            result = value1.compareTo(value2);
        }
        else if (v1 instanceof NullItem || v2 instanceof NullItem) {
            // null equals null
            if (v1 instanceof NullItem && v2 instanceof NullItem) {
                result = 0;
            }
            else if (v1 instanceof NullItem) {
                result = -1;
            }
            else{
                result = 1;
            }
        } else
            result = v1.serialize().compareTo(v2.serialize());
        return result;
    }

    public static boolean checkEquality(Item v1, Item v2) {
        return compareItems(v1, v2) == 0;
    }

    public abstract Item getItemAt(int i) throws OperationNotSupportedException;

    public abstract Item getItemByKey(String s) throws OperationNotSupportedException;

    public abstract void putItemByKey(String s, Item value) throws OperationNotSupportedException;

    public abstract List<String> getKeys() throws OperationNotSupportedException;

    public abstract int getSize() throws OperationNotSupportedException;

    public abstract String getStringValue() throws OperationNotSupportedException;

    public abstract boolean getBooleanValue() throws OperationNotSupportedException;

    public abstract double getDoubleValue() throws OperationNotSupportedException;

    public abstract int getIntegerValue() throws OperationNotSupportedException;

    public abstract BigDecimal getDecimalValue() throws OperationNotSupportedException;

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
