package org.rumbledb.expressions.scripting.block;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;

import java.util.Collections;
import java.util.List;

public class BlockExpression extends Expression {
    private final StatementsAndExpr statementsAndExpr;

    public BlockExpression(StatementsAndExpr statementsAndExpr, ExceptionMetadata metadata) {
        super(metadata);
        this.statementsAndExpr = statementsAndExpr;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitBlockExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.statementsAndExpr);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.statementsAndExpr.serializeToJSONiq(sb, 0);
    }

    public StatementsAndExpr getStatementsAndExpr() {
        return this.statementsAndExpr;
    }

    public List<Statement> getStatements() {
        return this.statementsAndExpr.getStatements();
    }

    public Expression getExpression() {
        return this.statementsAndExpr.getExpression();
    }
}
