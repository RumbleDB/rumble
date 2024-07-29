package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class ElementItem implements Item {
    private List<Item> children;
    private List<Item> attributes;
    private Node elementNode;
    private Item parent;
    // TODO: add base-uri, schema-type, namespaces, is-id, is-idrefs

    public ElementItem(Node elementNode, List<Item> children, List<Item> attributes) {
        this.elementNode = elementNode;
        this.children = children;
        this.attributes = attributes;
    }

    @Override
    public void addParentToDescendants() {
        this.children.forEach(child -> child.setParent(this));
        this.attributes.forEach(attribute -> attribute.setParent(this));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.elementNode);
        kryo.writeObject(output, this.children);
        kryo.writeObject(output, this.attributes);
        kryo.writeObject(output, this.parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.elementNode = kryo.readObject(input, Node.class);
        this.children = kryo.readObject(input, ArrayList.class);
        this.attributes = kryo.readObject(input, ArrayList.class);
        this.parent = kryo.readObject(input, Item.class);
    }

    @Override
    public boolean isElement() {
        return true;
    }



    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ElementItem)) {
            return false;
        }
        ElementItem otherElementItem = (ElementItem) other;
        return otherElementItem.elementNode.isEqualNode(this.elementNode);
    }

    @Override
    public List<Item> attributes() {
        return this.attributes;
    }

    @Override
    public List<Item> children() {
        return this.children;
    }

    // TODO: may require more checks to comply with the specification using typing.
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
        return this.elementNode.getNodeName();
    }

    @Override
    public Item parent() {
        return this.parent;
    }


    @Override
    public String stringValue() {
        return this.elementNode.getTextContent();
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }
}
