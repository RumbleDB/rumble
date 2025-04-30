package org.rumbledb.items;

import java.time.*;

import org.rumbledb.api.Item;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.xml.AttributeItem;
import org.rumbledb.items.xml.DocumentItem;
import org.rumbledb.items.xml.ElementItem;
import org.rumbledb.items.xml.TextItem;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private static ItemFactory instance;
    private Item nullItem;
    private Item emptyStringItem;
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
            instance.emptyStringItem = new StringItem("");
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
        if (s.equals("")) {
            return this.emptyStringItem;
        }
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

    public Item createDurationItem(Duration p) {
        return new DurationItem(p);
    }

    public Item createDurationItem(String p) {
        return new DurationItem(p);
    }

    public Item createYearMonthDurationItem(Period p) {
        return new YearMonthDurationItem(p);
    }

    public Item createYearMonthDurationItem(String p) {
        return new YearMonthDurationItem(p);
    }

    public Item createDayTimeDurationItem(Duration p) {
        return new DayTimeDurationItem(p);
    }

    public Item createDayTimeDurationItem(String p) {
        return new DayTimeDurationItem(p);
    }

    public Item createDateTimeItem(OffsetDateTime dt, boolean hasTimeZone) {
        return new DateTimeItem(dt, hasTimeZone);
    }

    public Item createDateTimeItem(String s) {
        return new DateTimeItem(s);
    }

    public Item createDateTimeStampItem(OffsetDateTime dt, boolean checkTimezone) {
        return new DateTimeStampItem(dt, checkTimezone);
    }

    public Item createDateTimeStampItem(String s) {
        return new DateTimeStampItem(s);
    }

    public Item createDateItem(OffsetDateTime dt, boolean hasTimeZone) {
        return new DateItem(dt, hasTimeZone);
    }

    public Item createDateItem(String s) {
        return new DateItem(s);
    }

    public Item createTimeItem(OffsetTime dt, boolean hasTimeZone) {
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

    public Item createLazyObjectItem() {
        return new LazyObjectItem();
    }

    public Item createArrayItem() {
        return new ArrayItem();
    }

    public Item createArrayItem(List<Item> items, boolean mutable) {
        Item result = new ArrayItem(items);
        if (mutable) {
            result.setMutabilityLevel(0);
        } else {
            result.setMutabilityLevel(-1);
        }
        return result;
    }

    public Item createObjectItem(
            List<String> keys,
            List<Item> values,
            ExceptionMetadata itemMetadata,
            boolean mutable
    ) {
        Item result = new ObjectItem(keys, values, itemMetadata);
        if (mutable) {
            result.setMutabilityLevel(0);
        } else {
            result.setMutabilityLevel(-1);
        }
        return result;
    }

    public Item createObjectItem(Map<String, List<Item>> keyValuePairs, boolean mutable) {
        Item result = new ObjectItem(keyValuePairs);
        if (mutable) {
            result.setMutabilityLevel(0);
        } else {
            result.setMutabilityLevel(-1);
        }
        return result;
    }

    public Item createXmlTextNode(Node currentNode) {
        return new TextItem(currentNode);
    }

    public Item createXmlAttributeNode(Node attribute) {
        return new AttributeItem(attribute);
    }

    public Item createXmlDocumentNode(Node documentNode, List<Item> children) {
        return new DocumentItem(documentNode, children);
    }

    public Item createXmlElementNode(Node elementNode, List<Item> children, List<Item> attributes) {
        return new ElementItem(elementNode, children, attributes);
    }
}
