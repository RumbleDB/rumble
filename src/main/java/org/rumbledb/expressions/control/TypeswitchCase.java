package org.rumbledb.expressions.control;


import org.rumbledb.context.Name;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a helper class that organizes the children expressions of a typeswitch expression.
 * From a tree perspective, all expressions in there are considered
 * to be direct children of the TypeswitchExpression.
 */
public class TypeswitchCase {

    private Name variableName;
    private List<SequenceType> union;
    private final Expression returnExpression;

    public TypeswitchCase(
            Name variableName,
            List<SequenceType> union,
            Expression returnExpression
    ) {
        this.variableName = variableName;
        this.union = new ArrayList<>(union);
        this.returnExpression = returnExpression;
    }

    public TypeswitchCase(
            Name variableName,
            Expression returnExpression
    ) {
        this.variableName = variableName;
        this.union = null;
        this.returnExpression = returnExpression;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public List<SequenceType> getUnion() {
        return this.union;
    }

    public Expression getReturnExpression() {
        return this.returnExpression;
    }

}
