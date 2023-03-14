package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.List;

public class InsertExpression extends Expression {
    public InsertExpression(ExceptionMetadata metadata) {
        super(metadata);
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
