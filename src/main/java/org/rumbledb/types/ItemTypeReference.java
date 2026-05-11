package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UndefinedTypeException;

import java.util.*;

public class ItemTypeReference implements ItemType {

    private static final long serialVersionUID = 1L;

    private ItemType resolvedItemType;
    private Name name;

    public ItemTypeReference() {
    }

    public ItemTypeReference(Name name) {
        if (name == null) {
            throw new OurBadException("A type name cannot be null!");
        }
        this.name = name;
    }

    @Override
    public void write(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Output output) {
        kryo.writeObjectOrNull(output, this.name, Name.class);
        kryo.writeClassAndObject(output, this.resolvedItemType);
    }

    @Override
    public void read(com.esotericsoftware.kryo.Kryo kryo, com.esotericsoftware.kryo.io.Input input) {
        this.name = kryo.readObjectOrNull(input, Name.class);
        this.resolvedItemType = (ItemType) kryo.readClassAndObject(input);
    }

    public boolean isResolved() {
        return this.resolvedItemType != null;
    }

    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(this.name)) {
            throw new UndefinedTypeException("Type undefined: " + this.name, metadata);
        }
        this.resolvedItemType = context.getInScopeSchemaTypes().getInScopeSchemaType(this.name);
        if (!this.resolvedItemType.isResolved()) {
            this.resolvedItemType.resolve(context, metadata);
        }
    }



    public void resolve(StaticContext context, ExceptionMetadata metadata) {

        Name renamed = renameAtomic(context.getRumbleConfiguration(), this.name);

        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(renamed)) {
            throw new UndefinedTypeException("Type undefined: " + renamed, metadata);
        }
        this.resolvedItemType = context.getInScopeSchemaTypes().getInScopeSchemaType(renamed);
        if (!this.resolvedItemType.isResolved()) {
            this.resolvedItemType.resolve(context, metadata);
        }
    }

    private static final Name oldAtomicName = new Name(Name.JS_NS, "js", "atomic");
    private static final Name newAtomicName = new Name(Name.XS_NS, "xs", "anyAtomicType");

    /**
     * in jsoniq 1.0 anyAtomicType was called atomic. This function gives backwards compatibility by replacing atomic
     * with anyAtomicType depending on the jsoniq version.
     */
    public static Name renameAtomic(RumbleRuntimeConfiguration config, Name oldName) {
        if (config.getQueryLanguage().equals("jsoniq10")) {
            if (oldName.getNamespace() != null && oldName.getNamespace().equals(Name.JSONIQ_DEFAULT_TYPE_NS)) {
                if (oldAtomicName.getLocalName().equals(oldName.getLocalName())) {
                    return newAtomicName;
                }
            } else {
                if (oldAtomicName.equals(oldName)) {
                    return newAtomicName;
                }
            }
        }
        return oldName;
    }

    @Override
    public int getTypeTreeDepth() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getTypeTreeDepth();
    }

    @Override
    public ItemType getBaseType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getBaseType();
    }

    @Override
    public Set<ConstrainingFacetTypes> getAllowedFacets() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getAllowedFacets();
    }

    public boolean equals(Object other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.equals(other);
    }

    public boolean isAtomicItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isAtomicItemType();
    }

    public boolean isObjectItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isObjectItemType();
    }

    public boolean isArrayItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isArrayItemType();
    }

    public boolean isJsonItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isJsonItemType();
    }

    public boolean isUnionType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isUnionType();
    }

    public boolean isFunctionItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isFunctionItemType();
    }

    public boolean isNumeric() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isNumeric();
    }

    public boolean hasName() {
        return true;
    }


    public Name getName() {
        if (this.resolvedItemType != null) {
            return this.resolvedItemType.getName();
        }
        return this.name;
    }

    public FunctionSignature getSignature() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getSignature();
    }

    public boolean isSubtypeOf(ItemType superType) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isSubtypeOf(superType);
    }

    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.findLeastCommonSuperTypeWith(other);
    }

    public ItemType findLeastCommonSuperTypeLax(ItemType other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.findLeastCommonSuperTypeLax(other);
    }

    public boolean isStaticallyCastableAs(ItemType other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isStaticallyCastableAs(other);
    }

    public boolean canBePromotedTo(ItemType itemType) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.canBePromotedTo(itemType);
    }

    public boolean isUserDefined() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isUserDefined();
    }

    public boolean isPrimitive() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isPrimitive();
    }

    public ItemType getPrimitiveType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getPrimitiveType();
    }

    public List<Item> getEnumerationFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getEnumerationFacet();
    }

    public List<String> getConstraintsFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getConstraintsFacet();
    }

    public Integer getMinLengthFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMinLengthFacet();
    }

    public Integer getLengthFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getLengthFacet();
    }

    public Integer getMaxLengthFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMaxLengthFacet();
    }

    public Item getMinExclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMinExclusiveFacet();
    }

    public Item getMinInclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMinInclusiveFacet();
    }

    public Item getMaxExclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMaxExclusiveFacet();
    }

    public Item getMaxInclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMaxInclusiveFacet();
    }

    public Integer getTotalDigitsFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getTotalDigitsFacet();
    }

    public Integer getFractionDigitsFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getFractionDigitsFacet();
    }

    public TimezoneFacet getExplicitTimezoneFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getExplicitTimezoneFacet();
    }

    public WhitespaceFacet getWhitespaceFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getWhitespaceFacet();
    }

    public List<String> getPatternFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getPatternFacet();
    }

    public OrderedFacetValue getOrderedFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getOrderedFacet();
    }

    public Boolean getBoundedFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getBoundedFacet();
    }

    public CardinalityFacetValue getCardinalityFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getCardinalityFacet();
    }

    public Boolean getNumericFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getNumericFacet();
    }

    public Map<String, FieldDescriptor> getObjectContentFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getObjectContentFacet();
    }

    public boolean getClosedFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getClosedFacet();
    }

    public ItemType getArrayContentFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getArrayContentFacet();
    }

    public List<ItemType> getTypes() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getTypes();
    }

    public String getIdentifierString() {
        if (!this.hasName()) {
            return "<anonymous>";
        }
        if (this.resolvedItemType == null) {
            return this.name.toString();
        }
        return this.resolvedItemType.getIdentifierString();
    }

    public String toString() {
        if (!this.hasName()) {
            return "<anonymous>";
        }
        if (this.resolvedItemType == null) {
            return this.name.toString();
        }
        return this.resolvedItemType.toString();
    }

    @Override
    public boolean isCompatibleWithDataFrames(RumbleRuntimeConfiguration configuration) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isCompatibleWithDataFrames(configuration);
    }
}
