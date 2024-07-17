package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.List;

public class ElementItem implements NodeItem {
    private List<Item> children;
    private List<Item> attributes;
    private String nodeName;
    private Item parent;
    // TODO: add base-uri, schema-type, namespaces, is-id, is-idrefs

    public ElementItem(List<Item> children, List<Item> attributes, String nodeName) {
        this.children = children;
        this.attributes = attributes;
        this.nodeName = nodeName;
    }

    @Override
    public void addParentToDescendants() {
        this.children.forEach(child -> ((NodeItem) child).setParent(this));
        this.attributes.forEach(attribute -> ((NodeItem) attribute).setParent(this));
    }

    private void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.children);
        kryo.writeObject(output, this.attributes);
        kryo.writeObject(output, this.parent);
        kryo.writeObject(output, this.nodeName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.children = kryo.readObject(input, ArrayList.class);
        this.attributes = kryo.readObject(input, ArrayList.class);
        this.parent = kryo.readObject(input, NodeItem.class);
        this.nodeName = kryo.readObject(input, String.class);
    }

    @Override
    public boolean isElement() {
        return true;
    }



    @Override
    public boolean equals(Object otherItem) {
        if (!(otherItem instanceof NodeItem)) {
            return false;
        }
        NodeItem o = (NodeItem) otherItem;
        if (!o.isElement()) {
            return false;
        }
        // TODO: Compare each element
        return true;
    }

    @Override
    public List<Item> attributes() {
        return this.attributes;
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
        return "element";
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

    @Override
    public void setParent(NodeItem parent) {
        this.parent = parent;
    }
}
