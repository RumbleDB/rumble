package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

public class TextItem implements Item {
    private static final long serialVersionUID = 1L;
    private Node textNode;
    private Item parent;
    private XMLDocumentPosition documentPos;

    public TextItem(Node textNode) {
        this.textNode = textNode;
    }

    @Override
    public int setXmlDocumentPosition(String path, int current) {
        this.documentPos = new XMLDocumentPosition(path, current);
        return ++current;
    }

    @Override
    public XMLDocumentPosition getXmlDocumentPosition() {
        return this.documentPos;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextItem)) {
            return false;
        }
        TextItem otherTextItem = (TextItem) other;
        return otherTextItem.textNode.isEqualNode(this.textNode);
    }

    @Override
    public String getTextValue() {
        return this.textNode.getNodeValue();
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getTextValue().isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, documentPos);
        kryo.writeClassAndObject(output, this.textNode);
        kryo.writeClassAndObject(output, this.parent);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.textNode = (Node) kryo.readClassAndObject(input);
        this.parent = (Item) kryo.readClassAndObject(input);
    }

    public int hashCode() {
        return this.textNode.hashCode();
    }

    @Override
    public boolean nilled() {
        return false;
    }

    @Override
    public String nodeKind() {
        return "text";
    }

    @Override
    public String stringValue() {
        return this.textNode.getTextContent();
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public boolean isTextNode() {
        return true;
    }

    @Override
    public String nodeName() {
        return "";
    }

    @Override
    public int compareXmlNode(Item otherNode) {
        int position = this.textNode.compareDocumentPosition(otherNode.getXmlNode());
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
        return this.textNode;
    }

    @Override
    public Item typedValue() {
        return ItemFactory.getInstance().createStringItem(this.textNode.getNodeValue());
    }
}
