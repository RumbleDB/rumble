package org.rumbledb.expressions.xml;

public enum Dash {
    SINGLE_DASH("/"),
    DOUBLE_DASH("//");

    private final String dashValue;

    Dash(String dashValue) {
        this.dashValue = dashValue;
    }

    public String getDashValue() {
        return dashValue;
    }
}
