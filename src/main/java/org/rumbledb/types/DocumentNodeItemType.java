package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.Objects;
import java.util.Set;

/**
 * Class representing document-node() and document-node(element(...)) item types.
 *
 * Wildcard document-node() is represented with no element-test restriction.
 * document-node(element(...)) is represented with a concrete inner element node item type.
 */
public class DocumentNodeItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name catalogueName;
    private ElementNodeItemType elementTestType;

    public DocumentNodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("document-node");
        this.elementTestType = null;
    }

    public DocumentNodeItemType(ElementNodeItemType elementTestType) {
        if (elementTestType == null) {
            throw new IllegalArgumentException("Document element-test type cannot be null.");
        }
        this.catalogueName = null;
        this.elementTestType = elementTestType;
    }

    private boolean isWildcardDocument() {
        return this.elementTestType == null;
    }

    public ElementNodeItemType getElementTestType() {
        return this.elementTestType;
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.catalogueName, Name.class);
        kryo.writeObjectOrNull(output, this.elementTestType, ElementNodeItemType.class);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.catalogueName = kryo.readObjectOrNull(input, Name.class);
        this.elementTestType = kryo.readObjectOrNull(input, ElementNodeItemType.class);
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
        return Objects.hash(this.catalogueName, this.elementTestType);
    }

    @Override
    public boolean isEqualTo(ItemType otherType) {
        if (!(otherType instanceof DocumentNodeItemType)) {
            return false;
        }
        DocumentNodeItemType other = (DocumentNodeItemType) otherType;
        return Objects.equals(this.catalogueName, other.catalogueName)
            && Objects.equals(this.elementTestType, other.elementTestType);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException("Named document node item type has no builtin QName");
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
        if (
            this.equals(superType)
                || superType.equals(BuiltinTypesCatalogue.item)
                || superType.equals(BuiltinTypesCatalogue.nodeItem)
        ) {
            return true;
        }
        if (!(superType instanceof DocumentNodeItemType)) {
            return false;
        }
        DocumentNodeItemType other = (DocumentNodeItemType) superType;
        if (other.isWildcardDocument()) {
            return true;
        }
        return this.elementTestType != null && this.elementTestType.isSubtypeOf(other.elementTestType);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof DocumentNodeItemType) {
            DocumentNodeItemType otherDocument = (DocumentNodeItemType) other;
            if (this.isWildcardDocument() || otherDocument.isWildcardDocument()) {
                return BuiltinTypesCatalogue.documentNode;
            }
            ItemType innerLeastCommonSuperType = this.elementTestType.findLeastCommonSuperTypeWith(
                otherDocument.elementTestType
            );
            return new DocumentNodeItemType((ElementNodeItemType) innerLeastCommonSuperType);
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
        throw new UnsupportedOperationException("document node item type does not support facets");
    }

    @Override
    public String toString() {
        if (isWildcardDocument()) {
            return this.catalogueName.toString();
        }
        return "document-node(" + this.elementTestType + ")";
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
