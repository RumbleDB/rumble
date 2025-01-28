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
    private String textNodeValue;
    private String textTextContent;
    private Item parent;
    private XMLDocumentPosition documentPos;

    public TextItem(Node textNode) {
        this.textNodeValue = textNode.getNodeValue();
        this.textTextContent = textNode.getTextContent();
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
        return otherTextItem.textNodeValue.equals(this.textNodeValue);
    }

    @Override
    public String getTextValue() {
        return this.textNodeValue;
    }

    public boolean getEffectiveBooleanValue() {
        return !this.getTextValue().isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        output.writeString(this.textNodeValue);
        output.writeString(this.textTextContent);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.textNodeValue = input.readString();
        this.textTextContent = input.readString();
    }

    public int hashCode() {
        return this.textNodeValue.hashCode();
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
        return this.textTextContent;
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
    public Item typedValue() {
        return ItemFactory.getInstance().createStringItem(this.textNodeValue);
    }
}
