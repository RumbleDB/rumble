package org.rumbledb.types;

/**
 * Legal values for the {@code ordered} fundamental facet per
 * <a href="https://www.w3.org/TR/xmlschema11-2/#rf-ordered">XSD 1.1 Part 2 §4.2.1</a>.
 *
 * <ul>
 * <li>{@link #FALSE} – the value space has no inherent order.</li>
 * <li>{@link #PARTIAL} – the value space is partially ordered.</li>
 * <li>{@link #TOTAL} – the value space is totally ordered.</li>
 * </ul>
 */
public enum OrderedFacetValue {
    FALSE,
    PARTIAL,
    TOTAL
}
