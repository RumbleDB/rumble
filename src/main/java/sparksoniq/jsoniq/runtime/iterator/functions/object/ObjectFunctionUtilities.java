package sparksoniq.jsoniq.runtime.iterator.functions.object;

import sparksoniq.jsoniq.item.Item;
import sparksoniq.jsoniq.item.StringItem;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public class ObjectFunctionUtilities {

    public static boolean listHasDuplicateString(List<Item> list, StringItem newItem) {
        for (Item i : list) {
            try {
                if (i.getStringValue().equals(newItem.getStringValue())) {
                    return true;
                }
            } catch (OperationNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
