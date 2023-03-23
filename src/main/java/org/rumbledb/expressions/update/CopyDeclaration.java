package org.rumbledb.expressions.update;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.types.SequenceType;

public class CopyDeclaration {

    private Name variableName;
    private Expression sourceExpression;
    private SequenceType sequenceType;

    public CopyDeclaration(Name variableName,
                           Expression sourceExpression
    ) {
        if (variableName == null) {
            throw new IllegalArgumentException("Copy clause var decls cannot be empty");
        }
        this.variableName = variableName;
        this.sourceExpression = sourceExpression;
    }

    public Name getVariableName() {
        return variableName;
    }

    public Expression getSourceExpression() {
        return sourceExpression;
    }

    public SequenceType getSourceSequenceType() {
        return sourceExpression.getStaticSequenceType();
    }

}
