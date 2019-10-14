package sparksoniq.jsoniq.item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.Period;
import org.rumbledb.api.Item;

import sparksoniq.jsoniq.item.metadata.ItemMetadata;

public class ItemFactory {

    private static ItemFactory _instance;
    private AtomicItem _nullItem;
    private AtomicItem _trueBooleanItem;
    private AtomicItem _falseBooleanItem;

    public static ItemFactory getInstance() {
        if(_instance == null)
        {
            _instance = new ItemFactory();
            _instance._nullItem = new NullItem();
            _instance._trueBooleanItem = new BooleanItem(true);
            _instance._falseBooleanItem = new BooleanItem(false);
        }
        return _instance;
    }

    public AtomicItem createStringItem(String s) {
        return new StringItem(s);
    }

    public AtomicItem createBooleanItem(boolean b) {
        return b?_trueBooleanItem:_falseBooleanItem;
    }

    public AtomicItem createNullItem() {
        return _nullItem;
    }

    public AtomicItem createIntegerItem(int i) {
        return new IntegerItem(i);
    }

    public AtomicItem createDecimalItem(BigDecimal d) {
        return new DecimalItem(d);
    }

    public AtomicItem createDoubleItem(double d) {
        return new DoubleItem(d);
    }

    public AtomicItem createDurationItem(Period p) { return new DurationItem(p); }

    public Item createObjectItem() {
        return new ObjectItem();
    }

    public Item createArrayItem() {
        return new ArrayItem();
    }

    public Item createArrayItem(List<Item> items) {
        return new ArrayItem(items);
    }

    public Item createObjectItem(List<String> keys, List<Item> values, ItemMetadata itemMetadata)
    {
        return new ObjectItem(keys, values, itemMetadata);
    }

    public Item createObjectItem(Map<String, List<Item>> keyValuePairs)
    {
        return new ObjectItem(keyValuePairs);
    }
}
