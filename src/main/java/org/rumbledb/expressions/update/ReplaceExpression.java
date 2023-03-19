package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class ReplaceExpression extends Expression {

    private Expression mainExpression;
    private Expression locatorExpression;
    private Expression newExpression;
    private UpdateLocatorKind locatorKind;
    public ReplaceExpression(Expression mainExpression,
                             Expression locatorExpression,
                             Expression newExpression,
                             UpdateLocatorKind locatorKind,
                             ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a replace expression.");
        }
        if (locatorExpression == null) {
            throw new OurBadException("Locator expression cannot be null in a replace expression.");
        }
        if (newExpression == null) {
            throw new OurBadException("New expression cannot be null in a replace expression.");
        }
        if (locatorKind == null) {
            throw new OurBadException("Locator kind cannot be null in a replace expression.");
        }
        this.mainExpression = mainExpression;
        this.locatorExpression = locatorExpression;
        this.newExpression = newExpression;
        this.locatorKind = locatorKind;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.locatorExpression, this.newExpression);
    }

    public Expression getMainExpression() {
        return mainExpression;
    }

    public Expression getLocatorExpression() {
        return locatorExpression;
    }

    public Expression getNewExpression() {
        return newExpression;
    }

    public UpdateLocatorKind getLocatorKind() {
        return locatorKind;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitReplaceExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("replace json value of ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        this.locatorExpression.serializeToJSONiq(sb,0);
        sb.append(" with ");
        this.newExpression.serializeToJSONiq(sb,0);
        sb.append("\n");
    }
}
