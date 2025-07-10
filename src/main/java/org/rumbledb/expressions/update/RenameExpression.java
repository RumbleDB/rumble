package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class RenameExpression extends Expression {

    private Expression mainExpression;
    private Expression locatorExpression;
    private Expression nameExpression;

    public RenameExpression(
            Expression mainExpression,
            Expression locatorExpression,
            Expression nameExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a rename expression.");
        }
        if (locatorExpression == null) {
            throw new OurBadException("Locator expression cannot be null in a rename expression.");
        }
        if (nameExpression == null) {
            throw new OurBadException("Name expression cannot be null in a rename expression.");
        }
        this.mainExpression = mainExpression;
        this.locatorExpression = locatorExpression;
        this.nameExpression = nameExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.locatorExpression, this.nameExpression);
    }

    public Expression getMainExpression() {
        return this.mainExpression;
    }

    public Expression getLocatorExpression() {
        return this.locatorExpression;
    }

    public Expression getNameExpression() {
        return this.nameExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitRenameExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("rename json ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        this.locatorExpression.serializeToJSONiq(sb, 0);
        this.nameExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");

    }
}
