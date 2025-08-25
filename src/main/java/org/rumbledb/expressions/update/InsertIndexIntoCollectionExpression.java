package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class InsertIndexIntoCollectionExpression extends Expression {
    private final Expression collection;
    private final Expression contentExpression;
    private final Expression pos;
    private final boolean isTable;
    private final boolean isFirst;
    private final boolean isLast;

    public InsertIndexIntoCollectionExpression(
            Expression collection,
            Expression contentExpression,
            Expression pos,
            boolean isTable,
            boolean isFirst,
            boolean isLast,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for insertion.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for insertion.");
        }

        this.collection = collection;
        this.contentExpression = contentExpression;
        this.isTable = isTable;
        this.pos = pos;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    public Expression getPosition() {
        return this.pos;
    }

    public boolean isTable() {
        return this.isTable;
    }

    public boolean isLast() {
        return this.isLast;
    }

    public boolean isFirst() {
        return this.isFirst;
    }

    @Override
    public List<Node> getChildren() {
        return this.pos != null
            ? Arrays.asList(this.contentExpression, this.collection, this.pos)
            : Arrays.asList(this.contentExpression, this.collection);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitInsertIndexIntoCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("insert ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        if (this.isLast) {
            sb.append(" last ");
        } else if (this.isFirst) {
            sb.append(" first ");
        } else {
            sb.append(" at ");
            this.pos.serializeToJSONiq(sb, 1);
        }
        sb.append(" into collection ");
        sb.append(this.isTable ? "table" : "delta-file");
        sb.append("(");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append(")");
        sb.append("\n");
    }

}
