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

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — node-kind.
     *
     * "For an Attribute Node, dm:node-kind returns the string \"attribute\"."
     */
    @Override
    public String nodeKind() {
        return "attribute";
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — children.
     *
     * "For an Attribute Node, dm:children returns the empty sequence."
     */
    @Override
    public List<Item> children() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — attributes.
     *
     * "For an Attribute Node, dm:attributes returns the empty sequence."
     */
    @Override
    public List<Item> attributes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — is-id.
     *
     * "For an Attribute Node, dm:is-id returns true if the attribute node is of type xs:ID or
     * is derived by restriction from xs:ID; otherwise it returns false."
     *
     * RumbleDB does not currently support schema type annotations on attributes, so this
     * implementation always returns false.
     */
    @Override
    public boolean isId() {
        return false;
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — is-idrefs.
     *
     * "For an Attribute Node, dm:is-idrefs returns true if the attribute node is of type
     * xs:IDREF or xs:IDREFS or is derived by restriction from one of these types; otherwise
     * it returns false."
     *
     * RumbleDB does not currently support schema type annotations on attributes, so this
     * implementation always returns false.
     */
    @Override
    public boolean isIdrefs() {
        return false;
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
        return Collections.emptyList();
    }

    @Override
    public List<Item> declaredNamespaceNodes() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — base-uri.
     *
     * For an Attribute Node, dm:base-uri returns the base URI of the parent element or document
     * node, if it has one; otherwise it returns the empty sequence.
     */
    @Override
    public List<Item> baseUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.baseUri();
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — document-uri.
     *
     * For an Attribute Node, dm:document-uri returns the document-uri of the document node that
     * is the root of the tree containing the attribute, if it has one; otherwise it returns the
     * empty sequence.
     */
    @Override
    public List<Item> documentUri() {
        if (this.parent == null) {
            return Collections.emptyList();
        }
        return this.parent.documentUri();
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — nilled.
     *
     * For an Attribute Node, dm:nilled returns the empty sequence.
     */
    @Override
    public List<Item> nilled() {
        return Collections.emptyList();
    }

    /**
     * XDM 3.1 Section 6.3 Attribute Node Accessors — type-name.
     *
     * For an Attribute Node, dm:type-name returns the name of the dynamic type of the attribute
     * node, or the empty sequence if the node is untyped.
     *
     * RumbleDB does not currently support schema-validated attribute types, so this
     * implementation returns the empty sequence.
     */
    @Override
    public List<Item> typeName() {
        return Collections.emptyList();
    }
}
