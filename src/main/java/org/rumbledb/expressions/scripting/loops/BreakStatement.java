package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class BreakStatement extends Statement {
    private Statement stoppingStatement;

    public BreakStatement(ExceptionMetadata metadata) {
        super(metadata);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitBreakStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.stoppingStatement.serializeToJSONiq(sb, indent);
    }

    /**
     * @return the Statement that the break must stop at runtime.
     */
    public Statement getStoppingStatement() {
        return stoppingStatement;
    }

    public void setStoppingStatement(Statement stoppingStatement) {
        this.stoppingStatement = stoppingStatement;
    }
}
