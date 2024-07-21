package org.rumbledb.expressions.xml.axis;

public enum ForwardAxis {
    CHILD("child"),
    DESCENDANT("descendant"),
    ATTRIBUTE("attribute"),
    SELF("self"),
    DESCENDANT_OR_SELF("descendant-or-self"),
    FOLLOWING_SIBLING("following-sibling"),
    FOLLOWING("following");

    private final String axisValue;

    ForwardAxis(String axisValue) {
        this.axisValue = axisValue;
    }

    public String getAxisValue() {
        return axisValue;
    }
}
