package sparksoniq.jsoniq.item;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sparksoniq.jsoniq.item.metadata.ItemMetadata;

public class ItemFactory {
    
    private static ItemFactory _instance;
    
    public static ItemFactory getInstance() {
        if(_instance == null)
        {
            _instance = new ItemFactory();
        }
        return _instance;
    }
    
    public Item createStringItem(String s) {
        return new StringItem(s);
    }

    public Item createBooleanItem(boolean b) {
        return new BooleanItem(b);
    }

    public Item createNullItem() {
        return new NullItem();
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
