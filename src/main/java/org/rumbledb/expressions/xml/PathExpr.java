package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class PathExpr extends Expression {
    private boolean startsWithDash;
    private List<StepExpr> relativePathExpr;

    public PathExpr(
            boolean startsWithDash,
            List<StepExpr> relativePathExpr,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.startsWithDash = startsWithDash;
        this.relativePathExpr = relativePathExpr;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitPathExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(this.relativePathExpr);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        int dashPathCounter = 0;
        indentIt(sb, indent);
        for (StepExpr relativePathExp : this.relativePathExpr) {
            relativePathExp.serializeToJSONiq(sb, indent);
        }
        sb.append("\n");
    }
}
