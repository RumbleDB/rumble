package org.rumbledb.types;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;
import org.rumbledb.exceptions.OurBadException;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Artificial item type representing the dynamic type of empty XQuery maps.
 *
 * Semantics:
 * - It is considered a map item type.
 * - It is a subtype of every concrete map(K, V) type and of map(*) (via BuiltinTypesCatalogue.mapItem).
 * - It is also a subtype of function(*) through the usual map(*) -> function(*) chain.
 * - It is not a subtype of js:object or other JSON-only types.
 *
 * This type is internal and is not exposed as a user-facing XQuery type.
 */
public class EmptyMapItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private static final EmptyMapItemType INSTANCE = new EmptyMapItemType();
    private Name name;
    private ItemType baseType;
    private ItemType keyType;
    private SequenceType valueSequenceType;
    private int typeTreeDepth;

    private EmptyMapItemType() {
        this.name = null;
        this.baseType = BuiltinTypesCatalogue.anyFunctionItem;
        this.keyType = BuiltinTypesCatalogue.atomicItem;
        this.valueSequenceType = SequenceType.createSequenceType("item*");
        processBaseType();
    }

    public static EmptyMapItemType getInstance() {
        return INSTANCE;
    }

    private boolean isPrimitiveMap() {
        return this.baseType.equals(BuiltinTypesCatalogue.anyFunctionItem);
    }

    @Override
    public boolean isMapItemType() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.baseType, this.keyType, this.valueSequenceType, this.typeTreeDepth);
    }

    @Override
    public boolean hasName() {
        return false;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (superType.isUnionType()) {
            for (ItemType member : superType.getTypes()) {
                if (this.isSubtypeOf(member)) {
                    return true;
                }
            }
        }
        if (equals(superType) || superType.equals(BuiltinTypesCatalogue.item)) {
            return true;
        }
        if (superType.isMapItemType()) {
            // By design, the empty-map type is a subtype of every map(K, V) and map(*).
            return true;
        }
        if (superType.isFunctionItemType()) {
            return superType.equals(BuiltinTypesCatalogue.anyFunctionItem);
        }
        return false;
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (equals(other)) {
            return this;
        }
        if (other.isMapItemType()) {
            // Any concrete map type, including map(*), is a supertype of the empty-map type.
            return other;
        }
        if (other.isFunctionItemType()) {
            return BuiltinTypesCatalogue.anyFunctionItem;
        }
        return BuiltinTypesCatalogue.item;
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        return Collections.emptySet();
    }

    @Override
    public int getTypeTreeDepth() {
        return this.typeTreeDepth;
    }

    @Override
    public ItemType getBaseType() {
        return this.baseType;
    }

    @Override
    public ItemType getMapKeyItemType() {
        return this.keyType;
    }

    @Override
    public SequenceType getMapValueSequenceType() {
        return this.valueSequenceType;
    }

    void processBaseType() {
        if (this.baseType == null || this.keyType == null || this.valueSequenceType == null) {
            throw new OurBadException("map item type requires base, key, and value sequence types");
        }
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (!isPrimitiveMap()) {
            throw new InvalidSchemaException(
                    "Invalid base type for empty map item type: " + this.baseType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (!this.keyType.equals(BuiltinTypesCatalogue.atomicItem)) {
            throw new InvalidSchemaException(
                    "Primitive map(*) must use xs:anyAtomicType for keys, got: " + this.keyType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (!this.valueSequenceType.equals(SequenceType.createSequenceType("item*"))) {
            throw new InvalidSchemaException(
                    "Primitive map(*) must use item* for values, got: " + this.valueSequenceType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
        if (!this.keyType.isResolved()) {
            this.keyType.resolve(context, metadata);
        }
        if (!this.valueSequenceType.isResolved()) {
            this.valueSequenceType.resolve(context, metadata);
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
        if (!this.keyType.isResolved()) {
            this.keyType.resolve(context, metadata);
        }
        if (!this.valueSequenceType.isResolved()) {
            this.valueSequenceType.resolve(context, metadata);
        }
    }

    @Override
    public boolean isResolved() {
        return this.baseType.isResolved()
            && this.keyType.isResolved()
            && this.valueSequenceType.isResolved();
    }

    @Override
    public String getIdentifierString() {
        return "<empty-map>";
    }

    @Override
    public String toString() {
        return "map{}";
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }

    @Override
    public String getSparkSQLType() {
        throw new UnsupportedOperationException("map item type is not mapped to Spark SQL");
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.name, Name.class);
        kryo.writeClassAndObject(output, this.baseType);
        kryo.writeClassAndObject(output, this.keyType);
        kryo.writeClassAndObject(output, this.valueSequenceType);
        output.writeInt(this.typeTreeDepth);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.name = kryo.readObjectOrNull(input, Name.class);
        this.baseType = (ItemType) kryo.readClassAndObject(input);
        this.keyType = (ItemType) kryo.readClassAndObject(input);
        this.valueSequenceType = (SequenceType) kryo.readClassAndObject(input);
        this.typeTreeDepth = input.readInt();
    }
}

