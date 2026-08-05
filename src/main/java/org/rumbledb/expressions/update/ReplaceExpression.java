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
public class ReplaceExpression extends Expression {

    private Expression mainExpression;
    private Expression locatorExpression;
    private Expression replacerExpression;

    public ReplaceExpression(
            Expression mainExpression,
            Expression locatorExpression,
            Expression replacerExpression,
            ExceptionMetadata metadata) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a replace expression.");
        }
        if (locatorExpression == null) {
            throw new OurBadException("Locator expression cannot be null in a replace expression.");
        }
        if (replacerExpression == null) {
            throw new OurBadException(
                    "New replacer expression cannot be null in a replace expression.");
        }
        this.mainExpression = mainExpression;
        this.locatorExpression = locatorExpression;
        this.replacerExpression = replacerExpression;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.locatorExpression, this.replacerExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitReplaceExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append("replace json value of ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        this.locatorExpression.serializeToJSONiq(sb, 0);
        sb.append(" with ");
        this.replacerExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
