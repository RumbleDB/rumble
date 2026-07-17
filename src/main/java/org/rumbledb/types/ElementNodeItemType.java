package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.xml.XmlSchemaNodeProperties;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;

import java.util.Objects;
import java.util.Set;

/**
 * Class representing element() and element(QName) item types.
 *
 * Wildcard element() is represented with no node-name restriction.
 * element(QName) is represented with a concrete node-name restriction.
 */
public class ElementNodeItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name catalogueName;
    private Name nodeName;
    private XmlSchemaTypeConstraint schemaTypeConstraint;
    private boolean allowsNilled;

    public ElementNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("element");
        this.nodeName = null;
        this.schemaTypeConstraint = null;
    }

    public ElementNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Element node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeConstraint = null;
    }

    public ElementNodeItemType(Name nodeName, Name schemaTypeName, boolean allowsNilled) {
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeConstraint = new XmlSchemaTypeConstraint(schemaTypeName);
        this.allowsNilled = allowsNilled;
    }

    private boolean isWildcardElement() {
        return this.nodeName == null;
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.catalogueName, Name.class);
        kryo.writeObjectOrNull(output, this.nodeName, Name.class);
        kryo.writeObjectOrNull(output, this.schemaTypeConstraint, XmlSchemaTypeConstraint.class);
        output.writeBoolean(this.allowsNilled);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.catalogueName = kryo.readObjectOrNull(input, Name.class);
        this.nodeName = kryo.readObjectOrNull(input, Name.class);
        this.schemaTypeConstraint = kryo.readObjectOrNull(input, XmlSchemaTypeConstraint.class);
        this.allowsNilled = input.readBoolean();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType itemType) || !itemType.isNodeItemType()) {
            return false;
        }
        return isEqualTo(itemType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.catalogueName, this.nodeName, this.schemaTypeConstraint, this.allowsNilled);
    }

    @Override
    public boolean isEqualTo(ItemType otherType) {
        if (!(otherType instanceof ElementNodeItemType other)) {
            return false;
        }
        return Objects.equals(this.catalogueName, other.catalogueName)
            && Objects.equals(this.nodeName, other.nodeName)
            && Objects.equals(this.schemaTypeConstraint, other.schemaTypeConstraint)
            && this.allowsNilled == other.allowsNilled;
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException("Named element node item type has no builtin QName");
        }
        return this.catalogueName;
    }

    public Name getNodeName() {
        return this.nodeName;
    }

    public Name getSchemaTypeName() {
        return this.schemaTypeConstraint == null ? null : this.schemaTypeConstraint.getTypeName();
    }

    public boolean allowsNilled() {
        return this.allowsNilled;
    }

    public boolean matches(Item item) {
        if (!item.isElementNode() || (this.nodeName != null && !this.nodeName.equals(item.nodeName()))) {
            return false;
        }
        if (this.schemaTypeConstraint == null) {
            return true;
        }
        XmlSchemaNodeProperties properties = item.getXmlSchemaProperties();
        XmlSchemaTypeAnnotation annotation = properties.typeAnnotation() == null
            ? XmlSchemaTypeAnnotation.untypedElement()
            : properties.typeAnnotation();
        return this.schemaTypeConstraint.matches(annotation)
            && (this.allowsNilled || properties.nilled() != XmlSchemaNodeProperties.Nilled.TRUE);
    }

    @Override
    public boolean isNodeItemType() {
        return true;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.isUnionType()) {
            for (ItemType unionItemType : superType.getTypes()) {
                if (this.isSubtypeOf(unionItemType)) {
                    return true;
                }
            }
        }
        if (
            this.equals(superType)
                || superType.equals(BuiltinTypesCatalogue.item)
                || superType.equals(BuiltinTypesCatalogue.nodeItem)
        ) {
            return true;
        }
        if (!(superType instanceof ElementNodeItemType other)) {
            return false;
        }
        if (other.nodeName != null && !other.nodeName.equals(this.nodeName)) {
            return false;
        }
        if (other.schemaTypeConstraint == null) {
            return true;
        }
        return this.schemaTypeConstraint != null
            && this.schemaTypeConstraint.isSubtypeOf(other.schemaTypeConstraint)
            && (!this.allowsNilled || other.allowsNilled);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof ElementNodeItemType) {
            return BuiltinTypesCatalogue.elementNode;
        }
        ItemType current = this;
        ItemType o = other;
        while (o.getTypeTreeDepth() > current.getTypeTreeDepth()) {
            o = o.getBaseType();
        }
        while (o.getTypeTreeDepth() < current.getTypeTreeDepth()) {
            current = current.getBaseType();
        }
        while (!current.equals(o)) {
            current = current.getBaseType();
            o = o.getBaseType();
        }
        return current;
    }

    @Override
    public int getTypeTreeDepth() {
        return 2;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.nodeItem;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("element node item type does not support facets");
    }

    @Override
    public String toString() {
        if (this.schemaTypeConstraint != null) {
            return "element("
                + (this.nodeName == null ? "*" : this.nodeName)
                + ", "
                + this.schemaTypeConstraint.getTypeName()
                + (this.allowsNilled ? "?" : "")
                + ")";
        }
        if (isWildcardElement()) {
            return this.catalogueName.toString();
        }
        return "element(" + this.nodeName + ")";
    }

    @Override
    public boolean isResolved() {
        return this.schemaTypeConstraint == null || this.schemaTypeConstraint.isResolved();
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (this.schemaTypeConstraint != null) {
            this.schemaTypeConstraint.resolve(context, metadata);
        }
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (this.schemaTypeConstraint != null) {
            this.schemaTypeConstraint.resolve(context, metadata);
        }
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}
