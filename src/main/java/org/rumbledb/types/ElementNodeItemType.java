package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

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
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.catalogueName, Name.class);
        kryo.writeObjectOrNull(output, this.nodeName, Name.class);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.catalogueName = kryo.readObjectOrNull(input, Name.class);
        this.nodeName = kryo.readObjectOrNull(input, Name.class);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType) || !((ItemType) other).isNodeItemType()) {
            return false;
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.catalogueName, this.nodeName);
    }

    @Override
    public boolean isEqualTo(ItemType otherType) {
        if (!(otherType instanceof ElementNodeItemType)) {
            return false;
        }
        ElementNodeItemType other = (ElementNodeItemType) otherType;
        return Objects.equals(this.catalogueName, other.catalogueName)
            && Objects.equals(this.nodeName, other.nodeName);
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
        if (!(superType instanceof ElementNodeItemType)) {
            return false;
        }
        ElementNodeItemType other = (ElementNodeItemType) superType;
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
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}
