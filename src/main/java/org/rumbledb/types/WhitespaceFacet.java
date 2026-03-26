package org.rumbledb.types;

/**
 * Represents the three legal values of the {@code whiteSpace} constraining facet
 * as defined in <a href="https://www.w3.org/TR/xmlschema11-2/#rf-whiteSpace">XSD 1.1 Part 2, Section 4.3.6</a>.
 *
 * <blockquote>
 * "whiteSpace constrains the value space of types derived from string such that
 * the various behaviors specified in Attribute Value Normalization in XML are realized.
 * The value of whiteSpace must be one of {preserve, replace, collapse}."
 * </blockquote>
 *
 * <ul>
 * <li>{@link #PRESERVE} – "No normalization is done, the value is not changed
 * (this is the behavior required by XML for element content)"</li>
 * <li>{@link #REPLACE} – "All occurrences of #x9 (tab), #xA (line feed) and
 * #xD (carriage return) are replaced with #x20 (space)"</li>
 * <li>{@link #COLLAPSE} – "After the processing implied by replace, contiguous
 * sequences of #x20's are collapsed to a single #x20, and any #x20 at the
 * start or end of the string is then removed."</li>
 * </ul>
 *
 * <p>
 * Applicability rules from the spec:
 * </p>
 * <blockquote>
 * "whiteSpace is applicable to all atomic and list datatypes. For all atomic datatypes
 * other than string (and types derived by facet-based restriction from it) the value of
 * whiteSpace is collapse and cannot be changed by a schema author; for string the value
 * of whiteSpace is preserve; for any type derived by facet-based restriction from string
 * the value of whiteSpace can be any of the three legal values (as long as the value is
 * at least as restrictive as the value of the base type)."
 * </blockquote>
 *
 * <p>
 * The enum constants are declared in restriction order ({@code PRESERVE} &lt;
 * {@code REPLACE} &lt; {@code COLLAPSE}) so that {@link #ordinal()} comparisons
 * correctly enforce the constraint that a derived type may only make the facet
 * <em>more</em> restrictive, never less.
 * </p>
 *
 * @see <a href="https://www.w3.org/TR/xmlschema11-2/#rf-whiteSpace">XSD 1.1 Part 2 §4.3.6 whiteSpace</a>
 */
public enum WhitespaceFacet {
    PRESERVE,
    REPLACE,
    COLLAPSE
}
