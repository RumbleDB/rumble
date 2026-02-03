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
        this.children.stream()
            .filter(Item::isElementNode)
            .forEach(child -> ((ElementItem) child).inheritNamespacesFromParent());
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
     * In-scope namespaces spec excerpt (verbatim):
     * "Definition : The in-scope namespaces property of an element node is a set of namespace bindings, each of which
     * associates a namespace prefix with a URI.]"
     * "For a given element, one namespace binding may have an empty prefix; the URI of this namespace binding is the
     * default namespace within the scope of the element."
     * "XQuery does not support the namespace axis and does not represent namespace bindings in the form of nodes."
     * "However, where other specifications such as [XSLT and XQuery Serialization 3.1] refer to namespace nodes, these
     * nodes may be synthesized from the in-scope namespaces of an element node by interpreting each namespace binding
     * as a namespace node."
     */
    @Override
    public List<Item> namespaceNodes() {
        List<Item> namespaces = new ArrayList<>();
        if (this.namespaces == null) {
            return namespaces;
        }
        for (Map.Entry<String, String> entry : this.namespaces.entrySet()) {
            Item namespaceItem = ItemFactory.getInstance()
                .createXmlNamespaceNode(entry.getKey(), entry.getValue());
            namespaceItem.setParent(this);
            namespaces.add(namespaceItem);
        }
        return namespaces;
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

    public void inheritNamespacesFromParent() {
        if (!(this.parent instanceof ElementItem)) {
            return;
        }
        ElementItem parentElement = (ElementItem) this.parent;
        if (parentElement.namespaces == null) {
            return;
        }
        if (this.namespaces == null) {
            this.namespaces = new HashMap<>();
        }
        // this implements the overriding rule: "If the same namespace prefix is bound in both the parent and the
        // child element, the child element’s binding overrides the parent element’s binding."
        for (Map.Entry<String, String> entry : parentElement.namespaces.entrySet()) {
            this.namespaces.putIfAbsent(entry.getKey(), entry.getValue());
        }
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


