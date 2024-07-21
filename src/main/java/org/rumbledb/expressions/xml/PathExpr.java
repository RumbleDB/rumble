package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class PathExpr extends Node {
    private boolean startsWithDash;
    private List<StepExpr> relativePathExpr;
    private List<Dash> dashes;

    public PathExpr(
            boolean startsWithDash,
            List<StepExpr> relativePathExpr,
            List<Dash> dashes,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.startsWithDash = startsWithDash;
        this.relativePathExpr = relativePathExpr;
        this.dashes = dashes;
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
        if (startsWithDash) {
            sb.append(this.dashes.get(dashPathCounter).getDashValue());
        }
        dashPathCounter++;
        while (dashPathCounter < this.dashes.size()) {
            sb.append(this.dashes.get(dashPathCounter).getDashValue());
            this.relativePathExpr.get(dashPathCounter).serializeToJSONiq(sb, indent);
        }
        if (dashPathCounter < this.relativePathExpr.size()) {
            this.relativePathExpr.get(dashPathCounter).serializeToJSONiq(sb, indent);
        }
        sb.append("\n");
    }
}
