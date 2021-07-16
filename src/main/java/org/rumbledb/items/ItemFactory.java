package org.rumbledb.items;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private static ItemFactory instance;
    private Item nullItem;
    private Item trueBooleanItem;
    private Item falseBooleanItem;
    private Item zeroItem;
    private Item positiveInfinityDoubleItem;
    private Item negativeInfinityDoubleItem;
    private Item NaNDoubleItem;
    private Item positiveInfinityFloatItem;
    private Item negativeInfinityFloatItem;
    private Item NaNFloatItem;

    public static ItemFactory getInstance() {
        if (instance == null) {
            instance = new ItemFactory();
            instance.nullItem = new NullItem();
            instance.trueBooleanItem = new BooleanItem(true);
            instance.falseBooleanItem = new BooleanItem(false);
            instance.zeroItem = new IntItem(0);
            instance.positiveInfinityDoubleItem = new DoubleItem(Double.POSITIVE_INFINITY);
            instance.negativeInfinityDoubleItem = new DoubleItem(Double.NEGATIVE_INFINITY);
            instance.NaNDoubleItem = new DoubleItem(Double.NaN);
            instance.positiveInfinityFloatItem = new FloatItem(Float.POSITIVE_INFINITY);
            instance.negativeInfinityFloatItem = new FloatItem(Float.NEGATIVE_INFINITY);
            instance.NaNFloatItem = new FloatItem(Float.NaN);
        }
        return instance;
    }

    public Item createStringItem(String s) {
        return new StringItem(s);
    }

    public Item createBooleanItem(boolean b) {
        return b ? this.trueBooleanItem : this.falseBooleanItem;
    }

    public Item createNullItem() {
        return this.nullItem;
    }

    public Item createDecimalItem(BigDecimal d) {
        return new DecimalItem(d);
    }

    public Item createIntegerItem(BigInteger i) {
        return new IntegerItem(i);
    }

    public Item createIntItem(int i) {
        if (i == 0) {
            return this.zeroItem;
        }
        return new IntItem(i);
    }

    public Item createLongItem(long l) {
        if (l == 0) {
            return this.zeroItem;
        }
        if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE) {
            return new IntItem((int) l);
        }
        return new IntegerItem(BigInteger.valueOf(l));
    }

    public Item createIntegerItem(String lexicalValue) {
        if (lexicalValue.length() >= 10) {
            return new IntegerItem(new BigInteger(lexicalValue));
        }
        return new IntItem(Integer.parseInt(lexicalValue));
    }

    public Item createDoubleItem(double d) {
        if (d == Double.POSITIVE_INFINITY) {
            return this.positiveInfinityDoubleItem;
        }
        if (d == Double.NEGATIVE_INFINITY) {
            return this.negativeInfinityDoubleItem;
        }
        if (d == Double.NaN) {
            return this.NaNDoubleItem;
        }
        return new DoubleItem(d);
    }

    public Item createFloatItem(float d) {
        if (d == Float.POSITIVE_INFINITY) {
            return this.positiveInfinityFloatItem;
        }
        if (d == Float.NEGATIVE_INFINITY) {
            return this.negativeInfinityFloatItem;
        }
        if (d == Float.NaN) {
            return this.NaNFloatItem;
        }
        return new FloatItem(d);
    }

    public Item createAnnotatedItem(Item itemToAnnotate, ItemType type) {
        return new AnnotatedItem(itemToAnnotate, type);
    }

    public Item createDurationItem(Period p) {
        return new DurationItem(p);
    }

    public Item createYearMonthDurationItem(Period p) {
        return new YearMonthDurationItem(p);
    }

    public Item createDayTimeDurationItem(Period p) {
        return new DayTimeDurationItem(p);
    }

    public Item createDateTimeItem(DateTime dt, boolean hasTimeZone) {
        return new DateTimeItem(dt, hasTimeZone);
    }

    public Item createDateTimeItem(String s) {
        return new DateTimeItem(s);
    }

    public Item createDateTimeStampItem(DateTime dt, boolean checkTimezone) {
        return new DateTimeStampItem(dt, checkTimezone);
    }

    public Item createDateTimeStampItem(String s) {
        return new DateTimeStampItem(s);
    }

    public Item createDateItem(DateTime dt, boolean hasTimeZone) {
        return new DateItem(dt, hasTimeZone);
    }

    public Item createDateItem(String s) {
        return new DateItem(s);
    }

    public Item createTimeItem(DateTime dt, boolean hasTimeZone) {
        return new TimeItem(dt, hasTimeZone);
    }

    public Item createTimeItem(String s) {
        return new TimeItem(s);
    }

    public Item createGDayItem(String s) {
        return new gDayItem(s);
    }

    public Item createGMonthItem(String s) {
        return new gMonthItem(s);
    }

    public Item createGYearItem(String s) {
        return new gYearItem(s);
    }

    public Item createGMonthDayItem(String s) {
        return new gMonthDayItem(s);
    }

    public Item createGYearMonthItem(String s) {
        return new gYearMonthItem(s);
    }

    public Item createAnyURIItem(String s) {
        return new AnyURIItem(s);
    }

    public Item createHexBinaryItem(String s) {
        return new HexBinaryItem(s);
    }

    public Item createBase64BinaryItem(String s) {
        return new Base64BinaryItem(s);
    }

    public Item createObjectItem() {
        return new ObjectItem();
    }

    public Item createArrayItem() {
        return new ArrayItem();
    }

    public Item createArrayItem(List<Item> items) {
        return new ArrayItem(items);
    }

    public Item createObjectItem(List<String> keys, List<Item> values, ExceptionMetadata itemMetadata) {
        return new ObjectItem(keys, values, itemMetadata);
    }

    public Item createObjectItem(Map<String, List<Item>> keyValuePairs) {
        return new ObjectItem(keyValuePairs);
    }

    public Item createUserDefinedItem(Item item, ItemType type) {
        return new UserDefinedItem(item, type);
    }

}
