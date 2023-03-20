package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class RenameExpression extends Expression {

    private Expression mainExpression;
    private Expression locatorExpression;
    private Expression nameExpression;
    private UpdateLocatorKind locatorKind;
    public RenameExpression(Expression mainExpression,
                               Expression locatorExpression,
                               Expression nameExpression,
                               UpdateLocatorKind locatorKind,
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
        if (locatorKind == null) {
            throw new OurBadException("Locator kind cannot be null in a rename expression.");
        }
        if (!locatorKind.isObjectLookup()) {
            throw new OurBadException("Locator kind must be Object Lookup");
        }
        this.mainExpression = mainExpression;
        this.locatorExpression = locatorExpression;
        this.nameExpression = nameExpression;
        this.locatorKind = locatorKind;
        this.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.locatorExpression, this.nameExpression);
    }

    public Expression getMainExpression() {
        return mainExpression;
    }

    public Expression getLocatorExpression() {
        return locatorExpression;
    }

    public Expression getNameExpression() {
        return nameExpression;
    }

    public UpdateLocatorKind getLocatorKind() {
        return locatorKind;
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
        this.locatorExpression.serializeToJSONiq(sb,0);
        this.nameExpression.serializeToJSONiq(sb,0);
        sb.append("\n");

    }
}
