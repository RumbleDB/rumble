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

public class ElementItem implements Item {
    private static final long serialVersionUID = 1L;
    private List<Item> children;
    private List<Item> attributes;
    private String nodeName;
    private String stringValue;
    private Item parent;
    // TODO: add base-uri, schema-type, namespaces, is-id, is-idrefs
    private XMLDocumentPosition documentPos;

    // needed for kryo
    @SuppressWarnings("unused")
    public ElementItem() {
    }

    /**
     * Constructor for an element item.
     * 
     * @param nodeName The name of the element
     * @param children The children items of the element
     * @param attributes The attributes items of the element
     */
    public ElementItem(String nodeName, List<Item> children, List<Item> attributes) {
        this.nodeName = nodeName;
        this.children = children;
        this.attributes = attributes;
        // TODO: add support for attributes and children
        this.stringValue = "<" + nodeName + "/>";
    }

    public ElementItem(Node elementNode, List<Item> children, List<Item> attributes) {
        this.nodeName = elementNode.getNodeName();
        this.stringValue = elementNode.getTextContent();
        this.children = children;
        this.attributes = attributes;
    }

    @Override
    public int setXmlDocumentPosition(String path, int current) {
        this.documentPos = new XMLDocumentPosition(path, current);
        for (Item attribute : this.attributes) {
            current++;
            current = attribute.setXmlDocumentPosition(path, current);
        }
        for (Item child : this.children) {
            current++;
            current = child.setXmlDocumentPosition(path, current);
        }
        return ++current;
    }

    @Override
    public XMLDocumentPosition getXmlDocumentPosition() {
        return this.documentPos;
    }

    @Override
    public void addParentToDescendants() {
        this.children.forEach(child -> child.setParent(this));
        this.attributes.forEach(attribute -> attribute.setParent(this));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        kryo.writeObject(output, this.children);
        kryo.writeObject(output, this.attributes);
        output.writeString(this.nodeName);
        output.writeString(this.stringValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.children = kryo.readObject(input, ArrayList.class);
        this.attributes = kryo.readObject(input, ArrayList.class);
        this.nodeName = input.readString();
        this.stringValue = input.readString();
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isElementNode() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ElementItem)) {
            return false;
        }
        ElementItem otherElementItem = (ElementItem) other;
        return this.getXmlDocumentPosition().equals(otherElementItem.getXmlDocumentPosition());
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
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        return this.documentPos.hashCode();
    }

    @Override
    public List<Item> atomizedValue() {
        // Reference: https://www.w3.org/TR/xpath-functions-31/#func-data
        // If the item is a node, the typed value of the node is appended to the result sequence.
        // The typed value is a sequence of zero or more atomic values: specifically, the result of the dm:typed-value
        // accessor as defined in [XQuery and XPath Data Model (XDM) 3.1] (See Section 5.14 typed-value Accessor DM31).
        // TODO: implement this following the spec. Most importantly, implement the dm:typed-value accessor.
        // This naive implementation is enough for now
        StringBuilder stringValueBuilder = new StringBuilder();
        for (Item child : this.children) {
            stringValueBuilder.append(child.atomizedValue().get(0).getStringValue());
        }
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(stringValueBuilder.toString()));
    }

    @Override
    public boolean getEffectiveBooleanValue() {
        return true;
    }
}
