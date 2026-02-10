package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeItem implements Item {
    private static final long serialVersionUID = 1L;
    private String nodeName;
    private String stringValue;
    private Item parent;
    private XMLDocumentPosition documentPos;
    // TODO: add schema-type, typed-value, is-id, is-idrefs

    // needed for kryo
    public AttributeItem() {
    }

    public AttributeItem(Node attributeNode) {
        this.nodeName = attributeNode.getNodeName();
        this.stringValue = attributeNode.getNodeValue();
    }

    /**
     * Constructor for an attribute item.
     * 
     * @param nodeName The name of the attribute
     * @param stringValue The string value of the attribute
     */
    public AttributeItem(String nodeName, String stringValue) {
        this.nodeName = nodeName;
        this.stringValue = stringValue;
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
        output.writeString(this.nodeName);
        output.writeString(this.stringValue);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.nodeName = input.readString();
        this.stringValue = input.readString();
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
    public String getStringValue() {
        return this.stringValue;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.attributeNode;
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
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isAttributeNode() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.documentPos.hashCode();
    }

    @Override
    public List<Item> atomizedValue() {
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.stringValue));
    }

    @Override
    public List<Item> namespaceNodes() {
        return new ArrayList<>();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        return new ArrayList<>();
    }
}
