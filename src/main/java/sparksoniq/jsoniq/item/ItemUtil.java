package sparksoniq.jsoniq.item;

import java.util.List;

public class ItemUtil {


    public static boolean listContainsItem(List<Item> list, Item newItem) {
        for (Item i:list) {
            if (Item.compareItems(i, newItem) == 0) {
                return true;
            }
        }
        return false;
    }
}
