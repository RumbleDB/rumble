package org.rumbledb.expressions.xml.axis;

public enum ReverseAxis {
    PARENT("parent"),
    ANCESTOR("ancestor"),
    PRECEDING_SIBLING("preceding-sibling"),
    PRECEDING("preceding"),
    ANCESTOR_OR_SELF("ancestor-or-self");

    private final String axisValue;

    ReverseAxis(String axisValue) {
        this.axisValue = axisValue;
    }

    public String getAxisValue() {
        return axisValue;
    }
}
