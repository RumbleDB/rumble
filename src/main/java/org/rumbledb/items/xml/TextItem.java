package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

public class TextItem implements Item {
    private static final long serialVersionUID = 1L;
    private String content; // is also typed-value
    private Item parent;
    private XMLDocumentPosition documentPos;

    // needed for kryo
    public TextItem() {
    }

    public TextItem(Node textNode) {
        this.content = textNode.getTextContent();
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
        return this.getXmlDocumentPosition().equals(otherTextItem.getXmlDocumentPosition());
    }

    @Override
    public String getTextValue() {
        return this.content;
    }

    public boolean getEffectiveBooleanValue() {
        return !this.content.isEmpty();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        output.writeString(this.content);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.content = input.readString();
    }

    public int hashCode() {
        return this.documentPos.hashCode();
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
    public String getStringValue() {
        return this.content;
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
    public boolean isNode() {
        return true;
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
    public List<Item> atomizedValue() {
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.content));
    }
}
