package org.rumbledb.runtime.functions.util.formatting;

/**
 * @param distanceFromAnchor Digit signs between the separator and the edge grouping is measured from: the right for an
 *        integer part, the left for a fractional part.
 */
public record GroupingPos(int distanceFromAnchor, int separatorCP) {

    @Override
    public String toString() {
        return Integer.toString(this.distanceFromAnchor);
    }
}
