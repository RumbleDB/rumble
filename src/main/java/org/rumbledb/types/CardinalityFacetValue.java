package org.rumbledb.types;

/**
 * Legal values for the {@code cardinality} fundamental facet per
 * <a href="https://www.w3.org/TR/xmlschema11-2/#rf-cardinality">XSD 1.1 Part 2 §4.2.3</a>.
 *
 * <ul>
 * <li>{@link #FINITE} – the value space is finite.</li>
 * <li>{@link #COUNTABLY_INFINITE} – the value space is countably infinite.</li>
 * </ul>
 */
public enum CardinalityFacetValue {
    FINITE,
    COUNTABLY_INFINITE
}
