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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElementItem implements Item {
    private static final long serialVersionUID = 1L;
    private List<Item> children;
    private List<Item> attributes;
    private Map<String, String> namespaces;
    private String nodeName;
    private String stringValue;
    private Item parent;
    // TODO: add base-uri, schema-type, is-id, is-idrefs
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
        this.namespaces = new HashMap<>();
        // TODO: add support for attributes and children
        this.stringValue = "<" + nodeName + "/>";
    }

    public ElementItem(Node elementNode, List<Item> children, List<Item> attributes) {
        this.nodeName = elementNode.getNodeName();
        this.stringValue = elementNode.getTextContent();
        this.children = children;
        this.attributes = attributes;
        this.namespaces = new HashMap<>();
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
        kryo.writeObject(output, this.namespaces);
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
        this.namespaces = kryo.readObject(input, HashMap.class);
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

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — node-kind.
     *
     * "For an Element Node, dm:node-kind returns the string \"element\"."
     */
    @Override
    public String nodeKind() {
        return "element";
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — base-uri.
     *
     * "For an Element Node, dm:base-uri returns the base URI of the element node, if it has one;
     * otherwise it returns the empty sequence."
     *
     * RumbleDB does not currently track base URIs for element nodes, so this implementation
     * returns null to represent the empty sequence.
     */
    @Override
    public String baseUri() {
        return null;
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — document-uri.
     *
     * "For an Element Node, dm:document-uri returns the document URI of the document node
     * that is the root of the tree containing the element, if it has one; otherwise it
     * returns the empty sequence."
     *
     * This implementation delegates to the parent, if any, otherwise returns null.
     */
    @Override
    public String documentUri() {
        return this.parent == null ? null : this.parent.documentUri();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        if (this.namespaces == null || this.namespaces.isEmpty()) {
            return Collections.emptyList();
        }
        List<Item> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.namespaces.entrySet()) {
            Item namespaceItem = ItemFactory.getInstance()
                .createXmlNamespaceNode(entry.getKey(), entry.getValue());
            namespaceItem.setParent(this);
            result.add(namespaceItem);
        }
        return result;
    }

    @Override
    public List<Item> namespaceNodes() {
        /*
         * Note: we implement this iteratively, so that namespace node instances are instantiated only once, before
         * returning.
         * Recursion would instantiate namespace node instances for each ancestor element, resulting in a higher memory
         * footprint.
         * A LinkedHashMap is used so that:
         * - Insertion order is preserved for stable iteration.
         * - Later puts for the same prefix override earlier values.
         */
        LinkedHashMap<String, String> inScope = new LinkedHashMap<>();

        // Step 1: Parent chaining -- collect ancestor declared namespaces from root to direct parent.
        // Walk up the parent chain, collecting declared namespaces from each ancestor element.
        // We collect frames in child-to-root order, then replay root-to-child for correct override semantics.
        List<Map<String, String>> ancestorFrames = new ArrayList<>();
        Item current = this.parent;
        // optimization: we know that no other node types apart from element nodes can have namespaces
        // so we stop the iteration when we encounter a non-element node
        while (current != null && current.isElementNode()) {
            ancestorFrames.add(((ElementItem) current).namespaces);
            current = current.parent();
        }
        // Replay from root (last in the list) to direct parent (first in the list),
        // so that inner ancestors override outer ones for the same prefix.
        for (int i = ancestorFrames.size() - 1; i >= 0; i--) {
            inScope.putAll(ancestorFrames.get(i));
        }

        // Step 2: Current element's own declared namespaces override all inherited ones.
        inScope.putAll(this.namespaces);

        // Step 3: Create NamespaceItem nodes from the final in-scope map.
        List<Item> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : inScope.entrySet()) {
            Item namespaceItem = ItemFactory.getInstance()
                .createXmlNamespaceNode(entry.getKey(), entry.getValue());
            namespaceItem.setParent(this);
            result.add(namespaceItem);
        }
        return result;
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — is-id.
     *
     * "For an Element Node, dm:is-id returns false."
     */
    @Override
    public boolean isId() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — is-idrefs.
     *
     * "For an Element Node, dm:is-idrefs returns false."
     */
    @Override
    public boolean isIdrefs() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — nilled.
     *
     * "For an Element Node, dm:nilled returns true if the element is nilled, false if it is
     * not nilled, or the empty sequence if the concept of nilled does not apply."
     *
     * RumbleDB does not currently support XML Schema nilled elements, so this always
     * returns Boolean.FALSE.
     */
    @Override
    public Boolean nilled() {
        return Boolean.FALSE;
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
        return BuiltinTypesCatalogue.elementNode;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — type-name.
     *
     * "For an Element Node, dm:type-name returns the name of the dynamic type of the element
     * node, or the empty sequence if the node is untyped."
     *
     * RumbleDB does not currently support schema-validated element types, so the dynamic
     * type-name is not available and this method returns null to represent the empty sequence.
     */
    @Override
    public String typeName() {
        return null;
    }

    /**
     * XDM 3.1 Section 6.2 Element Node Accessors — typed-value.
     *
     * "For an Element Node, dm:typed-value returns the typed value of the element node as a
     * sequence of zero or more atomic values."
     *
     * This implementation delegates to atomizedValue(), which currently computes a
     * best-effort typed value by concatenating the atomized values of the element's
     * children in document order.
     */
    @Override
    public List<Item> typedValue() {
        return this.atomizedValue();
    }

    public void addOrReplaceNamespace(Item namespaceItem) {
        if (!(namespaceItem instanceof NamespaceItem)) {
            return;
        }
        NamespaceItem namespace = (NamespaceItem) namespaceItem;
        if (this.namespaces == null) {
            this.namespaces = new HashMap<>();
        }
        this.namespaces.put(namespace.getPrefix(), namespace.getUri());
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


