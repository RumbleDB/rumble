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

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — node-kind.
     *
     * "For a Document Node, dm:node-kind returns the string \"document\"."
     */
    @Override
    public String nodeKind() {
        return "document";
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — node-name.
     *
     * "For a Document Node, dm:node-name returns the empty sequence."
     *
     * An empty string is used here to represent the empty sequence.
     */
    @Override
    public String nodeName() {
        return "";
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — attributes.
     *
     * "For a Document Node, dm:attributes returns the empty sequence."
     */
    @Override
    public List<Item> attributes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — base-uri.
     *
     * "For a Document Node, dm:base-uri returns the base URI of the document node, if it has
     * one; otherwise it returns the empty sequence."
     *
     * RumbleDB does not currently track base URIs for document nodes, so this implementation
     * returns null to represent the empty sequence.
     */
    @Override
    public List<Item> baseUri() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — document-uri.
     *
     * "For a Document Node, dm:document-uri returns the document-uri property of the document
     * node, if it has one; otherwise it returns the empty sequence."
     *
     * RumbleDB does not currently track document URIs, so this implementation returns null
     * to represent the empty sequence.
     */
    @Override
    public List<Item> documentUri() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — is-id.
     *
     * For a Document Node, dm:is-id returns false.
     */
    @Override
    public boolean isId() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — is-idrefs.
     *
     * For a Document Node, dm:is-idrefs returns false.
     */
    @Override
    public boolean isIdrefs() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — nilled.
     *
     * For a Document Node, dm:nilled returns the empty sequence.
     */
    @Override
    public List<Item> nilled() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — type-name.
     *
     * For a Document Node, dm:type-name returns the empty sequence.
     */
    @Override
    public List<Item> typeName() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — typed-value.
     *
     * For a Document Node, dm:typed-value returns the typed value of the document node as a
     * sequence of zero or more atomic values.
     *
     * This implementation delegates to atomizedValue().
     */
    @Override
    public List<Item> typedValue() {
        return this.atomizedValue();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — unparsed-entity-public-id.
     *
     * For a Document Node, dm:unparsed-entity-public-id returns the public identifier of an
     * unparsed entity with a given name in the document, or the empty sequence if there is
     * no such entity or if it has no public identifier.
     *
     * RumbleDB does not currently support unparsed entities, so this implementation always
     * returns the empty sequence.
     */
    @Override
    public List<Item> unparsedEntityPublicId(String name) {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.1 Document Node Accessors — unparsed-entity-system-id.
     *
     * For a Document Node, dm:unparsed-entity-system-id returns the system identifier of an
     * unparsed entity with a given name in the document, or the empty sequence if there is
     * no such entity or if it has no system identifier.
     *
     * RumbleDB does not currently support unparsed entities, so this implementation always
     * returns the empty sequence.
     */
    @Override
    public List<Item> unparsedEntitySystemId(String name) {
        return Collections.emptyList();
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
        return Collections.emptyList();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        return Collections.emptyList();
    }
}
