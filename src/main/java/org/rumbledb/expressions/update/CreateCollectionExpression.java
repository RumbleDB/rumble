package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class CreateCollectionExpression extends Expression {
    private Expression collection;
    private Expression contentExpression;
    private boolean isTable;

    public CreateCollectionExpression(
        Expression collection,
        Expression contentExpression,
        boolean isTable,
        ExceptionMetadata metadata
    ) {
        // TODO: The current implementations only accounts for two callening modes- table, and delta-file
        // Extension to other modes can be done by increasing flags for using enum instead
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for creation.");
        }
        if (contentExpression == null) {
            throw new OurBadException("Content must be specified for creating collection.");
        }
        this.collection = collection;
        this.contentExpression = contentExpression;
        this.isTable = isTable;
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

    @Override
    public List<Node> getChildren() {
        // Need expressions.Node.getChildren be modified to return List<Object>?
        // That will allow appending this.isTable to the children
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
        sb.append(this.isTable ? "table" : "delta-file");
        sb.append("(");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append(")");
        sb.append(" with ");
        this.contentExpression.serializeToJSONiq(sb, 1);
        sb.append("\n");
    }

}