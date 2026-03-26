package org.rumbledb.runtime.functions.base.formatting;

public final class GroupingPos {
    public final int distanceFromRight;
    public final String separator;

    public GroupingPos(int distanceFromRight, String separator) {
        this.distanceFromRight = distanceFromRight;
        this.separator = separator;
    }

    int getDistanceFromRight() {
        return distanceFromRight;
    }
}
