package org.rumbledb.types;

public enum FacetTypes {
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

    FacetTypes(String facetName) {
        this.facetName = facetName;
    }

    public String getTypeName() {
        return this.facetName;
    }
}
