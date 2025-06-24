package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class TruncateCollectionExpression extends Expression {
    private Expression collectionName;

    public TruncateCollectionExpression(
        Expression collectionName,
        ExceptionMetadata metadata
    ) {
        super(metadata);
        if (collectionName == null) {
            throw new OurBadException("collection must be identified for truncation.");
        }
        this.collectionName = collectionName;
    }

    public Expression getCollectionName() {
        return this.collectionName;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.collectionName);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTruncateCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("truncate ");
        sb.append("collection table(");
        this.collectionName.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
    
}