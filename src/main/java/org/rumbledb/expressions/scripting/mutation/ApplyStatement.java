package org.rumbledb.expressions.scripting.mutation;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.Collections;
import java.util.List;

public class ApplyStatement extends Statement {
    private final Expression applyExpression;

    public ApplyStatement(
            Expression applyExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.applyExpression = applyExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitApplyStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.applyExpression);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.applyExpression.serializeToJSONiq(sb, 0);
    }

    public Expression getApplyExpression() {
        return this.applyExpression;
    }
}
