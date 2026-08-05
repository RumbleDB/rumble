package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Class representing element() and element(QName) item types.
 *
 * <p>Wildcard element() is represented with no node-name restriction. element(QName) is represented
 * with a concrete node-name restriction.
 */
public class ElementNodeItemType extends AbstractItemType {

    @Serial private static final long serialVersionUID = 1L;

    private Name catalogueName;
    @Getter private Name nodeName;

    public ElementNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("element");
        this.nodeName = null;
    }

    public ElementNodeItemType(Name nodeName) {
        if (nodeName == null) {
            throw new IllegalArgumentException("Element node name cannot be null.");
        }
        this.catalogueName = null;
        this.nodeName = nodeName;
    }

    private boolean isWildcardElement() {
        return this.nodeName == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(ElementNodeItemType.class, this.catalogueName, this.nodeName);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException(
                    "Named element node item type has no builtin QName");
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
        if (!(superType instanceof ElementNodeItemType other)) {
            return false;
        }
        if (other.isWildcardElement()) {
            return true;
        }
        return this.nodeName != null && this.nodeName.equals(other.nodeName);
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
        if (isWildcardElement()) {
            return this.catalogueName.toString();
        }
        return "element(" + this.nodeName + ")";
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
