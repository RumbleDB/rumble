package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

public class AttributeItem implements NodeItem {
    private String nodeName;
    private String nodeValue;
    private Item parent;
    // TODO: add schema-type, typed-value, is-id, is-idrefs

    public AttributeItem(String nodeName, String nodeValue) {
        this.nodeName = nodeName;
        this.nodeValue = nodeValue;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.parent);
        kryo.writeObject(output, this.nodeName);
        kryo.writeObject(output, this.nodeValue);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObject(input, Item.class);
        this.nodeName = kryo.readObject(input, String.class);
        this.nodeValue = kryo.readObject(input, String.class);
    }

    @Override
    public boolean nilled() {
        return false;
    }

    @Override
    public String nodeKind() {
        return "attribute";
    }

    @Override
    public String nodeName() {
        return this.nodeName;
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public String stringValue() {
        return this.nodeValue;
    }

    @Override
    public void setParent(NodeItem parent) {
        this.parent = parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }
}
