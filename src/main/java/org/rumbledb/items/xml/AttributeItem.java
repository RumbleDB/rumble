package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

public class AttributeItem implements Item {
    private static final long serialVersionUID = 1L;
    private Node attributeNode;
    private Item parent;
    // TODO: add schema-type, typed-value, is-id, is-idrefs

    public AttributeItem(Node attributeNode) {
        this.attributeNode = attributeNode;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.parent);
        kryo.writeObject(output, this.attributeNode);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.parent = kryo.readObject(input, Item.class);
        this.attributeNode = kryo.readObject(input, Node.class);
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
        return this.attributeNode.getNodeName();
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public String stringValue() {
        return this.attributeNode.getNodeValue();
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AttributeItem)) {
            return false;
        }
        AttributeItem otherAttributeItem = (AttributeItem) other;
        return otherAttributeItem.attributeNode.isEqualNode(this.attributeNode);
    }

    @Override
    public boolean isAttributeNode() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.attributeNode.hashCode();
    }

    @Override
    public int compareXmlNode(Item otherNode) {
        int position = this.attributeNode.compareDocumentPosition(otherNode.getXmlNode());
        if ((position & Node.DOCUMENT_POSITION_FOLLOWING) > 0 || (position & Node.DOCUMENT_POSITION_CONTAINED_BY) > 0) {
            return -1;
        } else if (
            (position & Node.DOCUMENT_POSITION_PRECEDING) > 0 || (position & Node.DOCUMENT_POSITION_CONTAINS) > 0
        ) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Node getXmlNode() {
        return this.attributeNode;
    }
}
