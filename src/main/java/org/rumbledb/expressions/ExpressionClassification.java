package org.rumbledb.expressions;

/**
 * An ExpressionClassification classifies an expression under 4 possible classifications.
 *
 * A BASIC_UPDATING expression is classified as 1 of 6 expressions in the update package, that can alter the state of
 * an existing node.
 *
 * An UPDATING expression is classified as a BASIC_EXPRESSION or any expression (excluding a TransformExpression) that
 * directly contains an UPDATING expression and that can alter the state of an existing node.
 *
 * A SIMPLE expression is classified as an expression that is not an updating expression.
 *
 * A VACUOUS expression follows the definition of the XQuery Update Facility 1.0, but is largely classified as
 * an expression that can be determined statically to return an empty sequence or raise an error.
 */
public enum ExpressionClassification {
    UNSET,
    BASIC_UPDATING,
    UPDATING,
    SIMPLE,
    VACUOUS;

    public boolean isUnset() {
        return this == ExpressionClassification.UNSET;
    }

    public boolean isUpdating() {
        return this == ExpressionClassification.BASIC_UPDATING || this == ExpressionClassification.UPDATING;
    }

    public boolean isSimple() {
        return this == ExpressionClassification.SIMPLE;
    }

    public boolean isVacuous() {
        return this == ExpressionClassification.VACUOUS;
    }

    public String toString() {
        switch (this) {
            case UNSET:
                return "unset";
            case BASIC_UPDATING:
                return "basic_updating";
            case UPDATING:
                return "updating";
            case SIMPLE:
                return "simple";
            case VACUOUS:
                return "vacuous";
        }
        return null;
    }
}
