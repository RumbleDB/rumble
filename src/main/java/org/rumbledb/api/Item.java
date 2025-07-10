package org.rumbledb.api;

import com.esotericsoftware.kryo.KryoSerializable;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.ml.Estimator;
import org.apache.spark.ml.Transformer;

import java.time.*;

import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.items.xml.XMLDocumentPosition;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.serialization.Serializer;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * An instance of this class is an item in the JSONiq data model.
 * JSONiq manipulates sequences of items.
 * All calls should be made via this interface. Objects of type Item should never be cast to a subclass (in a subsequent
 * version,
 * we will make the classes implementing this interface visible only at the package level).
 * An item can be structured or atomic or a function.
 * Structured items include objects and arrays. Objects are mappings from strings (keys) to items. Arrays are ordered
 * lists of items.
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
    default boolean isArray() {
        return false;
    }

    /**
     * Tests whether the item is an object.
     *
     * @return true if it is an object, false otherwise.
     */
    default boolean isObject() {
        return false;
    }

    /**
     * Tests whether the item is a function.
     *
     * @return true if it is a function, false otherwise
     */
    default boolean isFunction() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item.
     *
     * @return true if it is an atomic item, false otherwise.
     */
    default boolean isAtomic() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type string.
     *
     * @return true if it is an atomic item of type string, false otherwise.
     */
    default boolean isString() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type default boolean.
     *
     * @return true if it is an atomic item of type default boolean, false otherwise.
     */
    default boolean isBoolean() {
        return false;
    }

    /**
     * Tests whether the item is the null item.
     *
     * @return true if it is the null item, false otherwise.
     */
    default boolean isNull() {
        return false;
    }

    /**
     * Tests whether the item is a number (decimal or double).
     *
     * @return true if it is a number, false otherwise.
     */
    default boolean isNumeric() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type decimal.
     *
     * @return true if it is an atomic item of type decimal, false otherwise.
     */
    default boolean isDecimal() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type integer.
     *
     * @return true if it is an atomic item of type integer, false otherwise.
     */
    default boolean isInteger() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type int.
     *
     * @return true if it is an atomic item of type int, false otherwise.
     */
    default boolean isInt() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type double.
     *
     * @return true if it is an atomic item of type double, false otherwise.
     */
    default boolean isDouble() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type float.
     *
     * @return true if it is an atomic item of type float, false otherwise.
     */
    default boolean isFloat() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type duration.
     *
     * @return true if it is an atomic item of type duration, false otherwise.
     */
    default boolean isDuration() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type period.
     *
     * @return true if it is an atomic item of type period, false otherwise.
     */
    default boolean isPeriod() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type yearMonthDuration.
     *
     * @return true if it is an atomic item of type yearMonthDuration, false otherwise.
     */
    default boolean isYearMonthDuration() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type dayTimeDuration.
     *
     * @return true if it is an atomic item of type dayTimeDuration, false otherwise.
     */
    default boolean isDayTimeDuration() {
        return false;
    }

    /**
     * Return only month of the item, if it's DateTime or Duration
     * It will not convert years into months
     * 
     * @return only month
     */
    default int getMonth() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Return year of the item, if it's DateTime or Duration
     *
     * @return year
     */
    default int getYear() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Return only day of the item, if it's DateTime or Duration
     * It will not convert months and years into days.
     * 
     * @return only day
     */
    default int getDay() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Offset is an integer between −840 and 840 inclusive
     *
     * @return offset in minutes
     */
    default int getOffset() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Return only hour of the item, if it's DateTime, Time or Duration
     *
     * @return only hour
     */
    default int getHour() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());

    }

    /**
     * Return only minutes of the item, if it's DateTime, Time or Duration
     * It will not convert hours into minutes
     * 
     * @return only minute
     */
    default int getMinute() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());

    }

    /**
     * Return only seconds of the item, if it's DateTime, Time or Duration
     * It will not convert hours and minutes into seconds
     * 
     * @return only seconds
     */
    default double getSecond() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());

    }

    /**
     * Return the only nanoseconds of the item, if it's DateTime, Time or Duration
     * It will not convert hours, minutes and seconds into nanoseconds
     * It exists only if the value in seconds will have decimal values, otherwise it will return 0
     * 
     * @return only nanoseconds
     */
    default int getNanosecond() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());

    }

    /**
     * Tests whether the item is an atomic item of type dateTime.
     *
     * @return true if it is an atomic item of type dateTime, false otherwise.
     */
    default boolean isDateTime() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type date.
     *
     * @return true if it is an atomic item of type date, false otherwise.
     */
    default boolean isDate() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type time.
     *
     * @return true if it is an atomic item of type time, false otherwise.
     */
    default boolean isTime() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type gDay.
     *
     * @return true if it is an atomic item of type gDay, false otherwise.
     */
    default boolean isGDay() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type gMonth.
     *
     * @return true if it is an atomic item of type gMonth, false otherwise.
     */
    default boolean isGMonth() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type gYear.
     *
     * @return true if it is an atomic item of type gYear, false otherwise.
     */
    default boolean isGYear() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type gMonthDay.
     *
     * @return true if it is an atomic item of type gMonthDay, false otherwise.
     */
    default boolean isGMonthDay() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type gMonthDay.
     *
     * @return true if it is an atomic item of type gMonthDay, false otherwise.
     */
    default boolean isGYearMonth() {
        return false;
    }


    /**
     * Tests whether the item is an atomic item of type anyURI.
     *
     * @return true if it is an atomic item of type anyURI, false otherwise.
     */
    default boolean isAnyURI() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type base64Binary or hexBinary.
     *
     * @return true if it is an atomic item of type base64Binary or hexBinary, false otherwise.
     */
    default boolean isBinary() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type hexBinary.
     *
     * @return true if it is an atomic item of type hexBinary, false otherwise.
     */
    default boolean isHexBinary() {
        return false;
    }

    /**
     * Tests whether the item is an atomic item of type base64Binary.
     *
     * @return true if it is an atomic item of type base64Binary, false otherwise.
     */
    default boolean isBase64Binary() {
        return false;
    }

    /**
     * Tests whether the item is an XML Element node.
     *
     * @return true if it is an XML Element node, false otherwise.
     */
    default boolean isElementNode() {
        return false;
    }

    /**
     * Tests whether the item is an XML Attribute node.
     *
     * @return true if it is an XML Attribute node, false otherwise.
     */
    default boolean isAttributeNode() {
        return false;
    }

    /**
     * Tests whether the item is an XML Text node.
     *
     * @return true if it is an XML Text node, false otherwise.
     */
    default boolean getContent() {
        return false;
    }

    /**
     * Tests whether the item is an XML Document node.
     *
     * @return true if it is an XML Document node, false otherwise.
     */
    default boolean isDocumentNode() {
        return false;
    }

    default boolean isTextNode() {
        return false;
    }

    /**
     * Tests whether the item is an XML node.
     *
     * @return true if it is an XML node, false otherwise.
     */
    default boolean isNode() {
        return false;
    }

    /**
     * Returns the members of the item if it is an array.
     *
     * @return the list of the array members.
     */
    default List<Item> getItems() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the member of the item at the specified position if it is an array.
     *
     * @param position a position.
     * @return the member at position position.
     */
    default Item getItemAt(int position) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the keys of the item, if it is an object.
     *
     * @return the list of the keys.
     */
    default List<String> getKeys() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the values of the item, if it is an object.
     *
     * @return the list of the value items.
     */
    default List<Item> getValues() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the value associated with a specific key, if it is an object.
     *
     * @param key a key.
     * @return the value associated with key.
     */
    default Item getItemByKey(String key) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the size of the item, if it is an array.
     *
     * @return the size as an int.
     */
    default int getSize() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the string value of the item, if it is an atomic item.
     *
     * @return the string value.
     */
    default String getStringValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the boolean value of the item, if it is a boolean.
     *
     * @return the boolean value.
     */
    default boolean getBooleanValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the double value of the item, if it is a double.
     *
     * @return the double value.
     */
    default double getDoubleValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the float value of the item, if it is a float.
     *
     * @return the float value.
     */
    default float getFloatValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the int value of the item, if it is an int.
     *
     * @return the integer value as an int.
     */
    default int getIntValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the integer value of the item as a bit integer, if it is an integer.
     *
     * @return the integer value as a BigInteger.
     */
    default BigInteger getIntegerValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the decimal value of the item, if it is a decimal.
     *
     * @return the decimal value as a BigDecimal.
     */
    default BigDecimal getDecimalValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the period value of the item, if it is a duration.
     *
     * @return the period value as a Period.
     */
    default Period getPeriodValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the duration value of the item, if it is a duration.
     *
     * @return the duration value as a Duration.
     */
    default Duration getDurationValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the EpochMillis of the item, if it's DateTime or Duration
     * It will collect all the parts of the item and compress it into the EpochMillis
     * 
     * @return the EpochMillis
     */
    default long getEpochMillis() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the dateTime value of the item, if it is an atomic item of type dateTimeItem or dateItem or timeItem.
     *
     * @return the dateTime value as a OffsetDateTime.
     */
    default OffsetDateTime getDateTimeValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the time value of the item, if it is an atomic item of type or timeItem.
     *
     * @return the time value as a OffsetTime.
     */
    default OffsetTime getTimeValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the byte[] value of the item, if it is an atomic item of type hexBinary or Base64Binary.
     *
     * @return the binary value as an array of bytes.
     */
    default byte[] getBinaryValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the dynamic type of the item (only for error message purposes).
     * 
     * @return the dynamic type as an item type.
     */
    default ItemType getDynamicType() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the identifier (name and arity) of the function, if it is a function item.
     * 
     * @return the function identifier.
     */
    default FunctionIdentifier getIdentifier() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the names of the parameters of the function, if it is a function item.
     * 
     * @return the function parameter names.
     */
    default List<Name> getParameterNames() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the signature of the function, if it is a function item.
     * 
     * @return the function signature.
     */
    default FunctionSignature getSignature() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the body iterator, if it is a function item.
     * 
     * @return the function signature.
     */
    default RuntimeIterator getBodyIterator() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the local variable bindings, if it is a function item.
     * 
     * @return the function signature.
     */
    default Map<Name, List<Item>> getLocalVariablesInClosure() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the RDD variable bindings, if it is a function item.
     * 
     * @return the function signature.
     */
    default Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the DataFrame variable bindings, if it is a function item.
     * 
     * @return the function signature.
     */
    default Map<Name, JSoundDataFrame> getDFVariablesInClosure() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the module dynamic context, if it is a function item.
     * 
     * @return the function signature.
     */
    default DynamicContext getModuleDynamicContext() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * @return true if the Item has a timeZone, false otherwise
     */
    default boolean hasTimeZone() {
        return false;
    }

    /**
     * Tests whether the item contains a representation of date or time (or both).
     *
     * @return true if it is an atomic item of type time, date or dateTime, false otherwise.
     */
    default boolean hasDateTime() {
        return false;
    }

    /**
     * Returns the effective boolean value of the item, if atomic.
     *
     * @return the effective boolean value.
     */
    default boolean getEffectiveBooleanValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Appends an item, if it is an array.
     *
     * @param item an item.
     */
    default void putItem(Item item) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Add an item at index i, if it is an array.
     *
     * @param item an item.
     * @param i an integer.
     */
    default void putItemAt(Item item, int i) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Add all items in items at index i, if it is an array.
     *
     * @param items a list of items.
     * @param i an integer.
     */
    default void putItemsAt(List<Item> items, int i) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Remove the item at index i, if it is an array.
     *
     * @param i an integer.
     */
    default void removeItemAt(int i) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Adds a value pair, if it is an array item.
     *
     * @param value a value.
     */
    default void append(Item value) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Adds a key-value pair, if it is an object item.
     *
     * @param key a key.
     * @param value a value.
     */
    default void putItemByKey(String key, Item value) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Removes a key-value pair, if it is an object item.
     *
     * @param key a key.
     */
    default void removeItemByKey(String key) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Adds a key-value pair, if it is an object item. The value is lazily computed.
     *
     * @param key a key.
     * @param iterator a runtime iterator.
     * @param context a dynamic context.
     * @param isArray whether to always wrap the result in an array.
     */
    default void putLazyItemByKey(
            String key,
            RuntimeIterator iterator,
            DynamicContext context,
            boolean isArray
    ) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Casts the item to a double value (must be a numeric).
     *
     * @return the double value.
     */
    default double castToDoubleValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Casts the item to a float value (must be a numeric).
     *
     * @return the float value.
     */
    default float castToFloatValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Casts the item to a decimal value (must be a numeric).
     *
     * @return the BigDecimal value.
     */
    default BigDecimal castToDecimalValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Casts the item to a big integer value (must be a numeric).
     *
     * @return the BigInteger value.
     */
    default BigInteger castToIntegerValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Casts the item to an integer value (must be a numeric).
     *
     * @return the int value.
     */
    default int castToIntValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Checks doubles and floats for NaN.
     *
     * @return true if NaN, false if not NaN.
     */
    default boolean isNaN() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the mutability level of the item.
     *
     * @return an int representing nestedness of the item inside transform expressions.
     */
    default int getMutabilityLevel() {
        return -1;
    }

    /**
     * Sets the mutability level of the item to a supplied value.
     *
     * @param mutabilityLevel new mutability level.
     */
    default void setMutabilityLevel(int mutabilityLevel) {
    }

    /**
     * Returns the top level ID of the item.
     *
     * @return int representing the rowID of the item within a DeltaFile.
     */
    default long getTopLevelID() {
        return -1;
    }

    /**
     * Sets the top level ID of the item to a supplied value.
     *
     * @param topLevelID new top level ID.
     */
    default void setTopLevelID(long topLevelID) {
    }

    /**
     * Returns the path from the top level object of a DeltaFile for the item.
     *
     * @return String representing the path of the item from the top level within a DeltaFile.
     */
    default String getPathIn() {
        return "null";
    }

    /**
     * Sets the path from the top level object of a DeltaFile for the item to a supplied value.
     *
     * @param pathIn new path from top level.
     */
    default void setPathIn(String pathIn) {
    }

    /**
     * Returns the location of the DeltaFile for the item.
     *
     * @return String representing the location of the DeltaFile for the item.
     */
    default String getTableLocation() {
        return null;
    }


    /**
     * Sets the location of the DeltaFile for the item to a supplied value.
     *
     * @param location new location of the DeltaFile for the item.
     */
    default void setTableLocation(String location) {
    }

    /**
     * Returns the SparkSQL value of the item for use in a query.
     *
     * @return String representing the SparkSQL value of the item.
     */
    default String getSparkSQLValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the SparkSQL value of the item for use in a query.
     *
     * @return String representing the SparkSQL value of the item.
     */
    default String getSparkSQLValue(ItemType itemType) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the SparkSQL type of the item for use in a query.
     *
     * @return String representing the SparkSQL type of the item.
     */
    default String getSparkSQLType() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Tests for physical equality. The semantics are that of the eq operator.
     *
     * @param other another item.
     * @return true it is equal to other, false otherwise.
     */
    default boolean physicalEquals(Object other) {
        if (!(other instanceof Item)) {
            return false;
        }
        Item otherItem = (Item) other;
        if (this.getTopLevelID() == -1 || otherItem.getTopLevelID() == -1) {
            return System.identityHashCode(this) == System.identityHashCode(otherItem);
        }
        return this.getTopLevelID() == otherItem.getTopLevelID() && this.getPathIn().equals(otherItem.getPathIn());
    }

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

    default String serialize() {
        return new Serializer("UTF-8", Serializer.Method.XML_JSON_HYBRID, false, "\n").serialize(this);
    }

    /**
     * Get sparkSql string for the item
     * 
     * @param context input context
     * @return String representing the item in a sparksql query or null if it is not supported for the item
     */
    default NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return NativeClauseContext.NoNativeQuery;
    }

    default boolean isEstimator() {
        return false;
    }

    default Estimator<?> getEstimator() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default boolean isTransformer() {
        return false;
    }

    default Transformer getTransformer() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Returns the string value of the text item, if it is a text item.
     *
     * @return the string value.
     */
    default String getTextValue() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Method sets the parent item for all descendents of the current item.
     */
    default void addParentToDescendants() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default List<Item> attributes() {
        return new ArrayList<>();
    }

    default List<Item> children() {
        return new ArrayList<>();
    }

    default String nodeName() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default Item parent() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default List<Item> atomizedValue() {
        if (isAtomic())
            return Collections.singletonList(this);
        else
            throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    default void setParent(Item parent) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }


    /**
     * Get the position of the Node inside the XML document (and path incase of multiple docs) for sorting /
     * uniqueness
     * 
     * @return the XML document position
     */
    default XMLDocumentPosition getXmlDocumentPosition() {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }

    /**
     * Set the position of the Node inside the XML document (and path incase of multiple docs) for sorting /
     * uniqueness
     * 
     * @param path the path of the XML document
     * @param current the current position
     * @return the new position
     */
    default int setXmlDocumentPosition(String path, int current) {
        throw new UnsupportedOperationException("Operation not defined for type " + this.getDynamicType());
    }
}
