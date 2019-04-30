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
public abstract class Item implements SerializableItem {
    public static boolean isNumeric(Item item) {
        return item.isInteger() || item.isDecimal() || item.isDouble();
    }

    //performs conversions for binary operations with a numeric return type
    //(int,double) -> double
    //(int,decimal) -> decimal
    //(decimal,double) -> double
    public static Type getNumericResultType(Item left, Item right) {
        if (left.isDecimal())
            return DecimalItem.class;
        if (left.isDouble()) {
            if (right.isDecimal())
                return DecimalItem.class;
            return DoubleItem.class;
        }
        if (left.isInteger()) {
            if (right.isDouble())
                return DoubleItem.class;
            if (right.isDecimal())
                return DecimalItem.class;
            return IntegerItem.class;
        }
        return DecimalItem.class;
    }

    public static <T> T getNumericValue(Item item, Class<T> type) {
        if (isNumeric(item)) {
            if (item.isDouble()) {
                Double result = item.getDoubleValue();
                if (type.equals(BigDecimal.class))
                    return (T) BigDecimal.valueOf(result);
                if (type.equals(Integer.class))
                    return (T) new Integer(result.intValue());
                return (T) result;
            }
            if (item.isInteger()) {
                Integer result = item.getIntegerValue();
                if (type.equals(BigDecimal.class))
                    return (T) BigDecimal.valueOf(result);
                if (type.equals(Double.class))
                    return (T) new Double(result.doubleValue());
                return (T) result;
            }
            if (item.isDecimal()) {
                BigDecimal result = item.getDecimalValue();
                if (type.equals(Integer.class))
                    return (T) new Integer(result.intValue());
                if (type.equals(Double.class))
                    return (T) new Double(result.doubleValue());
                return (T) result;
            }

        }
        throw new IteratorFlowException("Cannot call getNumericValue on non numeric");
    }

    //returns an effective boolean value of any item type
    public static boolean getEffectiveBooleanValue(Item item) {
        if (item == null)
            return false;
        else if (item.isBoolean())
            return item.getBooleanValue();
        else if (isNumeric(item)) {
            if (item.isInteger())
                return item.getIntegerValue() != 0;
            else if (item.isDouble())
                return item.getDoubleValue() != 0;
            else if (item.isDecimal())
                return !item.getDecimalValue().equals(0);
        }
        else if (item.isNull())
            return false;
        else if (item.isString())
            return !item.getStringValue().isEmpty();
        else if (item.isObject())
            return true;
        else if (item.isArray())
            return true;

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
        } else if (v1.isBoolean() && v2.isBoolean()) {
            Boolean value1 = new Boolean(v1.getBooleanValue());
            Boolean value2 = new Boolean(v2.getBooleanValue());
            result = value1.compareTo(value2);
        } else if (v1.isString()&& v2.isString()) {
            String value1 = v1.getStringValue();
            String value2 = v2.getStringValue();
            result = value1.compareTo(value2);
        }
        else if (v1.isNull() || v2.isNull()) {
            // null equals null
            if (v1.isNull() && v2.isNull()) {
                result = 0;
            }
            else if (v1.isNull()) {
                result = -1;
            }
            else{
                result = 1;
            }
        } else {
            result = v1.serialize().compareTo(v2.serialize());
        }
        return result;
    }

    public static boolean checkEquality(Item v1, Item v2) {
        return compareItems(v1, v2) == 0;
    }

    public List<Item> getItems() {
        throw new RuntimeException("Item is not an array.");
    };

    public Item getItemAt(int i) {
        throw new RuntimeException("Item is not an array.");
    };

    public void putItem(Item value) {
        throw new RuntimeException("Item is not an array.");
    };

    public List<String> getKeys() {
        throw new RuntimeException("Item is not an object.");
    };

    public Collection<? extends Item> getValues() {
        throw new RuntimeException("Item is not an object.");
    };

    public Item getItemByKey(String s) {
        throw new RuntimeException("Item is not an object.");
    };

    public void putItemByKey(String s, Item value) {
        throw new RuntimeException("Item is not an object.");
    };

    public int getSize() {
        throw new RuntimeException("Item is not an array.");
    };

    public String getStringValue() {
        throw new RuntimeException("Item is not a string.");
    };

    public boolean getBooleanValue() {
        throw new RuntimeException("Item is not a boolean.");
    };

    public double getDoubleValue() {
        throw new RuntimeException("Item is not a double.");
    };

    public int getIntegerValue() {
        throw new RuntimeException("Item is not an integer.");
    };

    public BigDecimal getDecimalValue() {
        throw new RuntimeException("Item is not a big decimal.");
    };

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

    protected Item() {
    }

    private void readObject(ObjectInputStream aInputStream)
            throws ClassNotFoundException, IOException {
        aInputStream.defaultReadObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream)
            throws IOException {
        aOutputStream.defaultWriteObject();
    }
}
