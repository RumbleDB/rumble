package org.rumbledb.expressions.scripting.statement;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class StatementsAndExpr extends Expression {
    protected StaticContext staticContext;
    private final List<Statement> statements;
    private final Expression expression;

    public StatementsAndExpr(List<Statement> statements, Expression expression, ExceptionMetadata metadata) {
        super(metadata);
        // An empty statements list should initialize an empty list for safety.
        if (statements == null) {
            this.statements = new ArrayList<>();
        } else {
            this.statements = statements;
        }
        if (expression == null) {
            throw new OurBadException("Expression cannot be null when statements and expression are needed.");
        }
        this.expression = expression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStatementsAndExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (!this.statements.isEmpty()) {
            this.statements.forEach(statement -> {
                if (statement != null) {
                    result.add(statement);
                }
            });
        }
        result.add(this.expression);
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.statements.forEach(statement -> {
            statement.serializeToJSONiq(sb, 0);
            sb.append(" ");
        });
        this.expression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }

    public List<Statement> getStatements() {
        return this.statements;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public StaticContext getStaticContext() {
        return this.staticContext;
    }

    public void setStaticContext(StaticContext staticContext) {
        this.staticContext = staticContext;
    }
}
