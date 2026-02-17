package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.update.primitives.Mode;

import java.util.Arrays;
import java.util.List;

public class CreateCollectionExpression extends Expression {
    private Expression collection;
    private Expression contentExpression;
    private Mode mode;

    public CreateCollectionExpression(
            Expression collection,
            Expression contentExpression,
            Mode mode,
            ExceptionMetadata metadata
    ) {
        // Currently supports HIVE/DELTA/ICEBERG
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for creation.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for creating collection.");
        }
        this.collection = collection;
        this.contentExpression = contentExpression;
        this.mode = mode;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    public Mode getMode() {
        return this.mode;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.contentExpression, this.collection);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCreateCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("create collection ");
        if (this.mode == Mode.HIVE) {
            sb.append("table");
        } else if (this.mode == Mode.DELTA) {
            sb.append("delta-file");
        } else if (this.mode == Mode.ICEBERG) {
            sb.append("iceberg-table");
        }
        sb.append("(");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append(")");
        if (!(this.contentExpression instanceof CommaExpression)) {
            // collection is created non-empty
            sb.append(" with ");
            this.contentExpression.serializeToJSONiq(sb, 1);
            sb.append("\n");
        }
    }

}
