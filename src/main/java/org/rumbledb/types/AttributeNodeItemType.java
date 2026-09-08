package org.rumbledb.types;

import java.io.Serial;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Class representing attribute() and attribute(QName) item types.
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
    private Name schemaTypeName;

    private List<Name> schemaTypeHierarchy;

    // Includes member types when the declaration uses a pure union.
    @Getter
    private List<Name> schemaTypeAlternatives = List.of();

    public AttributeNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("attribute");
        this.nodeName = null;
        this.schemaTypeName = null;
        this.schemaTypeHierarchy = List.of();
    }

    public AttributeNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Attribute node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeName = null;
        this.schemaTypeHierarchy = List.of();
    }

    public AttributeNodeItemType(Name nodeName, Name schemaTypeName, List<Name> schemaTypeHierarchy) {
        if (schemaTypeName == null || schemaTypeHierarchy == null || schemaTypeHierarchy.isEmpty()) {
            throw new IllegalArgumentException("A typed attribute test requires a schema type hierarchy.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
        this.schemaTypeName = schemaTypeName;
        this.schemaTypeHierarchy = List.copyOf(schemaTypeHierarchy);
        this.schemaTypeAlternatives = List.of(schemaTypeName);
    }

    public AttributeNodeItemType(
            Name nodeName, Name schemaTypeName, List<Name> schemaTypeHierarchy, List<Name> schemaTypeAlternatives) {
        this(nodeName, schemaTypeName, schemaTypeHierarchy);
        this.schemaTypeAlternatives = List.copyOf(schemaTypeAlternatives);
    }

    private boolean isWildcardAttribute() {
        return this.nodeName == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(
                AttributeNodeItemType.class,
                this.catalogueName,
                this.nodeName,
                this.schemaTypeName,
                this.schemaTypeAlternatives);
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
        if (other.isWildcardAttribute()) {
            return other.schemaTypeName == null || hasCompatibleSchemaType(other);
        }
        return this.nodeName != null
                && this.nodeName.equals(other.nodeName)
                && (other.schemaTypeName == null || hasCompatibleSchemaType(other));
    }

    private boolean hasCompatibleSchemaType(AttributeNodeItemType superType) {
        return this.schemaTypeName != null
                && superType.schemaTypeAlternatives.stream().anyMatch(this.schemaTypeHierarchy::contains);
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
        if (this.catalogueName != null) {
            return this.catalogueName.toString();
        }
        String name = this.nodeName == null ? "*" : this.nodeName.toString();
        return this.schemaTypeName == null
                ? "attribute(" + name + ")"
                : "attribute(" + name + ", " + this.schemaTypeName + ")";
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }
}
