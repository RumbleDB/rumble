package org.rumbledb.runtime.functions.base.formatting;

public final class GroupingPos {
    // TODO refactor to remove String separator and completely replace by integer codepoints then remove overloaded
    // constructor
    public final int distanceFromRight;
    public final String separator;
    public final int separatorCP;

    public GroupingPos(int distanceFromRight, String separator) {
        this.distanceFromRight = distanceFromRight;
        this.separator = separator;
        this.separatorCP = -1;
    }

    public GroupingPos(int distanceFromRight, int separator) {
        this.distanceFromRight = distanceFromRight;
        this.separator = null;
        this.separatorCP = separator;
    }

    int getDistanceFromRight() {
        return distanceFromRight;
    }

    @Override
    public String toString() {
        return Integer.toString(this.distanceFromRight);
    }
}
