package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.update.primitives.Mode;

import java.util.Arrays;
import java.util.List;

public class TruncateCollectionExpression extends Expression {
    private Expression collectionName;
    private Mode mode;

    public TruncateCollectionExpression(
            Expression collectionName,
            Mode mode,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (collectionName == null) {
            throw new OurBadException("collection must be identified for truncation.");
        }
        this.mode = mode;
        this.collectionName = collectionName;
    }

    public Expression getCollectionName() {
        return this.collectionName;
    }

    public Mode getMode() {
        return this.mode;
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
