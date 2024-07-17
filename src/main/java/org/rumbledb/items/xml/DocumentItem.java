package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.List;

public class DocumentItem implements NodeItem {
    private List<Item> children;
    // TODO: add base-uri, document-uri, typed-value

    public DocumentItem(List<Item> children) {
        this.children = children;
    }

    @Override
    public void addParentToDescendants() {
        this.children.forEach(child -> ((NodeItem) child).setParent(this));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, children);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.children = kryo.readObject(input, ArrayList.class);
    }

    @Override
    public List<Item> children() {
        return this.children;
    }

    @Override
    public boolean nilled() {
        return false;
    }

    @Override
    public String nodeKind() {
        return "document";
    }

    @Override
    public Item parent() {
        return null;
    }

    @Override
    public String stringValue() {
        StringBuffer result = new StringBuffer();
        this.children.forEach(child -> {
            String childStringValue = child.getStringValue();
            if (!childStringValue.isEmpty()) {
                result.append(childStringValue);
            }
        });
        return result.toString();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }
}
