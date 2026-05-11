package org.rumbledb.expressions.xml.axis;

public enum ForwardAxis {
    CHILD("child::"),
    DESCENDANT("descendant::"),
    ATTRIBUTE("attribute::"),
    SELF("self::"),
    DESCENDANT_OR_SELF("descendant-or-self::"),
    FOLLOWING_SIBLING("following-sibling::"),
    FOLLOWING("following::");

    private final String axisValue;

    ForwardAxis(String axisValue) {
        this.axisValue = axisValue;
    }

    public String getAxisValue() {
        return this.axisValue;
    }

    public static ForwardAxis fromString(String text) {
        for (ForwardAxis forwardAxis : ForwardAxis.values()) {
            if (forwardAxis.axisValue.equalsIgnoreCase(text)) {
                return forwardAxis;
            }
        }
        throw new IllegalArgumentException("No constant with text: " + text + " found!");
    }
}
