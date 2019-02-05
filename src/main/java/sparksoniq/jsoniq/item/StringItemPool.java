package sparksoniq.jsoniq.item;

import java.util.Map;
import java.util.HashMap;

public class StringItemPool {
    
    public StringItemPool() {
        _map = new HashMap<>();
    }
    
    public static StringItemPool getPool() {
        if(_pool == null)
        {
            _pool = new StringItemPool();
        }
        return _pool;
    }

    public static StringItem getStringItem(String string) {
        return getPool().getStringItemInternal(string);
    }

    protected StringItem getStringItemInternal(String string)
    {
        if(_map.containsKey(string))
        {
            return _map.get(string);
        }
        StringItem item = new StringItem(string);
        _map.put(string,  item);
        return item;
    }
    
    static Map<String, StringItem> _map;
    static StringItemPool _pool;
}
