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
 * XQuery/XPath array item type: array(*) and array(T) per XDM 3.1 / XPath 3.1.
 *
 * The primitive array(*) type is modeled as a subtype of function(*)
 * by using {@link BuiltinTypesCatalogue#anyFunctionItem} as its base type.
 */
public class XQueryArrayItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name name;
    private ItemType baseType;
    private SequenceType memberSequenceType;
    private int typeTreeDepth;

    XQueryArrayItemType() {
        this.name = null;
        this.baseType = null;
        this.memberSequenceType = null;
    }

    /**
     * @param name null for anonymous typed arrays
     * @param baseType {@link BuiltinTypesCatalogue#anyFunctionItem} for primitive array(*),
     *        else {@link BuiltinTypesCatalogue#xqueryArrayItem}
     */
    XQueryArrayItemType(
            Name name,
            ItemType baseType,
            SequenceType memberSequenceType
    ) {
        if (baseType == null || memberSequenceType == null) {
            throw new OurBadException("array item type requires base and member sequence types");
        }
        this.name = name;
        this.baseType = baseType;
        this.memberSequenceType = memberSequenceType;
        if (this.baseType.isResolved()) {
            processBaseType();
            if (isTypedArray() && this.memberSequenceType.isResolved()) {
                checkSubtypeConsistency();
            }
        }
    }

    private boolean isTypedArray() {
        return this.baseType.equals(BuiltinTypesCatalogue.xqueryArrayItem);
    }

    private boolean isPrimitiveArray() {
        return this.baseType.equals(BuiltinTypesCatalogue.anyFunctionItem);
    }

    @Override
    public boolean isXQueryArrayItemType() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        if (((ItemType) other).isXQueryArrayItemType()) {
            // structural equality check
            return this.structurallyEqual((XQueryArrayItemType) other);
        }
        if (((ItemType) other).isArrayItemType() && other.equals(BuiltinTypesCatalogue.arrayItem)) {
            // js:array() = array(item)
            ArrayItemType arrayItemType = (ArrayItemType) ItemTypeFactory.xqueryArrayOf(
                SequenceType.createSequenceType("item")
            );
            return this.equals(arrayItemType);
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.baseType, this.memberSequenceType);
    }

    boolean structurallyEqual(XQueryArrayItemType o) {
        return Objects.equals(this.name, o.name)
            && this.baseType.equals(o.baseType)
            && this.memberSequenceType.equals(o.memberSequenceType);
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
        if (superType.isArrayItemType()) {
            ItemType arrayItemType = ItemTypeFactory.xqueryArrayOf(
                new SequenceType(superType.getArrayContentFacet(), SequenceType.Arity.One)
            );
            // a js:array() with a base type of T and no other restrictions <: xs:array(T)
            return superType.equals(BuiltinTypesCatalogue.arrayItem) && this.isSubtypeOf(arrayItemType);
        }
        if (superType.isXQueryArrayItemType()) {
            return this.memberSequenceType.isSubtypeOf(superType.getMemberSequenceType());
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
        if (other.isFunctionItemType()) {
            return BuiltinTypesCatalogue.anyFunctionItem;
        }
        if (other.isArrayItemType()) {
            // an array(T) is a supertype of a js:array() with a base type of T and no other restrictions <: xs:array(T)
            ItemType arrayItemType = ItemTypeFactory.xqueryArrayOf(
                new SequenceType(other.getArrayContentFacet(), SequenceType.Arity.One)
            );
            return this.findLeastCommonSuperTypeWith(arrayItemType);
        }
        if (other.isXQueryArrayItemType()) {
            SequenceType memberSuperType = this.memberSequenceType.leastCommonSupertypeWith(
                other.getMemberSequenceType()
            );
            if (
                memberSuperType.equals(
                    SequenceType.createSequenceType("item*")
                )
            ) {
                return BuiltinTypesCatalogue.xqueryArrayItem;
            }
            return new XQueryArrayItemType(
                    null,
                    BuiltinTypesCatalogue.xqueryArrayItem,
                    memberSuperType
            );
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

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public Name getName() {
        if (this.name == null) {
            throw new UnsupportedOperationException("anonymous array item type has no QName");
        }
        return this.name;
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
    public SequenceType getMemberSequenceType() {
        return this.memberSequenceType;
    }

    void processBaseType() {
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (isPrimitiveArray()) {
            if (!this.memberSequenceType.equals(SequenceType.createSequenceType("item*"))) {
                throw new InvalidSchemaException(
                        "Primitive array(*) must use item* for members, got: " + this.memberSequenceType,
                        ExceptionMetadata.EMPTY_METADATA
                );
            }
            return;
        }
        if (!isTypedArray()) {
            throw new InvalidSchemaException(
                    "Invalid base type for array item type: " + this.baseType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    private void checkSubtypeConsistency() {
        XQueryArrayItemType primitive = (XQueryArrayItemType) BuiltinTypesCatalogue.xqueryArrayItem;
        if (!this.memberSequenceType.isSubtypeOf(primitive.memberSequenceType)) {
            throw new InvalidSchemaException(
                    "Array member sequence type "
                        + this.memberSequenceType
                        + " must be a subtype of "
                        + primitive.memberSequenceType,
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
        if (isTypedArray()) {
            checkSubtypeConsistency();
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
        if (isTypedArray()) {
            checkSubtypeConsistency();
        }
    }

    @Override
    public boolean isResolved() {
        return this.baseType.isResolved()
            && this.memberSequenceType.isResolved();
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        return Collections.emptySet();
    }

    @Override
    public String getIdentifierString() {
        if (hasName()) {
            return getName().toString();
        }
        return "array(" + this.memberSequenceType + ")";
    }

    @Override
    public String toString() {
        if (isPrimitiveArray() && hasName()) {
            return "array(*)";
        }
        return "array(" + this.memberSequenceType + ")";
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }

    @Override
    public String getSparkSQLType() {
        throw new UnsupportedOperationException("array item type is not mapped to Spark SQL");
    }
}

