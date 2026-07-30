package org.rumbledb.types;

import org.rumbledb.api.Item;
import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.UndefinedTypeException;

import java.io.Serial;
import java.util.*;

public class ItemTypeReference implements ItemType {

    @Serial
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
    public boolean isResolved() {
        return this.resolvedItemType != null;
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!context.getInScopeSchemaTypes().checkInScopeSchemaTypeExists(this.name)) {
            throw new UndefinedTypeException("Type undefined: " + this.name, metadata);
        }
        this.resolvedItemType = context.getInScopeSchemaTypes().getInScopeSchemaType(this.name);
        if (!this.resolvedItemType.isResolved()) {
            this.resolvedItemType.resolve(context, metadata);
        }
    }



    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {

        Name renamed = renameAtomic(context, this.name);

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
    public static Name renameAtomic(StaticContext context, Name oldName) {
        if (context.getQueryLanguage().equals("jsoniq10")) {
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

    @Override
    public boolean isAtomicItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isAtomicItemType();
    }

    @Override
    public boolean isObjectItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isObjectItemType();
    }

    @Override
    public boolean isArrayItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isArrayItemType();
    }

    @Override
    public boolean isJsonItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isJsonItemType();
    }

    @Override
    public boolean isUnionType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isUnionType();
    }

    @Override
    public boolean isFunctionItemType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isFunctionItemType();
    }

    @Override
    public boolean isNumeric() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isNumeric();
    }

    @Override
    public boolean hasName() {
        return true;
    }


    @Override
    public Name getName() {
        if (this.resolvedItemType != null) {
            return this.resolvedItemType.getName();
        }
        return this.name;
    }

    @Override
    public FunctionSignature getSignature() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getSignature();
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isSubtypeOf(superType);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.findLeastCommonSuperTypeWith(other);
    }

    @Override
    public ItemType findLeastCommonSuperTypeLax(ItemType other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.findLeastCommonSuperTypeLax(other);
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isStaticallyCastableAs(other);
    }

    @Override
    public boolean canBePromotedTo(ItemType itemType) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.canBePromotedTo(itemType);
    }

    @Override
    public boolean isUserDefined() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isUserDefined();
    }

    @Override
    public boolean isPrimitive() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.isPrimitive();
    }

    @Override
    public ItemType getPrimitiveType() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getPrimitiveType();
    }

    @Override
    public List<Item> getEnumerationFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getEnumerationFacet();
    }

    @Override
    public List<String> getConstraintsFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getConstraintsFacet();
    }

    @Override
    public Integer getMinLengthFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMinLengthFacet();
    }

    @Override
    public Integer getLengthFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getLengthFacet();
    }

    @Override
    public Integer getMaxLengthFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMaxLengthFacet();
    }

    @Override
    public Item getMinExclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMinExclusiveFacet();
    }

    @Override
    public Item getMinInclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMinInclusiveFacet();
    }

    @Override
    public Item getMaxExclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMaxExclusiveFacet();
    }

    @Override
    public Item getMaxInclusiveFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getMaxInclusiveFacet();
    }

    @Override
    public Integer getTotalDigitsFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getTotalDigitsFacet();
    }

    @Override
    public Integer getFractionDigitsFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getFractionDigitsFacet();
    }

    @Override
    public TimezoneFacet getExplicitTimezoneFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getExplicitTimezoneFacet();
    }

    @Override
    public WhitespaceFacet getWhitespaceFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getWhitespaceFacet();
    }

    @Override
    public List<String> getPatternFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getPatternFacet();
    }

    @Override
    public OrderedFacetValue getOrderedFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getOrderedFacet();
    }

    @Override
    public Boolean getBoundedFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getBoundedFacet();
    }

    @Override
    public CardinalityFacetValue getCardinalityFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getCardinalityFacet();
    }

    @Override
    public Boolean getNumericFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getNumericFacet();
    }

    @Override
    public List<String> getObjectKeysFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getObjectKeysFacet();
    }

    @Override
    public FieldDescriptor getObjectContentFacet(String key) {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getObjectContentFacet(key);
    }

    @Override
    public List<FieldDescriptor> getObjectContentFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getObjectContentFacet();
    }

    @Override
    public Map<String, FieldDescriptor> getObjectContentFacetAsUnorderedMap() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getObjectContentFacetAsUnorderedMap();
    }

    @Override
    public boolean getClosedFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getClosedFacet();
    }

    @Override
    public ItemType getArrayContentFacet() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getArrayContentFacet();
    }

    @Override
    public List<ItemType> getTypes() {
        if (this.resolvedItemType == null) {
            throw new OurBadException("Unresolved type: " + this.name);
        }
        return this.resolvedItemType.getTypes();
    }

    @Override
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
