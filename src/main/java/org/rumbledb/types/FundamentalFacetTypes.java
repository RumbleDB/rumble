package org.rumbledb.types;

/**
 * Fundamental facet kinds per
 * <a href="https://www.w3.org/TR/xmlschema11-2/#rf-fund-facets">XSD 1.1 Part 2 §4.2</a>.
 *
 * Fundamental facets provide metadata about a datatype (ordered, bounded,
 * cardinality, numeric). Unlike constraining facets, they do not restrict the
 * value space but describe inherent properties of the type.
 */
public enum FundamentalFacetTypes {
    ORDERED("ordered"),
    BOUNDED("bounded"),
    CARDINALITY("cardinality"),
    NUMERIC("numeric");

    private String facetName;

    FundamentalFacetTypes(String facetName) {
        this.facetName = facetName;
    }

    public String getTypeName() {
        return this.facetName;
    }
}
