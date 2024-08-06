package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.primary.FunctionCallExpression;

import java.util.ArrayList;
import java.util.List;

public class PathExpr extends Expression {
    private final List<Expression> relativePathExpressions;
    private final FunctionCallExpression fetchRootFunction;

    public PathExpr(
            FunctionCallExpression fetchRootFunction,
            List<Expression> relativePathExpressions,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.fetchRootFunction = fetchRootFunction;
        this.relativePathExpressions = relativePathExpressions;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitPathExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(this.relativePathExpressions);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        int pathCounter = 0;
        indentIt(sb, indent);
        if (this.fetchRootFunction != null) {
            sb.append("/");
        }
        for (Expression stepExpr : this.relativePathExpressions) {
            stepExpr.serializeToJSONiq(sb, indent);
            if (++pathCounter < this.relativePathExpressions.size()) {
                sb.append("/");
            }
        }
        sb.append("\n");
    }

    public List<Expression> getRelativePathExpressions() {
        return this.relativePathExpressions;
    }

    public boolean needsRoot() {
        return this.fetchRootFunction != null;
    }

    public FunctionCallExpression getFetchRootFunction() {
        return fetchRootFunction;
    }
}
