package sparksoniq.jsoniq.item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.rumbledb.api.Item;

import sparksoniq.jsoniq.item.metadata.ItemMetadata;

public class ItemFactory {

    private static ItemFactory _instance;
    private NullItem _nullItem;
    private BooleanItem _trueBooleanItem;
    private BooleanItem _falseBooleanItem;

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

    public StringItem createStringItem(String s) {
        return new StringItem(s);
    }

    public BooleanItem createBooleanItem(boolean b) {
        return b?_trueBooleanItem:_falseBooleanItem;
    }

    public NullItem createNullItem() {
        return _nullItem;
    }

    public IntegerItem createIntegerItem(int i) {
        return new IntegerItem(i);
    }

    public DecimalItem createDecimalItem(BigDecimal d) {
        return new DecimalItem(d);
    }

    public DoubleItem createDoubleItem(double d) {
        return new DoubleItem(d);
    }

    public ObjectItem createObjectItem() {
        return new ObjectItem();
    }

    public ArrayItem createArrayItem() {
        return new ArrayItem();
    }

    public ArrayItem createArrayItem(List<Item> items) {
        return new ArrayItem(items);
    }

    public ObjectItem createObjectItem(List<String> keys, List<Item> values, ItemMetadata itemMetadata)
    {
        return new ObjectItem(keys, values, itemMetadata);
    }

    public ObjectItem createObjectItem(Map<String, List<Item>> keyValuePairs)
    {
        return new ObjectItem(keyValuePairs);
    }

}
