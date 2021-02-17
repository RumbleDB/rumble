package org.rumbledb.types;

import org.apache.commons.collections.ListUtils;
import org.rumbledb.api.Item;
import org.rumbledb.context.Name;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UserDefinedAtomicItemType implements ItemType {

    private final ItemType baseType;
    private final Name name;
    private final Item minInclusive, maxInclusive, minExclusive, maxExclusive;
    private final Integer minLength, length, maxLength, totalDigits, fractionDigits;
    private final List<String> constraints;
    private final List<Item> enumeration;
    private final TimezoneFacet explicitTimezone;

    UserDefinedAtomicItemType(Name name, ItemType baseType, HashMap<FacetTypes, Object> facets) {
        // TODO : check in item factory that: name not already used or invalid, facets are correct and allowed according to baseType
        this.name = name;
        this.baseType = baseType;

        this.minInclusive = (Item) facets.get(FacetTypes.MININCLUSIVE);
        this.maxInclusive = (Item) facets.get(FacetTypes.MAXINCLUSIVE);
        this.minExclusive = (Item) facets.get(FacetTypes.MINEXCLUSIVE);
        this.maxExclusive = (Item) facets.get(FacetTypes.MAXEXCLUSIVE);

        this.minLength = (Integer) facets.get(FacetTypes.MINLENGTH);
        this.length = (Integer) facets.get(FacetTypes.LENGTH);
        this.maxLength = (Integer) facets.get(FacetTypes.MAXLENGTH);
        this.totalDigits = (Integer) facets.get(FacetTypes.TOTALDIGITS);
        this.fractionDigits = (Integer) facets.get(FacetTypes.FRACTIONDIGITS);

        this.explicitTimezone = (TimezoneFacet) facets.get(FacetTypes.EXPLICITTIMEZONE);

        this.constraints = (List<String>) facets.getOrDefault(FacetTypes.CONSTRAINTS, Collections.emptyList());
        this.enumeration = (List<Item>) facets.get(FacetTypes.ENUMERATION);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ItemType)) {
            return false;
        }
        return this.toString().equals(other.toString());
    }

    @Override
    public boolean isAtomicItemType() {
        return true;
    }

    @Override
    public boolean hasName() {
        return true;
    }

    @Override
    public Name getName() {
        return this.name;
    }

    @Override
    public boolean isSubtypeOf(ItemType superType) {
        return this.equals(superType) || superType.equals(this.baseType) || this.baseType.isSubtypeOf(superType);
    }

    @Override
    public ItemType findLeastCommonSuperTypeWith(ItemType other) {
        if(this.isSubtypeOf(other)){
            return other;
        } else if(other.isSubtypeOf(this)){
            return this;
        } else {
            return this.baseType.findLeastCommonSuperTypeWith(other.isUserDefined() ? other.getBaseType() : other);
        }
    }

    @Override
    public boolean isStaticallyCastableAs(ItemType other) {
        // TODO: what about further restrictions like string without num from int?
        ItemType castFrom = this.baseType;
        while(castFrom.isUserDefined()){
            castFrom = castFrom.getBaseType();
        }
        ItemType castTo = other;
        while (castTo.isUserDefined()){
            castTo = castTo.getBaseType();
        }
        return castFrom.isStaticallyCastableAs(castTo);
    }

    @Override
    public boolean canBePromotedTo(ItemType other) {
        // TODO : how about restriction types
        if (other.equals(BuiltinTypesCatalogue.stringItem)) {
            return this.isSubtypeOf(BuiltinTypesCatalogue.stringItem) || this.isSubtypeOf(BuiltinTypesCatalogue.anyURIItem);
        }
        if (other.equals(BuiltinTypesCatalogue.doubleItem)) {
            return this.isNumeric();
        }
        return false;
    }

    @Override
    public boolean isUserDefined() {
        return true;
    }

    @Override
    public ItemType getBaseType() {
        return this.baseType;
    }

    @Override
    public Set<FacetTypes> getAllowedFacets() {
        throw new UnsupportedOperationException("only non-user-defined types specify allowed facets");
    }

    public List<Item> getEnumerationFacet(){
        return this.enumeration == null ? this.baseType.getEnumerationFacet() : this.enumeration;
    }

    public List<String> getConstraintsFacet(){
        return ListUtils.union(this.baseType.getConstraintsFacet(), this.constraints);
    }

    public Integer getMinLengthFacet(){
        return this.minLength == null ? this.baseType.getMinLengthFacet() : this.minLength;
    }

    public Integer getLengthFacet(){
        return this.length == null ? this.baseType.getLengthFacet() : this.length;
    }

    public Integer getMaxLengthFacet(){
        return this.maxLength == null ? this.baseType.getMaxLengthFacet() : this.maxLength;
    }

    public Item getMinExclusiveFacet(){
        return this.minExclusive == null ? this.baseType.getMinExclusiveFacet() : this.minExclusive;
    }

    public Item getMinInclusiveFacet(){
        return this.minInclusive == null ? this.baseType.getMinInclusiveFacet() : this.minInclusive;
    }

    public Item getMaxExclusiveFacet(){
        return this.maxExclusive == null ? this.baseType.getMaxExclusiveFacet() : this.maxExclusive;
    }

    public Item getMaxInclusiveFacet(){
        return this.maxInclusive == null ? this.baseType.getMaxInclusiveFacet() : this.maxInclusive;
    }

    public Integer getTotalDigitsFacet(){
        return this.totalDigits == null ? this.baseType.getTotalDigitsFacet() : this.totalDigits;
    }

    public Integer getFractionDigitsFacet(){
        return this.fractionDigits == null ? this.baseType.getFractionDigitsFacet() : this.fractionDigits;
    }

    public TimezoneFacet getExplicitTimezoneFacet(){
        return this.explicitTimezone == null ? this.baseType.getExplicitTimezoneFacet() : this.explicitTimezone;
    }

    @Override
    public String toString() {
        // TODO : Consider added facets restriction and base type
        return this.name.toString();
    }
}
