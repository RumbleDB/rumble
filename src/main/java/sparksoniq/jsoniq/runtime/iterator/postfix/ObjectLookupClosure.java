package sparksoniq.jsoniq.runtime.iterator.postfix;

import sparksoniq.jsoniq.item.Item;
import org.apache.spark.api.java.function.Function;

public class ObjectLookupClosure implements Function<Item, Item> {
	private final String _key;

	public ObjectLookupClosure(String key) {
		_key = key;
	}
	
	public Item call(Item arg0) throws Exception {
    	return arg0.getItemByKey(_key);
    }
};
