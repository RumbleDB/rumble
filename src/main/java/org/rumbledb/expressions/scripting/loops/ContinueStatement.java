package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class ContinueStatement extends Statement {
    private Statement continueStatement;

    public ContinueStatement(ExceptionMetadata metadata) {
        super(metadata);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitContinueStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.continueStatement.serializeToJSONiq(sb, indent);
    }

    /**
     * @return the Statement that must be interrupted.
     */
    public Statement getContinueStatement() {
        return continueStatement;
    }

    public void setContinueStatement(Statement continueStatement) {
        this.continueStatement = continueStatement;
    }
}
