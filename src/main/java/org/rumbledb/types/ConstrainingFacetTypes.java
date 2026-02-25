package org.rumbledb.types;

/**
 * Constraining facet kinds per XSD 1.1 Part 2 §4.3.
 * Constraining facets restrict the value or lexical space during type derivation.
 *
 * Entries CONTENT, CLOSED, METADATA, and CONSTRAINTS are JSONiq-specific
 * extensions used by object/array types.
 **/
public enum ConstrainingFacetTypes {
    LENGTH("length"),
    MINLENGTH("minLength"),
    MAXLENGTH("maxLength"),
    MININCLUSIVE("minInclusive"),
    MAXINCLUSIVE("maxInclusive"),
    MINEXCLUSIVE("minExclusive"),
    MAXEXCLUSIVE("maxExclusive"),
    TOTALDIGITS("totalDigits"),
    FRACTIONDIGITS("fractionDigits"),
    EXPLICITTIMEZONE("explicitTimezone"),

    CONTENT("content"),
    CLOSED("closed"),

    WHITESPACE("whiteSpace"),
    PATTERN("pattern"),

    ENUMERATION("enumeration"),
    METADATA("metadata"),
    CONSTRAINTS("constraints");

    private String facetName;

    ConstrainingFacetTypes(String facetName) {
        this.facetName = facetName;
    }

    public String getTypeName() {
        return this.facetName;
    }
}
