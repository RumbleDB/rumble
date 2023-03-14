package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.List;

public class RenameExpression extends Expression {

    private Expression mainExpression;
    private Expression finalExpression;
    private Expression nameExpression;
    protected RenameExpression(Expression mainExpression,
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
        return null;
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {

    }
}
