package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Class representing processing-instruction() and processing-instruction(N) item types.
 *
 * <p>Wildcard processing-instruction() is represented with no target-name restriction.
 * processing-instruction(N) is represented with a normalized target-name restriction.
 */
public class PINodeItemType extends AbstractItemType {

    @Serial private static final long serialVersionUID = 1L;

    private Name catalogueName;
    @Getter private String normalizedTarget;

    public PINodeItemType() {
        this.catalogueName = Name.createVariableInDefaultTypeNamespace("processing-instruction");
        this.normalizedTarget = null;
    }

    public PINodeItemType(String targetName) {
        if (targetName == null) {
            throw new IllegalArgumentException("Processing-instruction target cannot be null.");
        }
        this.catalogueName = null;
        this.normalizedTarget = StringUtils.normalizeSpace(targetName);
    }

    private boolean isWildcardPI() {
        return this.normalizedTarget == null;
    }

    @Override
    protected Object equalityKey() {
        return structuralTypeKey(PINodeItemType.class, this.catalogueName, this.normalizedTarget);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException(
                    "Named processing-instruction item type has no builtin QName");
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
        if (!(superType instanceof PINodeItemType other)) {
            return false;
        }
        if (other.isWildcardPI()) {
            return true;
        }
        return this.normalizedTarget != null
                && this.normalizedTarget.equals(other.normalizedTarget);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.equals(other)) {
            return this;
        }
        if (other instanceof PINodeItemType) {
            return BuiltinTypesCatalogue.processingInstructionNode;
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
        throw new UnsupportedOperationException(
                "processing-instruction item type does not support facets");
    }

    @Override
    public String toString() {
        if (isWildcardPI()) {
            return this.catalogueName.toString();
        }
        return "processing-instruction(" + this.normalizedTarget + ")";
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
