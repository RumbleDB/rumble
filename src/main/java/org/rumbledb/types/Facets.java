package org.rumbledb.types;

import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.rumbledb.api.Item;

/**
 * Facets class represent a container with the ability to get and set facets and is intended to be a
 * mutable proxy that will be passed to a DerivedAtomicType to indicate the specified facets
 */
@Getter
public class Facets {

    /**
     * @return Facets for the integer derived type (fractionDigits=0, pattern per XSD 1.1 §3.4.13)
     */
    public static Facets getIntegerFacets() {
        Facets facets = new Facets();
        facets.setFractionDigits(0);
        facets.setPattern(Collections.singletonList("[\\-+]?[0-9]+"));
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
        // XSD 1.1 §4.2.2.1 / §4.2.3.1: both min+max bounds → bounded=true, cardinality=finite
        facets.setBounded(true);
        facets.setCardinality(CardinalityFacetValue.FINITE);
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

    @Setter private Item minInclusive;
    @Setter private Item maxInclusive;
    @Setter private Item minExclusive;
    @Setter private Item maxExclusive;
    private Integer minLength;
    private Integer length;
    private Integer maxLength;
    private Integer totalDigits;
    @Setter private Integer fractionDigits;
    private List<String> constraints = Collections.emptyList();
    private List<Item> enumeration;
    @Setter private TimezoneFacet explicitTimezone;
    @Setter private WhitespaceFacet whiteSpace;
    @Setter private List<String> pattern;

    // Fundamental facets (XSD 1.1 §4.2)
    private OrderedFacetValue ordered;
    @Setter private Boolean bounded;
    @Setter private CardinalityFacetValue cardinality;
    private Boolean numeric;

    /**
     * Creates a Facets with the given pattern regex strings. Multiple patterns in a single
     * derivation step are OR-ed per XSD 1.1 §4.3.4.2.
     *
     * @param patterns regex strings for this derivation step
     * @return Facets containing the pattern
     */
    public static Facets createPatternFacets(List<String> patterns) {
        Facets facets = new Facets();
        facets.setPattern(patterns);
        return facets;
    }

    public static Facets createWhitespaceFacets(WhitespaceFacet whiteSpace) {
        Facets facets = new Facets();
        facets.setWhiteSpace(whiteSpace);
        return facets;
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
            Integer fractionDigits) {
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
