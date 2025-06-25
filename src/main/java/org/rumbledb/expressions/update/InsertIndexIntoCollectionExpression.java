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
    private final boolean isTable;
    private final Integer pos;
    private final boolean isLast;

    public InsertIndexIntoCollectionExpression(
        Expression collection,
        Expression contentExpression,
        boolean isTable,
        Integer pos,
        boolean isLast,
        ExceptionMetadata metadata
    ) {
        // TODO: The current implementations only accounts for two callening modes- table, and delta-file
        // Extension to other modes can be done by increasing flags for using enum instead
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for insertion.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for insertion.");
        }
        if (!(pos != null ^ isLast)) {
            throw new OurBadException("Invalid flags passed for insertion order");
        }

        this.collection = collection;
        this.contentExpression = contentExpression;
        this.isTable = isTable;
        this.pos = pos;
        this.isLast = isLast;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public Expression getContentExpression() {
        return this.contentExpression;
    }

    public boolean isTable() {
        return this.isTable;
    }

    public boolean isLast() {
        return this.isLast;
    }

    public Integer getPosition() {
        return this.pos;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.contentExpression, this.collection);
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
            sb.append(" last");
        } else {
            sb.append(" at " + this.pos);
        }
        sb.append(" into collection ");
        sb.append(this.isTable ? "table" : "delta-file");
        sb.append("(");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append(")");
        sb.append("\n");
    }

}