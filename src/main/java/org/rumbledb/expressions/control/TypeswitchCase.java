package org.rumbledb.expressions.control;


import org.rumbledb.expressions.Expression;
import sparksoniq.semantics.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class that organizes the children expressions of a typeswitch expression.
 * From a tree perspective, all expressions in there are considered
 * to be direct children of the TypeswitchExpression.
 */
public class TypeswitchCase {

    private String variableName;
    private List<SequenceType> union;
    private final Expression returnExpression;

    public TypeswitchCase(
            String variableName,
            List<SequenceType> union,
            Expression returnExpression
    ) {
        this.variableName = variableName;
        this.union = new ArrayList<>(union);
        this.returnExpression = returnExpression;
    }

    public TypeswitchCase(
            String variableName,
            Expression returnExpression
    ) {
        this.variableName = variableName;
        this.union = null;
        this.returnExpression = returnExpression;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public List<SequenceType> getUnion() {
        return this.union;
    }

    public Expression getReturnExpression() {
        return this.returnExpression;
    }

}
