package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class PathExpr extends Expression {
    private final List<IntermediaryPath> relativePathExpr;

    public PathExpr(
            List<IntermediaryPath> relativePathExpr,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.relativePathExpr = relativePathExpr;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitPathExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        for (IntermediaryPath path : this.relativePathExpr) {
            if (path.getPreStepExprDash() != null && path.getPreStepExprDash().getAxisStep() != null) {
                result.add(path.getPreStepExprDash().getAxisStep());
            }
            result.add(path.getStepExpr());
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        for (IntermediaryPath path : this.relativePathExpr) {
            if (path.getPreStepExprDash() == null) {
                path.getStepExpr().serializeToJSONiq(sb, indent);
            } else {
                sb.append(path.getPreStepExprDash().toString());
                path.getStepExpr().serializeToJSONiq(sb, indent);
            }
        }
        sb.append("\n");
    }

    public List<IntermediaryPath> getIntermediaryPaths() {
        return this.relativePathExpr;
    }
}
