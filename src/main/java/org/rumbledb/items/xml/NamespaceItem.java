package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.types.BuiltinTypesCatalogue;
import org.rumbledb.types.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NamespaceItem implements Item {
    private static final long serialVersionUID = 1L;
    private String prefix;
    private String uri;
    private Item parent;
    private XMLDocumentPosition documentPos;

    // needed for kryo
    @SuppressWarnings("unused")
    public NamespaceItem() {
    }

    /**
     * Constructor for a namespace item.
     *
     * @param prefix The namespace prefix (possibly empty)
     * @param uri The namespace URI
     */
    public NamespaceItem(String prefix, String uri) {
        this.prefix = prefix == null ? "" : prefix;
        this.uri = uri;
    }


    /**
     * Each Namespace Node represents the binding of a namespace URI to a namespace prefix or to the default
     * namespace.
     * Namespaces have the following properties: prefix , possibly empty uri parent , possibly empty
     * A Namespace Node must not have the name xmlns nor the string-value http://www.w3.org/2000/xmlns/ .
     */

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
    public void addParentToDescendants() {
        // no descendants
        // Namespaces do not have descendants.
    }

    @Override
    public void write(Kryo kryo, Output output) {
        kryo.writeObject(output, this.documentPos);
        kryo.writeClassAndObject(output, this.parent);
        output.writeString(this.prefix);
        output.writeString(this.uri);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.prefix = input.readString();
        this.uri = input.readString();
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isNamespaceNode() {
        return true;
    }

    @Override
    public String nodeName() {
        // Spec: "dm: node-name If the prefix is not empty, returns an xs:QName with the value of the prefix property in
        // the
        // local-name and an empty namespace name, otherwise returns the empty sequence."
        // For practical purposes, we return the prefix (may be empty).
        return this.prefix;
    }

    /**
     * Returns the namespace prefix (possibly empty).
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Returns the namespace URI.
     */
    public String getUri() {
        return this.uri;
    }

    @Override
    public Item parent() {
        return this.parent;
    }

    @Override
    public String getStringValue() {
        // Spec: "dm: string-value Returns the value of the uri property."
        return this.uri;
    }

    @Override
    public void setParent(Item parent) {
        this.parent = parent;
    }

    @Override
    public ItemType getDynamicType() {
        return BuiltinTypesCatalogue.namespaceNode;
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — node-kind.
     *
     * "For a Namespace Node, dm:node-kind returns the string \"namespace\"."
     */
    @Override
    public String nodeKind() {
        return "namespace";
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof NamespaceItem)) {
            return false;
        }
        NamespaceItem otherNamespaceItem = (NamespaceItem) other;
        return this.getXmlDocumentPosition().equals(otherNamespaceItem.getXmlDocumentPosition());
    }

    @Override
    public int hashCode() {
        return this.documentPos.hashCode();
    }

    @Override
    public List<Item> atomizedValue() {
        // Spec: "dm: typed-value Returns the value of the uri property as an xs:string ."
        return Collections.singletonList(ItemFactory.getInstance().createStringItem(this.uri));
    }

    @Override
    public List<Item> namespaceNodes() {
        return Collections.emptyList();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — attributes.
     *
     * For a Namespace Node, dm:attributes returns the empty sequence.
     */
    @Override
    public List<Item> attributes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — children.
     *
     * For a Namespace Node, dm:children returns the empty sequence.
     */
    @Override
    public List<Item> children() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — base-uri.
     *
     * For a Namespace Node, dm:base-uri returns the base URI of the parent node, if it has one;
     * otherwise it returns the empty sequence.
     */
    @Override
    public List<Item> baseUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.baseUri();
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — document-uri.
     *
     * For a Namespace Node, dm:document-uri returns the document-uri of the document node that is
     * the root of the tree containing the namespace node, if it has one; otherwise it returns
     * the empty sequence.
     */
    @Override
    public List<Item> documentUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.documentUri();
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — is-id.
     *
     * For a Namespace Node, dm:is-id returns false.
     */
    @Override
    public boolean isId() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — is-idrefs.
     *
     * For a Namespace Node, dm:is-idrefs returns false.
     */
    @Override
    public boolean isIdrefs() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — nilled.
     *
     * For a Namespace Node, dm:nilled returns the empty sequence.
     */
    @Override
    public List<Item> nilled() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.4 Namespace Node Accessors — type-name.
     *
     * For a Namespace Node, dm:type-name returns the empty sequence.
     */
    @Override
    public List<Item> typeName() {
        return Collections.emptyList();
    }
}

