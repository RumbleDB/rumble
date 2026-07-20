package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.items.xml.XmlSchemaTypeAnnotation;

import java.util.Objects;
import java.util.Set;

/**
 * Class representing attribute() and attribute(QName) item types.
 *
 * Wildcard attribute() is represented with no node-name restriction.
 * attribute(QName) is represented with a concrete node-name restriction.
 */
public class AttributeNodeItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name catalogueName;
    private Name nodeName;
    private XmlSchemaTypeConstraint schemaTypeConstraint;
    private XmlSchemaDeclarationConstraint schemaDeclarationConstraint;

    public AttributeNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("attribute");
        this.nodeName = null;
        this.schemaTypeConstraint = null;
    }

    public AttributeNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Attribute node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeConstraint = null;
    }

    public AttributeNodeItemType(Name nodeName, Name schemaTypeName) {
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeConstraint = new XmlSchemaTypeConstraint(schemaTypeName);
    }

    public static AttributeNodeItemType schemaAttribute(Name declarationName) {
        AttributeNodeItemType result = new AttributeNodeItemType();
        result.catalogueName = null;
        result.schemaDeclarationConstraint = new XmlSchemaDeclarationConstraint(
                XmlSchemaDeclarationConstraint.Kind.ATTRIBUTE,
                declarationName
        );
        return result;
    }

    private boolean isWildcardAttribute() {
        return this.nodeName == null;
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.catalogueName, Name.class);
        kryo.writeObjectOrNull(output, this.nodeName, Name.class);
        kryo.writeObjectOrNull(output, this.schemaTypeConstraint, XmlSchemaTypeConstraint.class);
        kryo.writeObjectOrNull(
            output,
            this.schemaDeclarationConstraint,
            XmlSchemaDeclarationConstraint.class
        );
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.catalogueName = kryo.readObjectOrNull(input, Name.class);
        this.nodeName = kryo.readObjectOrNull(input, Name.class);
        this.schemaTypeConstraint = kryo.readObjectOrNull(input, XmlSchemaTypeConstraint.class);
        this.schemaDeclarationConstraint = kryo.readObjectOrNull(
            input,
            XmlSchemaDeclarationConstraint.class
        );
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
        return Objects.hash(
            this.catalogueName,
            this.nodeName,
            this.schemaTypeConstraint,
            this.schemaDeclarationConstraint
        );
    }

    @Override
    public boolean isEqualTo(ItemType otherType) {
        if (!(otherType instanceof AttributeNodeItemType other)) {
            return false;
        }
        return Objects.equals(this.catalogueName, other.catalogueName)
            && Objects.equals(this.nodeName, other.nodeName)
            && Objects.equals(this.schemaTypeConstraint, other.schemaTypeConstraint)
            && Objects.equals(this.schemaDeclarationConstraint, other.schemaDeclarationConstraint);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException("Named attribute node item type has no builtin QName");
        }
        return this.catalogueName;
    }

    public Name getNodeName() {
        return this.nodeName;
    }

    public Name getSchemaTypeName() {
        return this.schemaTypeConstraint == null ? null : this.schemaTypeConstraint.getTypeName();
    }

    public Name getSchemaDeclarationName() {
        return this.schemaDeclarationConstraint == null
            ? null
            : this.schemaDeclarationConstraint.getDeclarationName();
    }

    public boolean matches(Item item) {
        if (this.schemaDeclarationConstraint != null) {
            return this.schemaDeclarationConstraint.matches(item);
        }
        if (!item.isAttributeNode() || (this.nodeName != null && !this.nodeName.equals(item.nodeName()))) {
            return false;
        }
        if (this.schemaTypeConstraint == null) {
            return true;
        }
        XmlSchemaTypeAnnotation annotation = item.getXmlSchemaProperties().typeAnnotation() == null
            ? XmlSchemaTypeAnnotation.untypedAttribute()
            : item.getXmlSchemaProperties().typeAnnotation();
        return this.schemaTypeConstraint.matches(annotation);
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
        if (!(superType instanceof AttributeNodeItemType other)) {
            return false;
        }
        if (other.schemaDeclarationConstraint != null) {
            return this.schemaDeclarationConstraint != null
                && this.schemaDeclarationConstraint.isSubtypeOf(other.schemaDeclarationConstraint);
        }
        if (this.schemaDeclarationConstraint != null) {
            return other.nodeName == null && other.schemaTypeConstraint == null;
        }
        if (other.nodeName != null && !other.nodeName.equals(this.nodeName)) {
            return false;
        }
        if (other.schemaTypeConstraint == null) {
            return true;
        }
        return this.schemaTypeConstraint != null && this.schemaTypeConstraint.isSubtypeOf(other.schemaTypeConstraint);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof AttributeNodeItemType) {
            return BuiltinTypesCatalogue.attributeNode;
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
        throw new UnsupportedOperationException("attribute node item type does not support facets");
    }

    @Override
    public String toString() {
        if (this.schemaDeclarationConstraint != null) {
            return "schema-attribute(" + this.schemaDeclarationConstraint.getDeclarationName() + ")";
        }
        if (this.schemaTypeConstraint != null) {
            return "attribute("
                + (this.nodeName == null ? "*" : this.nodeName)
                + ", "
                + this.schemaTypeConstraint.getTypeName()
                + ")";
        }
        if (isWildcardAttribute()) {
            return this.catalogueName.toString();
        }
        return "attribute(" + this.nodeName + ")";
    }

    @Override
    public boolean isResolved() {
        return (this.schemaTypeConstraint == null || this.schemaTypeConstraint.isResolved())
            && (this.schemaDeclarationConstraint == null || this.schemaDeclarationConstraint.isResolved());
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (this.schemaTypeConstraint != null) {
            this.schemaTypeConstraint.resolve(context, metadata);
        }
        if (this.schemaDeclarationConstraint != null) {
            this.schemaDeclarationConstraint.resolve(context, metadata);
        }
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (this.schemaTypeConstraint != null) {
            this.schemaTypeConstraint.resolve(context, metadata);
        }
        if (this.schemaDeclarationConstraint != null) {
            this.schemaDeclarationConstraint.resolve(context, metadata);
        }
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}
