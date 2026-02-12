package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.runtime.update.primitives.Mode;

import java.util.Arrays;
import java.util.List;

public class DeleteIndexFromCollectionExpression extends Expression {
    private Expression collection;
    private Expression numDelete;
    private boolean isFirst;
    private Mode mode;

    public DeleteIndexFromCollectionExpression(
            Expression collection,
            Expression numDelete,
            boolean isFirst,
            Mode mode,
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
        this.mode = mode;
    }

    public Expression getCollection() {
        return this.collection;
    }

    public Expression getNumDelete() {
        return this.numDelete;
    }

    public Mode getMode() {
        return this.mode;
    }

    public boolean isFirst() {
        return this.isFirst;
    }

    @Override
    public List<Node> getChildren() {
        return this.numDelete == null ? Arrays.asList(this.collection) : Arrays.asList(this.collection, this.numDelete);
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
