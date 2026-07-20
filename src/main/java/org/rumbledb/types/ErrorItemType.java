package org.rumbledb.types;

import java.io.Serial;
import java.util.Set;

import org.rumbledb.config.RumbleConfiguration;
import org.rumbledb.context.Name;

/**
 * Bottom item type implementing {@code xs:error}.
 *
 * <p>
 * This type has an empty value space and behaves as the identity element for least-common-supertype
 * computations. That makes it useful both as the spec-facing {@code xs:error} type and as the merge
 * identity when inferring a type across many runtime items.
 */
public class ErrorItemType implements ItemType {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Name name = new Name(Name.XS_NS, "xs", "error");

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        // No fields to serialize
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        // No fields to deserialize
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ErrorItemType;
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        return true;
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        return other;
    }

    @Override
    public ItemType findLeastCommonSuperTypeLax(ItemType other) {
        return other;
    }

    @Override
    public int getTypeTreeDepth() {
        return Integer.MAX_VALUE;
    }

    @Override
    public ItemType getBaseType() {
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("xs:error does not support facets");
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleConfiguration configuration) {
        return false;
    }

    @Override
    public String getSparkSQLType() {
        throw new UnsupportedOperationException("xs:error does not have a SparkSQL representation");
    }

    @Override
    public boolean isAtomicItemType() {
        return true;
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        return false;
    }

    @Override
    public boolean canBePromotedTo(ItemType itemType) {
        return false;
    }

    @Override
    public boolean isUserDefined() {
        return false;
    }

    @Override
    public void resolve(
            org.rumbledb.context.DynamicContext context,
            org.rumbledb.exceptions.ExceptionMetadata metadata
    ) {
        // No resolution needed
    }

    @Override
    public void resolve(
            org.rumbledb.context.StaticContext context,
            org.rumbledb.exceptions.ExceptionMetadata metadata
    ) {
        // No resolution needed
    }
}
