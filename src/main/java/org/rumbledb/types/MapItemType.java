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
 * XQuery/XPath map item type: map(*) and map(K, V) per XDM 3.1 / XPath 3.1.
 * map(*) is a subtype of function(*) (see base type chain).
 */
public class MapItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private Name name;
    private ItemType baseType;
    private ItemType keyType;
    private SequenceType valueSequenceType;
    private int typeTreeDepth;

    MapItemType() {
        this.name = null;
        this.baseType = null;
        this.keyType = null;
        this.valueSequenceType = null;
    }

    /**
     * @param name null for anonymous typed maps
     * @param baseType {@link BuiltinTypesCatalogue#anyFunctionItem} for primitive map(*), else
     *        {@link BuiltinTypesCatalogue#mapItem}
     */
    MapItemType(
            Name name,
            ItemType baseType,
            ItemType keyType,
            SequenceType valueSequenceType
    ) {
        if (baseType == null || keyType == null || valueSequenceType == null) {
            throw new OurBadException("map item type requires base, key, and value sequence types");
        }
        this.name = name;
        this.baseType = baseType;
        this.keyType = keyType;
        this.valueSequenceType = valueSequenceType;
        if (this.baseType.isResolved()) {
            processBaseType();
            if (isTypedMap() && this.keyType.isResolved() && this.valueSequenceType.isResolved()) {
                checkSubtypeConsistency();
            }
        }
    }

    private boolean isTypedMap() {
        return this.baseType.equals(BuiltinTypesCatalogue.mapItem);
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
        if(((ItemType) other).isMapItemType()) {
            return this.structurallyEqual((MapItemType) other);
        }
        if(((ItemType) other).isObjectItemType() && other.equals(BuiltinTypesCatalogue.objectItem)) {
            // a js:object = map(xs:string, item)
            ItemType objectAsMap = ItemTypeFactory.mapOf(
                BuiltinTypesCatalogue.stringItem,
                SequenceType.createSequenceType("item")
            );
            return this.equals(objectAsMap);
        }
            return isEqualTo((ItemType) other);
        }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.keyType, this.valueSequenceType, this.baseType);
    }

    boolean structurallyEqual(MapItemType o) {
        return Objects.equals(this.name, o.name)
            && this.keyType.equals(o.keyType)
            && this.valueSequenceType.equals(o.valueSequenceType)
            && this.baseType.equals(o.baseType);
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
        if (superType.isObjectItemType()) {
            // js:object = map(xs:string, item)
            ItemType objectAsMap = ItemTypeFactory.mapOf(
                BuiltinTypesCatalogue.stringItem,
                SequenceType.createSequenceType("item")
            );
            // an object type (js:object) WITHOUT a JSound schema attached is a subtype of a map(*)
            return superType.equals(BuiltinTypesCatalogue.objectItem) && this.isSubtypeOf(objectAsMap);
        }
        if (superType.isMapItemType()) {
            MapItemType sup = (MapItemType) superType;
            return this.keyType.isSubtypeOf(sup.keyType)
                && this.valueSequenceType.isSubtypeOf(sup.valueSequenceType);
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
        if (other.isObjectItemType()) {
            // js:object = map(xs:string, item)
            ItemType objectAsMap = ItemTypeFactory.mapOf(
                BuiltinTypesCatalogue.stringItem,
                SequenceType.createSequenceType("item")
            );
            return this.findLeastCommonSuperTypeWith(objectAsMap);
        }
        if (other.isFunctionItemType()) {
            return BuiltinTypesCatalogue.anyFunctionItem;
        }
        if (other.isMapItemType()) {
            MapItemType otherMap = (MapItemType) other;
            ItemType keySuperType = this.keyType.findLeastCommonSuperTypeWith(otherMap.keyType);
            SequenceType valueSuperType = this.valueSequenceType.leastCommonSupertypeWith(otherMap.valueSequenceType);
            if (
                keySuperType.equals(BuiltinTypesCatalogue.atomicItem)
                    && valueSuperType.equals(SequenceType.createSequenceType("item*"))
            ) {
                return BuiltinTypesCatalogue.mapItem;
            }
            return ItemTypeFactory.mapOf(keySuperType, valueSuperType);
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

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public Name getName() {
        if (this.name == null) {
            throw new UnsupportedOperationException("anonymous map item type has no QName");
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
    public ItemType getMapKeyItemType() {
        return this.keyType;
    }

    @Override
    public SequenceType getMapValueSequenceType() {
        return this.valueSequenceType;
    }

    void processBaseType() {
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (isPrimitiveMap()) {
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
            return;
        }
        if (!isTypedMap()) {
            throw new InvalidSchemaException(
                    "Invalid base type for map item type: " + this.baseType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
    }

    private void checkSubtypeConsistency() {
        MapItemType primitive = (MapItemType) BuiltinTypesCatalogue.mapItem;
        if (!this.keyType.isSubtypeOf(primitive.keyType)) {
            throw new InvalidSchemaException(
                    "Map key type " + this.keyType + " must be a subtype of " + primitive.keyType,
                    ExceptionMetadata.EMPTY_METADATA
            );
        }
        if (!this.valueSequenceType.isSubtypeOf(primitive.valueSequenceType)) {
            throw new InvalidSchemaException(
                    "Map value sequence type "
                        + this.valueSequenceType
                        + " must be a subtype of "
                        + primitive.valueSequenceType,
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
        if (isTypedMap()) {
            checkSubtypeConsistency();
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
        if (isTypedMap()) {
            checkSubtypeConsistency();
        }
    }

    @Override
    public boolean isResolved() {
        return this.baseType.isResolved()
            && this.keyType.isResolved()
            && this.valueSequenceType.isResolved();
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
        return "map(" + this.keyType + ", " + this.valueSequenceType + ")";
    }

    @Override
    public String toString() {
        if (isPrimitiveMap() && hasName()) {
            return "map(*)";
        }
        return "map(" + this.keyType + ", " + this.valueSequenceType + ")";
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        return false;
    }

    @Override
    public String getSparkSQLType() {
        throw new UnsupportedOperationException("map item type is not mapped to Spark SQL");
    }
}
