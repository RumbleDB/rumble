package org.rumbledb.items;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class UserDefinedItem implements Item {

    private static final long serialVersionUID = 1L;

    private Item item;
    private ItemType type;

    public UserDefinedItem() {
        super();
    }

    public UserDefinedItem(Item item, ItemType type) {
        this.item = item;
        this.type = type;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeClassAndObject(output, this.item);
        kryo.writeClassAndObject(output, this.type);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.item = (Item) kryo.readClassAndObject(input);
        this.type = (ItemType) kryo.readClassAndObject(input);
    }

    @Override
    public String serialize() {
        return this.item.serialize();
    }

    @Override
    public boolean isArray() {
        return this.item.isArray();
    }

    @Override
    public boolean isObject() {

        return this.item.isObject();
    }

    @Override
    public boolean isFunction() {

        return this.item.isFunction();
    }

    @Override
    public boolean isAtomic() {

        return this.item.isAtomic();
    }

    @Override
    public boolean isString() {

        return this.item.isString();
    }

    @Override
    public boolean isBoolean() {

        return this.item.isBoolean();
    }

    @Override
    public boolean isNull() {

        return this.item.isNull();
    }

    @Override
    public boolean isNumeric() {

        return this.item.isNumeric();
    }

    @Override
    public boolean isDecimal() {

        return this.item.isDecimal();
    }

    @Override
    public boolean isInteger() {

        return this.item.isInteger();
    }

    @Override
    public boolean isInt() {

        return this.item.isInt();
    }

    @Override
    public boolean isDouble() {

        return this.item.isDouble();
    }

    @Override
    public boolean isFloat() {

        return this.item.isFloat();
    }

    @Override
    public boolean isDuration() {

        return this.item.isDuration();
    }

    @Override
    public boolean isYearMonthDuration() {

        return this.item.isYearMonthDuration();
    }

    @Override
    public boolean isDayTimeDuration() {

        return this.item.isDayTimeDuration();
    }

    @Override
    public boolean isDateTime() {

        return this.item.isDateTime();
    }

    @Override
    public boolean isDate() {

        return this.item.isDate();
    }

    @Override
    public boolean isTime() {

        return this.item.isTime();
    }

    @Override
    public boolean isAnyURI() {

        return this.item.isAnyURI();
    }

    @Override
    public boolean isBinary() {

        return this.item.isBinary();
    }

    @Override
    public boolean isHexBinary() {

        return this.item.isHexBinary();
    }

    @Override
    public boolean isBase64Binary() {

        return this.item.isBase64Binary();
    }

    @Override
    public List<Item> getItems() {

        return this.item.getItems();
    }

    @Override
    public Item getItemAt(int position) {

        return this.item.getItemAt(position);
    }

    @Override
    public List<String> getKeys() {

        return this.item.getKeys();
    }

    @Override
    public List<Item> getValues() {

        return this.item.getValues();
    }

    @Override
    public Item getItemByKey(String key) {

        return this.item.getItemByKey(key);
    }

    @Override
    public int getSize() {

        return this.item.getSize();
    }

    @Override
    public String getStringValue() {

        return this.item.getStringValue();
    }

    @Override
    public boolean getBooleanValue() {

        return this.item.getBooleanValue();
    }

    @Override
    public double getDoubleValue() {

        return this.item.getDoubleValue();
    }

    @Override
    public float getFloatValue() {

        return this.item.getFloatValue();
    }

    @Override
    public int getIntValue() {

        return this.item.getIntValue();
    }

    @Override
    public BigInteger getIntegerValue() {

        return this.item.getIntegerValue();
    }

    @Override
    public BigDecimal getDecimalValue() {

        return this.item.getDecimalValue();
    }

    @Override
    public Period getDurationValue() {

        return this.item.getDurationValue();
    }

    @Override
    public DateTime getDateTimeValue() {

        return this.item.getDateTimeValue();
    }

    @Override
    public byte[] getBinaryValue() {

        return this.item.getBinaryValue();
    }

    @Override
    public ItemType getDynamicType() {

        return this.type;
    }

    @Override
    public FunctionIdentifier getIdentifier() {

        return this.item.getIdentifier();
    }

    @Override
    public List<Name> getParameterNames() {

        return this.item.getParameterNames();
    }

    @Override
    public FunctionSignature getSignature() {

        return this.item.getSignature();
    }

    @Override
    public RuntimeIterator getBodyIterator() {

        return this.item.getBodyIterator();
    }

    @Override
    public Map<Name, List<Item>> getLocalVariablesInClosure() {

        return this.item.getLocalVariablesInClosure();
    }

    @Override
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {

        return this.item.getRDDVariablesInClosure();
    }

    @Override
    public Map<Name, JSoundDataFrame> getDFVariablesInClosure() {

        return this.item.getDFVariablesInClosure();
    }

    @Override
    public DynamicContext getModuleDynamicContext() {

        return this.item.getModuleDynamicContext();
    }

    @Override
    public boolean hasTimeZone() {

        return this.item.hasTimeZone();
    }

    @Override
    public boolean hasDateTime() {

        return this.item.hasDateTime();
    }

    @Override
    public boolean getEffectiveBooleanValue() {

        return this.item.getEffectiveBooleanValue();
    }

    @Override
    public void putItem(Item item) {

        this.item.putItem(item);
    }

    @Override
    public void append(Item value) {

        this.item.append(value);
    }

    @Override
    public void putItemByKey(String key, Item value) {

        this.item.putItemByKey(key, value);
    }

    @Override
    public double castToDoubleValue() {

        return this.item.castToDoubleValue();
    }

    @Override
    public float castToFloatValue() {

        return this.item.castToFloatValue();
    }

    @Override
    public BigDecimal castToDecimalValue() {

        return this.item.castToDecimalValue();
    }

    @Override
    public BigInteger castToIntegerValue() {

        return this.item.castToIntegerValue();
    }

    @Override
    public int castToIntValue() {

        return this.item.castToIntValue();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {

        return this.item.generateNativeQuery(context);
    }

}
