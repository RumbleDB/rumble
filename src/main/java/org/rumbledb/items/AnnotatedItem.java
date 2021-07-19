package org.rumbledb.items;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.api.java.JavaRDD;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.comparison.ComparisonExpression;
import org.rumbledb.items.structured.JSoundDataFrame;
import org.rumbledb.runtime.RuntimeIterator;
import org.rumbledb.runtime.flwor.NativeClauseContext;
import org.rumbledb.runtime.misc.ComparisonIterator;
import org.rumbledb.types.FunctionSignature;
import org.rumbledb.types.ItemType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class AnnotatedItem implements Item {

    private static final long serialVersionUID = 1L;

    private Item itemToAnnotate;
    private ItemType type;

    public AnnotatedItem() {
        super();
    }

    public AnnotatedItem(Item itemToAnnotate, ItemType type) {
        this.itemToAnnotate = itemToAnnotate;
        this.type = type;
    }

    @Override
    public boolean equals(Object otherItem) {
        if (otherItem instanceof AnnotatedItem) {
            long c = ComparisonIterator.compareItems(
                this,
                (Item) otherItem,
                ComparisonExpression.ComparisonOperator.VC_EQ,
                ExceptionMetadata.EMPTY_METADATA
            );
            return c == 0;
        }
        return false;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        this.itemToAnnotate.write(kryo, output);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.itemToAnnotate.read(kryo, input);
    }

    @Override
    public boolean isArray() {
        return this.itemToAnnotate.isArray();
    }

    @Override
    public boolean isObject() {
        return this.itemToAnnotate.isObject();
    }

    @Override
    public boolean isFunction() {
        return this.itemToAnnotate.isFunction();
    }

    @Override
    public boolean isAtomic() {
        return this.itemToAnnotate.isAtomic();
    }

    @Override
    public boolean isString() {
        return this.itemToAnnotate.isString();
    }

    @Override
    public boolean isBoolean() {
        return this.itemToAnnotate.isBoolean();
    }

    @Override
    public boolean isNull() {
        return this.itemToAnnotate.isNull();
    }

    @Override
    public boolean isNumeric() {
        return this.itemToAnnotate.isNumeric();
    }

    @Override
    public boolean isDecimal() {
        return this.itemToAnnotate.isDecimal();
    }

    @Override
    public boolean isInteger() {
        return this.itemToAnnotate.isInteger();
    }

    @Override
    public boolean isInt() {
        return this.itemToAnnotate.isInt();
    }

    @Override
    public boolean isDouble() {
        return this.itemToAnnotate.isDouble();
    }

    @Override
    public boolean isFloat() {
        return this.itemToAnnotate.isFloat();
    }

    @Override
    public boolean isDuration() {
        return this.itemToAnnotate.isDuration();
    }

    @Override
    public boolean isYearMonthDuration() {
        return this.itemToAnnotate.isYearMonthDuration();
    }

    @Override
    public boolean isDayTimeDuration() {
        return this.itemToAnnotate.isDayTimeDuration();
    }

    @Override
    public boolean isDateTime() {
        return this.itemToAnnotate.isDateTime();
    }

    @Override
    public boolean isDate() {
        return this.itemToAnnotate.isDate();
    }

    @Override
    public boolean isTime() {
        return this.itemToAnnotate.isTime();
    }

    @Override
    public boolean isAnyURI() {
        return this.itemToAnnotate.isAnyURI();
    }

    @Override
    public boolean isBinary() {
        return this.itemToAnnotate.isBinary();
    }

    @Override
    public boolean isHexBinary() {
        return this.itemToAnnotate.isHexBinary();
    }

    @Override
    public boolean isBase64Binary() {
        return this.itemToAnnotate.isBase64Binary();
    }

    @Override
    public List<Item> getItems() {
        return this.itemToAnnotate.getItems();
    }

    @Override
    public Item getItemAt(int position) {
        return this.itemToAnnotate.getItemAt(position);
    }

    @Override
    public List<String> getKeys() {
        return this.itemToAnnotate.getKeys();
    }

    @Override
    public List<Item> getValues() {
        return this.itemToAnnotate.getValues();
    }

    @Override
    public Item getItemByKey(String key) {
        return this.itemToAnnotate.getItemByKey(key);
    }

    @Override
    public int getSize() {
        return this.itemToAnnotate.getSize();
    }

    @Override
    public String getStringValue() {
        return this.itemToAnnotate.getStringValue();
    }

    @Override
    public boolean getBooleanValue() {
        return this.itemToAnnotate.getBooleanValue();
    }

    @Override
    public double getDoubleValue() {
        return this.itemToAnnotate.getDoubleValue();
    }

    @Override
    public float getFloatValue() {
        return this.itemToAnnotate.getFloatValue();
    }

    @Override
    public int getIntValue() {
        return this.itemToAnnotate.getIntValue();
    }

    @Override
    public BigInteger getIntegerValue() {
        return this.itemToAnnotate.getIntegerValue();
    }

    @Override
    public BigDecimal getDecimalValue() {
        return this.itemToAnnotate.getDecimalValue();
    }

    @Override
    public Period getDurationValue() {
        return this.itemToAnnotate.getDurationValue();
    }

    @Override
    public DateTime getDateTimeValue() {
        return this.itemToAnnotate.getDateTimeValue();
    }

    @Override
    public byte[] getBinaryValue() {
        return this.itemToAnnotate.getBinaryValue();
    }

    @Override
    public ItemType getDynamicType() {
        return this.type;
    }

    @Override
    public FunctionIdentifier getIdentifier() {
        return this.itemToAnnotate.getIdentifier();
    }

    @Override
    public List<Name> getParameterNames() {
        return this.itemToAnnotate.getParameterNames();
    }

    @Override
    public FunctionSignature getSignature() {
        return this.itemToAnnotate.getSignature();
    }

    @Override
    public RuntimeIterator getBodyIterator() {
        return this.itemToAnnotate.getBodyIterator();
    }

    @Override
    public Map<Name, List<Item>> getLocalVariablesInClosure() {
        return this.itemToAnnotate.getLocalVariablesInClosure();
    }

    @Override
    public Map<Name, JavaRDD<Item>> getRDDVariablesInClosure() {
        return this.itemToAnnotate.getRDDVariablesInClosure();
    }

    @Override
    public Map<Name, JSoundDataFrame> getDFVariablesInClosure() {
        return this.itemToAnnotate.getDFVariablesInClosure();
    }

    @Override
    public DynamicContext getModuleDynamicContext() {
        return this.itemToAnnotate.getModuleDynamicContext();
    }

    @Override
    public boolean hasTimeZone() {
        return this.itemToAnnotate.hasTimeZone();
    }

    @Override
    public boolean hasDateTime() {
        return this.itemToAnnotate.hasDateTime();
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return this.itemToAnnotate.getEffectiveBooleanValue();
    }

    @Override
    public void putItem(Item item) {
        this.itemToAnnotate.putItem(item);
    }

    @Override
    public void append(Item value) {
        this.itemToAnnotate.append(value);
    }

    @Override
    public void putItemByKey(String key, Item value) {
        this.itemToAnnotate.putItemByKey(key, value);
    }

    @Override
    public double castToDoubleValue() {
        return this.itemToAnnotate.castToDoubleValue();
    }

    @Override
    public float castToFloatValue() {
        return this.itemToAnnotate.castToFloatValue();
    }

    @Override
    public BigDecimal castToDecimalValue() {
        return this.itemToAnnotate.castToDecimalValue();
    }

    @Override
    public BigInteger castToIntegerValue() {
        return this.itemToAnnotate.castToIntegerValue();
    }

    @Override
    public int castToIntValue() {
        return this.itemToAnnotate.castToIntValue();
    }

    @Override
    public String serialize() {
        return this.itemToAnnotate.serialize();
    }

    @Override
    public NativeClauseContext generateNativeQuery(NativeClauseContext context) {
        return this.itemToAnnotate.generateNativeQuery(context);
    }
}
