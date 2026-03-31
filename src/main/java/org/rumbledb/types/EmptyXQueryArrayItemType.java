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
 * Artificial item type representing the dynamic type of empty XQuery arrays.
 *
 * Semantics:
 * - It is considered an array item type.
 * - It is a subtype of every concrete array(T) type and of array(*)
 *   (via {@link BuiltinTypesCatalogue#xqueryArrayItem}).
 * - It is also a subtype of function(*) through the usual array(*) -> function(*) chain.
 *
 * This type is internal and is not exposed as a user-facing XQuery type.
 */
public class EmptyXQueryArrayItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private static final EmptyXQueryArrayItemType INSTANCE = new EmptyXQueryArrayItemType();
    private Name name;
    private ItemType baseType;
    private SequenceType memberSequenceType;
    private int typeTreeDepth;

    private EmptyXQueryArrayItemType() {
        this.name = null;
        this.baseType = BuiltinTypesCatalogue.anyFunctionItem;
        this.memberSequenceType = SequenceType.createSequenceType("item*");
        processBaseType();
    }

    public static EmptyXQueryArrayItemType getInstance() {
        return INSTANCE;
    }

    private boolean isPrimitiveArray() {
        return this.baseType.equals(BuiltinTypesCatalogue.anyFunctionItem);
    }

    @Override
    public boolean isArrayItemType() {
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
        return Objects.hash(this.name, this.baseType, this.memberSequenceType, this.typeTreeDepth);
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
        if (superType.isXQueryArrayItemType()) {
            // By design, the empty-array type is a subtype of every array(T) and array(*).
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
        if (other.isArrayItemType()) {
            // Any concrete array type, including array(*), is a supertype of the empty-array type.
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

    public SequenceType getMemberSequenceType() {
        return this.memberSequenceType;
    }

    void processBaseType() {
        if (this.baseType == null || this.memberSequenceType == null) {
            throw new OurBadException("array item type requires base and member sequence types");
        }
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (!isPrimitiveArray()) {
            throw new InvalidSchemaException(
                    "Invalid base type for empty array item type: " + this.baseType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (!this.memberSequenceType.equals(SequenceType.createSequenceType("item*"))) {
            throw new InvalidSchemaException(
                    "Primitive array(*) must use item* for members, got: " + this.memberSequenceType,
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
        if (!this.memberSequenceType.isResolved()) {
            this.memberSequenceType.resolve(context, metadata);
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
        if (!this.memberSequenceType.isResolved()) {
            this.memberSequenceType.resolve(context, metadata);
        }
    }

    @Override
    public boolean isResolved() {
        return this.baseType.isResolved()
            && this.memberSequenceType.isResolved();
    }

    @Override
    public String getIdentifierString() {
        return "<empty-array>";
    }

    @Override
    public String toString() {
        return "array{}";
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }

    @Override
    public String getSparkSQLType() {
        throw new UnsupportedOperationException("array item type is not mapped to Spark SQL");
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.name, Name.class);
        kryo.writeClassAndObject(output, this.baseType);
        kryo.writeClassAndObject(output, this.memberSequenceType);
        output.writeInt(this.typeTreeDepth);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.name = kryo.readObjectOrNull(input, Name.class);
        this.baseType = (ItemType) kryo.readClassAndObject(input);
        this.memberSequenceType = (SequenceType) kryo.readClassAndObject(input);
        this.typeTreeDepth = input.readInt();
    }
}

