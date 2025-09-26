package org.rumbledb.expressions.scripting.statement;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class StatementsAndOptionalExpr extends Expression {
    protected StaticContext staticContext;
    private final List<Statement> statements;
    private final Expression expression;

    public StatementsAndOptionalExpr(ExceptionMetadata metadata) {
        super(metadata);
        this.statements = new ArrayList<>();
        this.expression = new CommaExpression(metadata);
    }

    public StatementsAndOptionalExpr(
            List<Statement> statements,
            Expression expression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        // An empty statements list should initialize an empty list for safety.
        if (statements == null) {
            this.statements = new ArrayList<>();
        } else {
            this.statements = statements;
        }
        // An empty expression should be a comma expression to avoid null pointer exceptions.
        if (expression == null) {
            this.expression = new CommaExpression(metadata);
        } else {
            this.expression = expression;
        }
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStatementsAndOptionalExpr(this, argument);
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
        if (this.expression != null) {
            result.add(this.expression);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        for (int i = 0; i < this.statements.size(); ++i) {
            this.statements.get(i).serializeToJSONiq(sb, 0);
            if (i == this.statements.size() - 1 && this.expression == null) {
                sb.append("\n");
            } else {
                sb.append(", ");
            }
        }
        if (this.expression != null) {
            this.expression.serializeToJSONiq(sb, 0);
            sb.append('\n');
        }
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
