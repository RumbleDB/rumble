package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.List;

public class DeleteIndexFromCollectionExpression extends Expression {
    private Expression collection;
    private int numDelete;
    private boolean isFirst;
    private boolean isTable;

    public DeleteIndexFromCollectionExpression(
            Expression collection,
            int numDelete,
            boolean isFirst,
            boolean isTable,
            ExceptionMetadata metadata
    ) {
        // TODO: The current implementations only accounts for two callening modes- table, and delta-file
        // Extension to other modes can be done by increasing flags for using enum instead
        super(metadata);
        if (collection == null) {
            throw new OurBadException("Collection must be identified for indexed Deletion.");
        }
        this.collection = collection;
        this.numDelete = numDelete;
        this.isFirst = isFirst;
        this.isTable = isTable;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public boolean isTable() {
        return this.isTable;
    }

    public boolean isFirst() {
        return this.isFirst;
    }

    public int getNumDelete() {
        return this.numDelete;
    }

    @Override
    public List<Node> getChildren() {
        return Arrays.asList(this.collection);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDeleteIndexFromCollectionExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("delete ");
        sb.append(this.isFirst ? "first " : "last ");
        sb.append(this.numDelete);
        sb.append(" from collection ");
        this.collection.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }

}
