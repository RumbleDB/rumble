package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.Name;

import java.util.Set;

/**
 * Neutral element ItemType for aggregation operations.
 * This type serves as the neutral element in Spark aggregation operations
 * and ensures that any real ItemType combined with it returns the real ItemType.
 */
public class NeutralItemType implements ItemType {

    private static final long serialVersionUID = 1L;
    private static final Name name = Name.createVariableInDefaultTypeNamespace("neutral");

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
        return other instanceof NeutralItemType;
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
        return false; // Neutral type is not a subtype of any real type
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        // Identity property: neutral element combined with any type returns that type
        return other;
    }

    @Override
    public int getTypeTreeDepth() {
        // Return a very large value to ensure it's treated as neutral
        return Integer.MAX_VALUE;
    }

    @Override
    public ItemType getBaseType() {
        return null;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("neutral type does not support facets");
    }

    @Override
    public String toString() {
        return "neutral";
    }

    @Override
    public boolean isResolved() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false; // Neutral type cannot be used in DataFrames
    }

    @Override
    public String getSparkSQLType() {
        throw new UnsupportedOperationException("neutral type does not have SparkSQL representation");
    }

    // Default implementations for all other ItemType methods
    @Override
    public boolean isTopmostItemType() {
        return false;
    }

    @Override
    public boolean isAtomicItemType() {
        return false;
    }

    @Override
    public boolean isObjectItemType() {
        return false;
    }

    @Override
    public boolean isArrayItemType() {
        return false;
    }

    @Override
    public boolean isJsonItemType() {
        return false;
    }

    @Override
    public boolean isUnionType() {
        return false;
    }

    @Override
    public boolean isFunctionItemType() {
        return false;
    }

    @Override
    public boolean isNumeric() {
        return false;
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
