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

public class DocumentItem implements Item {
    private static final long serialVersionUID = 1L;
    private String stringValue;
    private List<Item> children;
    private XMLDocumentPosition documentPos;
    // TODO: add base-uri, document-uri, typed-value

    // needed for kryo
    public DocumentItem() {
    }

    public DocumentItem(Node documentNode, List<Item> children) {
        this.stringValue = documentNode.getTextContent();
        this.children = children;
    }

    /**
     * Constructor for creating a document node with children items.
     * Used by document node constructors when no actual DOM node is available.
     * 
     * @param children the child nodes of the document
     */
    public DocumentItem(List<Item> children) {
        this.children = children;
        // Compute string value as concatenated text content of children in document order
        StringBuilder sb = new StringBuilder();
        computeStringValue(children, sb);
        this.stringValue = sb.toString();
    }

    /**
     * Recursively computes the string value by concatenating text node descendants in document order.
     */
    private void computeStringValue(List<Item> items, StringBuilder sb) {
        for (Item item : items) {
            if (item.isTextNode()) {
                sb.append(item.getStringValue());
            } else if (item.isElementNode() && item.children() != null) {
                computeStringValue(item.children(), sb);
            }
        }
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
        output.writeString(this.stringValue);
        kryo.writeObject(output, this.children);
        kryo.writeObject(output, this.documentPos);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void read(Kryo kryo, Input input) {
        this.stringValue = input.readString();
        this.children = kryo.readObject(input, ArrayList.class);
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
    }


    @Override
    public List<Item> children() {
        return this.children;
    }

    @Override
    public Item parent() {
        return null;
    }

    @Override
    public String getStringValue() {
        return this.stringValue;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.documentNode;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DocumentItem)) {
            return false;
        }
        DocumentItem otherDocumentItem = (DocumentItem) other;
        return this.getXmlDocumentPosition().equals(otherDocumentItem.getXmlDocumentPosition());
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isDocumentNode() {
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
