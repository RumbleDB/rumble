package org.rumbledb.expressions.xml;

import org.rumbledb.expressions.xml.axis.AxisStep;

public class Dash {
    private boolean getRoot;
    private AxisStep axisStep;

    public Dash(boolean getRoot, AxisStep axisStep) {
        this.getRoot = getRoot;
        this.axisStep = axisStep;
    }

    public Dash(boolean getRoot) {
        this.getRoot = getRoot;
        this.axisStep = null;
    }

    public AxisStep getAxisStep() {
        return axisStep;
    }

    public boolean requiresRoot() {
        return getRoot;
    }

    @Override
    public String toString() {
        // TODO: add root function call
        StringBuffer sb = new StringBuffer();
        this.axisStep.serializeToJSONiq(sb, 0);
        return sb.toString();
    }
}
