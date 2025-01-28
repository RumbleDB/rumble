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
import java.util.List;

public class DocumentItem implements Item {
    private static final long serialVersionUID = 1L;
    private String documentNodeValue;
    private String documentTextContent;
    private List<Item> children;
    private XMLDocumentPosition documentPos;
    // TODO: add base-uri, document-uri, typed-value

    public DocumentItem(Node documentNode, List<Item> children) {
        this.documentNodeValue = documentNode.getNodeValue();
        this.documentTextContent = documentNode.getTextContent();
        this.children = children;
    }

    @Override
    public int setXmlDocumentPosition(String path, int current) {
        this.documentPos = new XMLDocumentPosition(path, current);
        current++;
        for (Item child : this.children)
            current = child.setXmlDocumentPosition(path, current);
        return current;
    }

    @Override
    public XMLDocumentPosition getXmlDocumentPosition() {
        return this.documentPos;
    }

    @Override
    public void addParentToDescendants() {
        this.children.forEach(child -> child.setParent(this));
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(this.documentNodeValue);
        output.writeString(this.documentTextContent);
        kryo.writeObject(output, this.children);
        kryo.writeObject(output, this.documentPos);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.documentNodeValue = input.readString();
        this.documentTextContent = input.readString();
        this.children = kryo.readObject(input, ArrayList.class);
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
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
        return this.documentTextContent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DocumentItem)) {
            return false;
        }
        DocumentItem otherDocumentItem = (DocumentItem) other;
        return otherDocumentItem.documentNodeValue.equals(this.documentNodeValue);
    }

    @Override
    public boolean isDocumentNode() {
        return true;
    }

    @Override
    public int hashCode() {
        return this.documentNodeValue.hashCode();
    }

    @Override
    public Item typedValue() {
        return ItemFactory.getInstance().createStringItem(this.documentNodeValue);
    }
}
