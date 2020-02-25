package org.rumbledb.expressions.control;


import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.flowr.FlworVarSequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class that organizes the children expressions of a typeswitch expression.
 * From a tree perspective, all expressions in there are considered
 * to be direct children of the TypeswitchExpression.
 */
public class TypeswitchCase {

    private String variableName;
    private List<FlworVarSequenceType> union = null;
    private final Expression returnExpression;

    public TypeswitchCase(
            String variableName,
            List<FlworVarSequenceType> union,
            Expression returnExpression
    ) {
        this.variableName = variableName;
        union = new ArrayList<>(union);
        this.returnExpression = returnExpression;
    }

    public TypeswitchCase(
            String variableName,
            Expression returnExpression
    ) {
        this.variableName = variableName;
        this.returnExpression = returnExpression;
    }

    public String getVariableName() {
        return this.variableName;
    }

    public List<FlworVarSequenceType> getUnion() {
        return this.union;
    }

    public Expression getReturnExpression() {
        return this.returnExpression;
    }

    public List<Expression> getAllExpressions() {
        List<Expression> result = new ArrayList<>();
        if (this.returnExpression != null)
            result.add(this.returnExpression);
        return result;
    }

}
