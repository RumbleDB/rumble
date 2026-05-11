package org.rumbledb.types;

import org.apache.commons.lang3.StringUtils;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.Objects;
import java.util.Set;

/**
 * Class representing processing-instruction() and processing-instruction(N) item types.
 *
 * Wildcard processing-instruction() is represented with no target-name restriction.
 * processing-instruction(N) is represented with a normalized target-name restriction.
 */
public class PINodeItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name catalogueName;
    private String normalizedTarget;

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

    public String getNormalizedTarget() {
        return this.normalizedTarget;
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.catalogueName, Name.class);
        output.writeString(this.normalizedTarget);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.catalogueName = kryo.readObjectOrNull(input, Name.class);
        this.normalizedTarget = input.readString();
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
        return Objects.hash(this.catalogueName, this.normalizedTarget);
    }

    @Override
    public boolean isEqualTo(ItemType otherType) {
        if (!(otherType instanceof PINodeItemType)) {
            return false;
        }
        PINodeItemType other = (PINodeItemType) otherType;
        return Objects.equals(this.catalogueName, other.catalogueName)
            && Objects.equals(this.normalizedTarget, other.normalizedTarget);
    }

    @Override
    public boolean hasName() {
        return this.catalogueName != null;
    }

    @Override
    public Name getName() {
        if (this.catalogueName == null) {
            throw new UnsupportedOperationException("Named processing-instruction item type has no builtin QName");
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
        if (!(superType instanceof PINodeItemType)) {
            return false;
        }
        PINodeItemType other = (PINodeItemType) superType;
        if (other.isWildcardPI()) {
            return true;
        }
        return this.normalizedTarget != null && this.normalizedTarget.equals(other.normalizedTarget);
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
        throw new UnsupportedOperationException("processing-instruction item type does not support facets");
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
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }
}
