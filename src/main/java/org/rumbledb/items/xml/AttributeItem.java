package org.rumbledb.items.xml;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;
import org.rumbledb.items.ItemFactory;
import org.rumbledb.runtime.typing.CastIterator;
import org.rumbledb.runtime.xml.NamespaceBindingUtils;
import org.rumbledb.types.ItemType;
import org.rumbledb.types.ItemTypeFactory;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.List;

public class AttributeItem implements Item {
    private static final long serialVersionUID = 1L;
    private Name dmNodeName;
    private String stringValue;
    private Item parent;
    private XMLDocumentPosition documentPos;
    private ItemType typeAnnotation;
    // TODO: add is-id, is-idrefs

    // needed for kryo
    public AttributeItem() {
    }

    public AttributeItem(Node attributeNode) {
        this.dmNodeName = NamespaceBindingUtils.nameFromElementOrAttributeDomNode(attributeNode);
        this.stringValue = attributeNode.getNodeValue();
        this.typeAnnotation = null;
    }

    public AttributeItem(Name dmNodeName, String stringValue) {
        this.dmNodeName = dmNodeName;
        this.stringValue = stringValue;
        this.typeAnnotation = null;
    }

    @Override
    public Item copy(boolean mutable) {
        AttributeItem copy = new AttributeItem(this.dmNodeName, this.stringValue);
        copy.typeAnnotation = this.typeAnnotation;
        return copy;
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
        kryo.writeObject(output, this.dmNodeName);
        output.writeString(this.stringValue);
        kryo.writeClassAndObject(output, this.typeAnnotation);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.documentPos = kryo.readObject(input, XMLDocumentPosition.class);
        this.parent = (Item) kryo.readClassAndObject(input);
        this.dmNodeName = kryo.readObject(input, Name.class);
        this.stringValue = input.readString();
        this.typeAnnotation = (ItemType) kryo.readClassAndObject(input);
    }

    @Override
    public Name nodeName() {
        return this.dmNodeName;
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

    public void setNodeName(Name nodeName) {
        this.dmNodeName = nodeName;
    }

    @Override
    public void addParentToDescendants() {
        // Attribute nodes are leaves and therefore have no descendants to update.
    }

    @Override
    public ItemType getDynamicType() {
        return ItemTypeFactory.attributeNodeItemType(this.dmNodeName);
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
        if (this == other) {
            return true;
        }
        if (!(other instanceof AttributeItem otherAttributeItem)) {
            return false;
        }
        if (this.documentPos == null || otherAttributeItem.documentPos == null) {
            return false;
        }
        return this.documentPos.equals(otherAttributeItem.documentPos);
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
        if (this.documentPos == null) {
            return System.identityHashCode(this);
        }
        return this.documentPos.hashCode();
    }

    @Override
    public List<Item> atomizedValue() {
        if (this.typeAnnotation != null) {
            Item typedValue = CastIterator.castItemToType(
                ItemFactory.getInstance().createUntypedAtomicItem(this.stringValue),
                this.typeAnnotation,
                org.rumbledb.exceptions.ExceptionMetadata.EMPTY_METADATA
            );
            return Collections.singletonList(typedValue);
        }
        return Collections.singletonList(ItemFactory.getInstance().createUntypedAtomicItem(this.stringValue));
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
        if (this.typeAnnotation == null || !this.typeAnnotation.hasName()) {
            return Collections.emptyList();
        }
        return Collections.singletonList(
            ItemFactory.getInstance().createQNameItem(this.typeAnnotation.getName())
        );
    }

    public void setSchemaType(ItemType typeAnnotation) {
        this.typeAnnotation = typeAnnotation;
    }

    @Override
    public ItemType getSchemaType() {
        return this.typeAnnotation;
    }
}
