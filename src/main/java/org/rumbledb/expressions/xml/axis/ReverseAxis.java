package org.rumbledb.expressions.xml.axis;

public enum ReverseAxis {
    PARENT("parent::"),
    ANCESTOR("ancestor::"),
    PRECEDING_SIBLING("preceding-sibling::"),
    PRECEDING("preceding::"),
    ANCESTOR_OR_SELF("ancestor-or-self::");

    private final String axisValue;

    ReverseAxis(String axisValue) {
        this.axisValue = axisValue;
    }

    public String getAxisValue() {
        return axisValue;
    }

    public static ReverseAxis fromString(String text) {
        for (ReverseAxis forwardAxis : ReverseAxis.values()) {
            if (forwardAxis.axisValue.equalsIgnoreCase(text)) {
                return forwardAxis;
            }
        }
        throw new IllegalArgumentException("No constant with text: " + text + " found!");
    }
}
