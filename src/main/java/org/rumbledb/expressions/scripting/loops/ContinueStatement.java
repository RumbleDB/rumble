package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.List;

public class ContinueStatement extends Statement {
    public ContinueStatement(ExceptionMetadata metadata) {
        super(metadata);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitContinueStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return null;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {

    }
}
