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
    private Expression finalExpression;
    private Expression nameExpression;
    public RenameExpression(Expression mainExpression,
                               Expression finalExpression,
                               Expression nameExpression,
                               ExceptionMetadata metadata
    ) {
        super(metadata);
        if (mainExpression == null) {
            throw new OurBadException("Main expression cannot be null in a rename expression.");
        }
        if (finalExpression == null) {
            throw new OurBadException("Final expression cannot be null in a rename expression.");
        }
        if (nameExpression == null) {
            throw new OurBadException("Name expression cannot be null in a rename expression.");
        }
        this.mainExpression = mainExpression;
        this.finalExpression = finalExpression;
        this.nameExpression = nameExpression;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitRenameExpression(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.mainExpression, this.finalExpression, this,nameExpression);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("rename ");
        this.mainExpression.serializeToJSONiq(sb, 0);
        sb.append("(");
        this.finalExpression.serializeToJSONiq(sb,0);
        sb.append(") as ");
        this.nameExpression.serializeToJSONiq(sb,0);
        sb.append("\n");

    }
}
