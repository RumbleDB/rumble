package org.rumbledb.types;

import org.rumbledb.api.Item;

import java.util.Collections;
import java.util.List;

/**
 * Facets class represent a container with the ability to get and set facets and is intended to be a mutable proxy that
 * will be passed to a DerivedAtomicType to indicate the specified facets
 */
public class Facets {

    /**
     * @return Facets for the integer derived type
     */
    public static Facets getIntegerFacets() {
        Facets facets = new Facets();
        facets.setFractionDigits(0);
        return facets;
    }

    public static Facets createMinMaxFacets(Item min, Item max, boolean isInclusive) {
        Facets facets = new Facets();
        if (isInclusive) {
            facets.setMinInclusive(min);
            facets.setMaxInclusive(max);
        } else {
            facets.setMinExclusive(min);
            facets.setMaxExclusive(max);
        }
        return facets;
    }

    public static Facets createMinFacets(Item min, boolean isInclusive) {
        Facets facets = new Facets();
        if (isInclusive) {
            facets.setMinInclusive(min);
        } else {
            facets.setMinExclusive(min);
        }
        return facets;
    }

    public static Facets createMaxFacets(Item max, boolean isInclusive) {
        Facets facets = new Facets();
        if (isInclusive) {
            facets.setMaxInclusive(max);
        } else {
            facets.setMaxExclusive(max);
        }
        return facets;
    }

    public static Facets createTimezoneFacets(TimezoneFacet explicitTimezone) {
        Facets facets = new Facets();
        facets.setExplicitTimezone(explicitTimezone);
        return facets;
    }

    private Item minInclusive, maxInclusive;
    private Item minExclusive, maxExclusive;
    private Integer minLength, length, maxLength, totalDigits, fractionDigits;
    private List<String> constraints = Collections.emptyList();
    private List<Item> enumeration;
    private TimezoneFacet explicitTimezone;

    public Facets() {

    }

    public Item getMinInclusive() {
        return this.minInclusive;
    }

    public void setMinInclusive(Item minInclusive) {
        this.minInclusive = minInclusive;
    }

    public Item getMaxInclusive() {
        return this.maxInclusive;
    }

    public void setMaxInclusive(Item maxInclusive) {
        this.maxInclusive = maxInclusive;
    }

    public Item getMinExclusive() {
        return this.minExclusive;
    }

    public void setMinExclusive(Item minExclusive) {
        this.minExclusive = minExclusive;
    }

    public Item getMaxExclusive() {
        return this.maxExclusive;
    }

    public void setMaxExclusive(Item maxExclusive) {
        this.maxExclusive = maxExclusive;
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getMinLength() {
        return this.minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getTotalDigits() {
        return this.totalDigits;
    }

    public void setTotalDigits(Integer totalDigits) {
        this.totalDigits = totalDigits;
    }

    public Integer getFractionDigits() {
        return this.fractionDigits;
    }

    public void setFractionDigits(Integer fractionDigits) {
        this.fractionDigits = fractionDigits;
    }

    public List<String> getConstraints() {
        return this.constraints;
    }

    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

    public List<Item> getEnumeration() {
        return this.enumeration;
    }

    public void setEnumeration(List<Item> enumeration) {
        this.enumeration = enumeration;
    }

    public TimezoneFacet getExplicitTimezone() {
        return this.explicitTimezone;
    }

    public void setExplicitTimezone(TimezoneFacet explicitTimezone) {
        this.explicitTimezone = explicitTimezone;
    }

    public static Facets createAtomicTypeFacets(
            Integer length,
            List<Item> enumeration,
            Integer minLength,
            Integer maxLength,
            Item minInclusive,
            Item maxInclusive,
            Item minExclusive,
            Item maxExclusive,
            Integer totalDigits,
            Integer fractionDigits
    ) {
        Facets facets = new Facets();
        facets.length = length;
        facets.enumeration = enumeration;
        facets.minLength = minLength;
        facets.maxLength = maxLength;
        facets.minInclusive = minInclusive;
        facets.maxInclusive = maxInclusive;
        facets.minExclusive = minExclusive;
        facets.maxExclusive = maxExclusive;
        facets.totalDigits = totalDigits;
        facets.fractionDigits = fractionDigits;
        return facets;
    }
}
