package org.rumbledb.types;

import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.Name;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSchemaException;

import java.util.List;
import java.util.Set;

public class DerivedAtomicItemType implements ItemType {

    private static final long serialVersionUID = 1L;

    private final ItemType baseType;
    private ItemType primitiveType;
    private int typeTreeDepth;
    private final boolean isUserDefined;
    private final Name name;
    private Item minInclusive, maxInclusive, minExclusive, maxExclusive;
    private Integer minLength, length, maxLength, totalDigits, fractionDigits;
    private List<String> constraints;
    private List<Item> enumeration;
    private TimezoneFacet explicitTimezone;

    DerivedAtomicItemType(Name name, ItemType baseType, ItemType primitiveType, Facets facets) {
        this(name, baseType, primitiveType, facets, true);
    }
    // TODO : turn builtin derived atomic types into this class

    DerivedAtomicItemType(
            Name name,
            ItemType baseType,
            ItemType primitiveType,
            Facets facets,
            boolean isUserDefined
    ) {
        // TODO : check in item factory that: name not already used or invalid, facets are correct and allowed according
        // to baseType
        this.name = name;
        this.baseType = baseType;
        this.primitiveType = primitiveType;
        this.isUserDefined = isUserDefined;
        this.typeTreeDepth = baseType.getTypeTreeDepth() + 1;

        this.minInclusive = facets.getMinInclusive();
        this.maxInclusive = facets.getMaxInclusive();
        this.minExclusive = facets.getMinExclusive();
        this.maxExclusive = facets.getMaxExclusive();

        this.minLength = facets.getMinLength();
        this.length = facets.getLength();
        this.maxLength = facets.getMaxLength();
        this.totalDigits = facets.getTotalDigits();
        this.fractionDigits = facets.getFractionDigits();

        this.explicitTimezone = facets.getExplicitTimezone();

        this.constraints = facets.getConstraints();
        this.enumeration = facets.getEnumeration();

        if (this.baseType.isResolved()) {
            processBaseType();
        }

    }

    DerivedAtomicItemType(
            Name name,
            ItemType baseType,
            Facets facets,
            boolean isUserDefined
    ) {
        // TODO : check in item factory that: name not already used or invalid, facets are correct and allowed according
        // to baseType
        this(name, baseType, null, facets, isUserDefined);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return isEqualTo((ItemType) other);
    }

    @Override
    public boolean isAtomicItemType() {
        return true;
    }

    @Override
    public boolean isNumeric() {
        return this.primitiveType.isNumeric();
    }

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public int getTypeTreeDepth() {
        return this.typeTreeDepth;
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        // TODO: what about further restrictions like string without num from int?
        return this.primitiveType.isStaticallyCastableAs(other.isPrimitive() ? other : other.getPrimitiveType());
    }

    @Override
    public boolean canBePromotedTo(ItemType other) {
        // TODO : how about restriction types
        if (other.equals(BuiltinTypesCatalogue.stringItem)) {
            return this.isSubtypeOf(BuiltinTypesCatalogue.stringItem)
                || this.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem);
        }
        if (other.equals(BuiltinTypesCatalogue.doubleItem)) {
            return this.isNumeric();
        }
        return false;
    }

    @Override
    public boolean isUserDefined() {
        return this.isUserDefined;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public ItemType getPrimitiveType() {
        return this.primitiveType;
    }

    @Override
    public ItemType getBaseType() {
        return this.baseType;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        return this.primitiveType.getAllowedFacets();
    }

    @Override
    public List<Item> getEnumerationFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.ENUMERATION)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the enumeration facet"
            );
        }
        return this.enumeration == null ? this.baseType.getEnumerationFacet() : this.enumeration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getConstraintsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.CONSTRAINTS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the constraints facet"
            );
        }
        return ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    @Override
    public Integer getMinLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MINLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum length facet"
            );
        }
        return this.minLength == null ? this.baseType.getMinLengthFacet() : this.minLength;
    }

    @Override
    public Integer getLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.LENGTH)) {
            throw new UnsupportedOperationException(this.toString() + " item type does not support the length facet");
        }
        return this.length == null ? this.baseType.getLengthFacet() : this.length;
    }

    @Override
    public Integer getMaxLengthFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXLENGTH)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum length facet"
            );
        }
        return this.maxLength == null ? this.baseType.getMaxLengthFacet() : this.maxLength;
    }

    @Override
    public Item getMinExclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MINEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum exclusive facet"
            );
        }
        return this.minExclusive == null ? this.baseType.getMinExclusiveFacet() : this.minExclusive;
    }

    @Override
    public Item getMinInclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MININCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the minimum inclusive facet"
            );
        }
        return this.minInclusive == null ? this.baseType.getMinInclusiveFacet() : this.minInclusive;
    }

    @Override
    public Item getMaxExclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXEXCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum exclusive facet"
            );
        }
        return this.maxExclusive == null ? this.baseType.getMaxExclusiveFacet() : this.maxExclusive;
    }

    @Override
    public Item getMaxInclusiveFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.MAXINCLUSIVE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the maximum inclusive facet"
            );
        }
        return this.maxInclusive == null ? this.baseType.getMaxInclusiveFacet() : this.maxInclusive;
    }

    @Override
    public Integer getTotalDigitsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.TOTALDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the total digits facet"
            );
        }
        return this.totalDigits == null ? this.baseType.getTotalDigitsFacet() : this.totalDigits;
    }

    @Override
    public Integer getFractionDigitsFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.FRACTIONDIGITS)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the fraction digits facet"
            );
        }
        return this.fractionDigits == null ? this.baseType.getFractionDigitsFacet() : this.fractionDigits;
    }

    @Override
    public TimezoneFacet getExplicitTimezoneFacet() {
        if (!this.getAllowedFacets().contains(FacetTypes.EXPLICITTIMEZONE)) {
            throw new UnsupportedOperationException(
                    this.toString() + " item type does not support the explicit timezone facet"
            );
        }
        return this.explicitTimezone == null ? this.baseType.getExplicitTimezoneFacet() : this.explicitTimezone;
    }

    @Override
    public String getIdentifierString() {
        if (this.hasName()) {
            return this.name.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#anonymous-atomic-base{");
        sb.append(this.baseType.getIdentifierString());
        sb.append("}");

        if (this.minLength != null) {
            sb.append("-ml:");
            sb.append(this.minLength);
        }
        if (this.length != null) {
            sb.append("-l:");
            sb.append(this.length);
        }
        if (this.maxLength != null) {
            sb.append("-Ml:");
            sb.append(this.maxLength);
        }

        if (this.totalDigits != null) {
            sb.append("-td:");
            sb.append(this.totalDigits);
        }
        if (this.fractionDigits != null) {
            sb.append("-fd:");
            sb.append(this.fractionDigits);
        }

        if (this.minInclusive != null) {
            sb.append("-mi:");
            sb.append(this.minInclusive.serialize());
        }
        if (this.minExclusive != null) {
            sb.append("-me:");
            sb.append(this.minExclusive.serialize());
        }
        if (this.maxInclusive != null) {
            sb.append("-Mi:");
            sb.append(this.maxInclusive.serialize());
        }
        if (this.maxExclusive != null) {
            sb.append("-Me:");
            sb.append(this.maxExclusive.serialize());
        }

        if (this.explicitTimezone != null) {
            sb.append("-et:");
            sb.append(this.explicitTimezone.name());
        }


        if (this.enumeration != null) {
            sb.append("-enum{");
            String comma = "";
            for (Item item : this.enumeration) {
                sb.append(comma);
                sb.append(item.serialize());
                comma = ",";
            }
            sb.append("}");
        }

        if (this.constraints.size() > 0) {
            sb.append("-const{");
            String comma = "";
            for (String c : this.constraints) {
                sb.append(comma);
                sb.append("\"");
                sb.append(c);
                sb.append("\"");
                comma = ",";
            }
            sb.append("}");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        // TODO : Consider added facets restriction and base type
        return this.name.toString();
    }

    @Override
    public boolean isDataFrameType() {
        return true;
    }

    @Override
    public boolean isCompatibleWithDataFrames() {
        return true;
    }

    @Override
    public void resolve(DynamicContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
    }

    @Override
    public void resolve(StaticContext context, ExceptionMetadata metadata) {
        if (!this.baseType.isResolved()) {
            this.baseType.resolve(context, metadata);
            processBaseType();
        }
    }

    public void processBaseType() {
        this.typeTreeDepth = this.baseType.getTypeTreeDepth() + 1;
        if (this.baseType.isAtomicItemType()) {
            this.primitiveType = this.baseType.getPrimitiveType();


            if (this.minLength == null) {
                this.minLength = this.baseType.getMinLengthFacet();
            } else {
                if (!this.primitiveType.getAllowedFacets().contains(FacetTypes.MINLENGTH)) {
                    throw new InvalidSchemaException(
                            "This facet is not applicable ...",
                            ExceptionMetadata.EMPTY_METADATA
                    );
                }
                if (this.minLength < this.baseType.getMinLengthFacet()) {
                    throw new InvalidSchemaException("Out of bounds", ExceptionMetadata.EMPTY_METADATA);
                }
            }
            if (this.maxLength == null) {
                this.maxLength = this.baseType.getMaxLengthFacet();
            }
            if (this.enumeration == null) {
                this.enumeration = this.baseType.getEnumerationFacet();
            }
            // TODO: for all facets
            // Check
            return;
        }
        throw new InvalidSchemaException(
                "The base type of a user-defined atomic type must be an atomic type.",
                ExceptionMetadata.EMPTY_METADATA
        );
    }
}
