package org.rumbledb.expressions.update;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

@Getter
public class DeleteExpression extends Expression {

    private Expression mainExpression;
    private Expression locatorExpression;

    public DeleteExpression(
            Expression mainExpression, Expression locatorExpression, ExceptionMetadata metadata) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a delete expression.");
        }
        if (locatorExpression == null) {
            throw new OurBadException("Locator expression cannot be null in a delete expression.");
        }
        this.mainExpression = mainExpression;
        this.locatorExpression = locatorExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.locatorExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDeleteExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("delete json ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        this.locatorExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
