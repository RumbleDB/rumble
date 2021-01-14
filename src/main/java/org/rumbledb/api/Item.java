package org.rumbledb.api;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;

import com.esotericsoftware.kryo.KryoSerializable;


/**
 * An instance of this class is an item in the JSONiq data model.
 *
 * JSONiq manipulates sequences of items.
 *
 * All calls should be made via this interface. Objects of type Item should never be cast to a subclass (in a subsequent
 * version,
 * we will make the classes implementing this interface visible only at the package level).
 *
 * An item can be structured or atomic or a function.
 *
 * Structured items include objects and arrays. Objects are mappings from strings (keys) to items. Arrays are ordered
 * lists of items.
 *
 * Atomic items have a lexical value and a type. Rumble does not support all atomic types yet.
 *
 * @author Ghislain Fourny, Stefan Irimescu, Can Berker Cikis
 */
public interface Item extends Serializable, KryoSerializable {

    /**
     * Tests whether the item is an array.
     *
     * @return true if it is an array, false otherwise.
     */
    boolean isArray();

    /**
     * Tests whether the item is an object.
     *
     * @return true if it is an object, false otherwise.
     */
    boolean isObject();

    /**
     * Tests whether the item is a function.
     *
     * @return true if it is a function, false otherwise
     */
    boolean isFunction();

    /**
     * Tests whether the item is an atomic item.
     *
     * @return true if it is an atomic item, false otherwise.
     */
    boolean isAtomic();

    /**
     * Tests whether the item is an atomic item of type string.
     *
     * @return true if it is an atomic item of type string, false otherwise.
     */
    boolean isString();

    /**
     * Tests whether the item is an atomic item of type boolean.
     *
     * @return true if it is an atomic item of type boolean, false otherwise.
     */
    boolean isBoolean();

    /**
     * Tests whether the item is the null item.
     *
     * @return true if it is the null item, false otherwise.
     */
    boolean isNull();

    /**
     * Tests whether the item is a number (decimal or double).
     *
     * @return true if it is a number, false otherwise.
     */
    boolean isNumeric();

    /**
     * Tests whether the item is an atomic item of type decimal.
     *
     * @return true if it is an atomic item of type decimal, false otherwise.
     */
    boolean isDecimal();

    /**
     * Tests whether the item is an atomic item of type integer.
     *
     * @return true if it is an atomic item of type integer, false otherwise.
     */
    boolean isInteger();

    /**
     * Tests whether the item is an atomic item of type int.
     *
     * @return true if it is an atomic item of type int, false otherwise.
     */
    boolean isInt();

    /**
     * Tests whether the item is an atomic item of type double.
     *
     * @return true if it is an atomic item of type double, false otherwise.
     */
    boolean isDouble();

    /**
     * Tests whether the item is an atomic item of type float.
     *
     * @return true if it is an atomic item of type float, false otherwise.
     */
    boolean isFloat();

    /**
     * Tests whether the item is an atomic item of type duration.
     *
     * @return true if it is an atomic item of type duration, false otherwise.
     */
    boolean isDuration();

    /**
     * Tests whether the item is an atomic item of type yearMonthDuration.
     *
     * @return true if it is an atomic item of type yearMonthDuration, false otherwise.
     */
    boolean isYearMonthDuration();

    /**
     * Tests whether the item is an atomic item of type dayTimeDuration.
     *
     * @return true if it is an atomic item of type dayTimeDuration, false otherwise.
     */
    boolean isDayTimeDuration();

    /**
     * Tests whether the item is an atomic item of type dateTime.
     *
     * @return true if it is an atomic item of type dateTime, false otherwise.
     */
    boolean isDateTime();

    /**
     * Tests whether the item is an atomic item of type date.
     *
     * @return true if it is an atomic item of type date, false otherwise.
     */
    boolean isDate();

    /**
     * Tests whether the item is an atomic item of type time.
     *
     * @return true if it is an atomic item of type time, false otherwise.
     */
    boolean isTime();

    /**
     * Tests whether the item is an atomic item of type anyURI.
     *
     * @return true if it is an atomic item of type anyURI, false otherwise.
     */
    boolean isAnyURI();

    /**
     * Tests whether the item is an atomic item of type base64Binary or hexBinary.
     *
     * @return true if it is an atomic item of type base64Binary or hexBinary, false otherwise.
     */
    boolean isBinary();

    /**
     * Tests whether the item is an atomic item of type hexBinary.
     *
     * @return true if it is an atomic item of type hexBinary, false otherwise.
     */
    boolean isHexBinary();

    /**
     * Tests whether the item is an atomic item of type base64Binary.
     *
     * @return true if it is an atomic item of type base64Binary, false otherwise.
     */
    boolean isBase64Binary();

    /**
     * Returns the members of the item if it is an array.
     *
     * @return the list of the array members.
     */
    List<Item> getItems();

    /**
     * Returns the member of the item at the specified position if it is an array.
     *
     * @param position a position.
     * @return the member at position position.
     */
    Item getItemAt(int position);

    /**
     * Returns the keys of the item, if it is an object.
     *
     * @return the list of the keys.
     */
    List<String> getKeys();

    /**
     * Returns the values of the item, if it is an object.
     *
     * @return the list of the value items.
     */
    List<Item> getValues();

    /**
     * Returns the value associated with a specific key, if it is an object.
     *
     * @param key a key.
     * @return the value associated with key.
     */
    Item getItemByKey(String key);

    /**
     * Returns the size of the item, if it is an array.
     *
     * @return the size as an int.
     */
    int getSize();

    /**
     * Returns the string value of the item, if it is a string.
     *
     * @return the string value.
     */
    String getStringValue();

    /**
     * Returns the boolean value of the item, if it is a boolean.
     *
     * @return the boolean value.
     */
    boolean getBooleanValue();

    /**
     * Returns the double value of the item, if it is a double.
     *
     * @return the double value.
     */
    double getDoubleValue();

    /**
     * Returns the float value of the item, if it is a float.
     *
     * @return the float value.
     */
    float getFloatValue();

    /**
     * Returns the int value of the item, if it is an int.
     *
     * @return the integer value as an int.
     */
    int getIntValue();

    /**
     * Returns the integer value of the item as a bit integer, if it is an integer.
     *
     * @return the integer value as a BigInteger.
     */
    BigInteger getIntegerValue();

    /**
     * Returns the decimal value of the item, if it is a decimal.
     *
     * @return the decimal value as a BigDecimal.
     */
    BigDecimal getDecimalValue();

    /**
     * Returns the period value of the item, if it is a duration.
     *
     * @return the period value as a Period.
     */
    Period getDurationValue();

    /**
     * Returns the dateTime value of the item, if it is a atomic item of type dateTimeItem or dateItem or timeItem.
     *
     * @return the dateTime value as a DateTime.
     */
    DateTime getDateTimeValue();

    /**
     * Returns the byte[] value of the item, if it is a atomic item of type hexBinary or Base64Binary.
     *
     * @return the binary value as an array of bytes.
     */
    byte[] getBinaryValue();

    /**
     * Returns the dynamic type of the item (only for error message purposes).
     * 
     * @return the dynamic type as an item type.
     */
    ItemType getDynamicType();

    /**
     * Returns the identifier (name and arity) of the function, if it is a function item.
     * 
     * @return the function identifier.
     */
    FunctionIdentifier getIdentifier();

    /**
     * Returns the names of the parameters of the function, if it is a function item.
     * 
     * @return the function parameter names.
     */
    List<Name> getParameterNames();

    /**
     * Returns the signature of the function, if it is a function item.
     * 
     * @return the function signature.
     */
    FunctionSignature getSignature();

    /**
     * Returns the body iterator, if it is a function item.
     * 
     * @return the function signature.
     */
    public RuntimeIterator getBodyIterator();

    /**
     * Returns the local variable bindings, if it is a function item.
     * 
     * @return the function signature.
     */
    public Map<Name, List<Item>> getLocalVariablesInClosure();

    /**
     * Returns the RDD variable bindings, if it is a function item.
     * 
     * @return the function signature.
     */
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure();

    /**
     * Returns the DataFrame variable bindings, if it is a function item.
     * 
     * @return the function signature.
     */
    public Map<Name, Dataset<Row>> getDFVariablesInClosure();

    /**
     * Returns the module dynamic context, if it is a function item.
     * 
     * @return the function signature.
     */
    public DynamicContext getDynamicModuleContext();

    /**
     * @return true if the Item has a timeZone, false otherwise
     */
    boolean hasTimeZone();

    /**
     * Tests whether the item contains a representation of date or time (or both).
     *
     * @return true if it is an atomic item of type time, date or dateTime, false otherwise.
     */
    boolean hasDateTime();

    /**
     * Returns the effective boolean value of the item, if atomic.
     *
     * @return the effective boolean value.
     */
    boolean getEffectiveBooleanValue();

    /**
     * Appends an item, if it is an array.
     *
     * @param item an item.
     */
    void putItem(Item item);

    /**
     * Adds a value pair, if it is an array item.
     *
     * @param value a value.
     */
    void append(Item value);

    /**
     * Adds a key-value pair, if it is an object item.
     *
     * @param key a key.
     * @param value a value.
     */
    void putItemByKey(String key, Item value);

    /**
     * Casts the item to a double value (must be a numeric).
     *
     * @return the double value.
     */
    double castToDoubleValue();

    /**
     * Casts the item to a float value (must be a numeric).
     *
     * @return the float value.
     */
    float castToFloatValue();

    /**
     * Casts the item to a decimal value (must be a numeric).
     *
     * @return the BigDecimal value.
     */
    BigDecimal castToDecimalValue();

    /**
     * Casts the item to a big integer value (must be a numeric).
     *
     * @return the BigInteger value.
     */
    BigInteger castToIntegerValue();

    /**
     * Casts the item to an integer value (must be a numeric).
     *
     * @return the int value.
     */
    int castToIntValue();

    /**
     * Tests for logical equality. The semantics are that of the eq operator.
     *
     * @param other another item.
     * @return true it is equal to other, false otherwise.
     */
    boolean equals(Object other);

    /**
     * Computes a hash code.
     *
     * @return a hash code as an int.
     */
    int hashCode();

    String serialize();
}
