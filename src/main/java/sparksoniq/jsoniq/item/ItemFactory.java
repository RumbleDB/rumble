package sparksoniq.jsoniq.item;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.rumbledb.api.Item;

import org.rumbledb.exceptions.ExceptionMetadata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ItemFactory {

    private static ItemFactory _instance;
    private Item _nullItem;
    private Item _trueBooleanItem;
    private Item _falseBooleanItem;

    public static ItemFactory getInstance() {
        if (_instance == null) {
            _instance = new ItemFactory();
            _instance._nullItem = new NullItem();
            _instance._trueBooleanItem = new BooleanItem(true);
            _instance._falseBooleanItem = new BooleanItem(false);
        }
        return _instance;
    }

    public Item createStringItem(String s) {
        return new StringItem(s);
    }

    public Item createBooleanItem(boolean b) {
        return b ? _trueBooleanItem : _falseBooleanItem;
    }

    public Item createNullItem() {
        return _nullItem;
    }

    public Item createIntegerItem(int i) {
        return new IntegerItem(i);
    }

    public Item createDecimalItem(BigDecimal d) {
        return new DecimalItem(d);
    }

    public Item createDoubleItem(double d) {
        return new DoubleItem(d);
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

}
