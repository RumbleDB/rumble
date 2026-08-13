package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.UndefinedTypeException;
import org.rumbledb.xml.schema.XmlSchemaCatalog;

/**
 * Class representing attribute(), attribute(QName), and schema-typed attribute item types.
 *
 * Wildcard attribute() is represented with no node-name restriction.
 * attribute(QName) is represented with a concrete node-name restriction.
 */
public class AttributeNodeItemType extends AbstractItemType {

    @Serial
    private static final long serialVersionUID = 1L;

    private Name catalogueName;

    @Getter
    private Name nodeName;

    @Getter
    private Name typeName;

    private boolean resolved;

    public AttributeNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("attribute");
        this.nodeName = null;
        this.typeName = null;
        this.resolved = true;
    }

    public AttributeNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Attribute node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.typeName = null;
        this.resolved = true;
    }

    public AttributeNodeItemType(Name nodeName, Name typeName) {
        this(nodeName, typeName, false);
    }

    AttributeNodeItemType(Name nodeName, Name typeName, boolean resolved) {
        if (typeName == null) {
            throw new IllegalArgumentException("Attribute schema type name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.typeName = typeName;
        this.resolved = resolved;
    }

    private boolean isWildcardAttribute() {
        return this.nodeName == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(AttributeNodeItemType.class, this.catalogueName, this.nodeName, this.typeName);
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
        if (this.equals(superType)
                || superType.equals(BuiltinTypesCatalogue.item)
                || superType.equals(BuiltinTypesCatalogue.nodeItem)) {
            return true;
        }
        if (!(superType instanceof AttributeNodeItemType other)) {
            return false;
        }
        if (other.nodeName != null && (this.nodeName == null || !this.nodeName.equals(other.nodeName))) {
            return false;
        }
        return other.typeName == null || (this.typeName != null && this.typeName.equals(other.typeName));
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
        if (isWildcardAttribute() && this.typeName == null) {
            return this.catalogueName.toString();
        }
        String name = this.nodeName == null ? "*" : this.nodeName.toString();
        return this.typeName == null ? "attribute(" + name + ")" : "attribute(" + name + ", " + this.typeName + ")";
    }

    @Override
    public boolean isResolved() {
        return this.resolved;
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (this.resolved) {
            return;
        }
        this.typeName = ItemTypeReference.renameAtomic(context, this.typeName);
        XmlSchemaCatalog catalog = context.getXmlSchemaCatalog();
        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(this.typeName)
                && (catalog == null || !catalog.isImportedSimpleType(this.typeName))) {
            throw new UndefinedTypeException("Type undefined: " + this.typeName, metadata);
        }
        this.resolved = true;
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (this.resolved) {
            return;
        }
        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(this.typeName)) {
            throw new UndefinedTypeException("Type undefined: " + this.typeName, metadata);
        }
        this.resolved = true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }
}
