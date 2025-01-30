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

public class AttributeItem implements Item {
    private static final long serialVersionUID = 1L;
    private String attributeName;
    private String attributeValue;
    private Item parent;
    private XMLDocumentPosition documentPos;
    // TODO: add schema-type, typed-value, is-id, is-idrefs

    public AttributeItem(Node attributeNode) {
        this.attributeName = attributeNode.getNodeName();
        this.attributeValue = attributeNode.getNodeValue();
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
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        output.writeString(this.attributeName);
        output.writeString(this.attributeValue);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.attributeName = input.readString();
        this.attributeValue = input.readString();
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
        return this.attributeName;
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public String stringValue() {
        return this.attributeValue;
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
        return this.getXmlDocumentPosition().equals(otherAttributeItem.getXmlDocumentPosition());
    }

    @Override
    public boolean isAttributeNode() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.attributeValue.hashCode();
    }

    @Override
    public List<Item> typedValue() {
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.attributeValue));
    }
}
