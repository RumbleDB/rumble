package org.rumbledb.expressions.scripting.mutation;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.Collections;
import java.util.List;

public class AssignStatement extends Statement {
    private final Expression assignExpression;
    private final Name name;

    public AssignStatement(
            Expression assignExpression,
            Name name,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.assignExpression = assignExpression;
        this.name = name;
    }


    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAssignStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.assignExpression);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.assignExpression.serializeToJSONiq(sb, indent);
    }

    public Expression getAssignExpression() {
        return this.assignExpression;
    }

    public Name getName() {
        return this.name;
    }
}
