package org.rumbledb.serialization;

import org.rumbledb.api.Item;

public interface Serializer {
    String serialize(Item item);

    void serialize(Item item, StringBuffer sb, String indent, boolean isTopLevel);
}
