package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class ReturnStatementClause extends Clause {
    private final Statement returnStatement;

    public ReturnStatementClause(
            Statement returnStatement,
            ExceptionMetadata metadata
    ) {
        super(FLWOR_CLAUSES.RETURN, metadata);
        this.returnStatement = returnStatement;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitReturnStatementClause(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.returnStatement != null) {
            result.add(this.returnStatement);
        }
        result.add(this.getPreviousClause());
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("return ");
        this.returnStatement.serializeToJSONiq(sb, 0);
    }

    public Statement getReturnStatement() {
        return returnStatement;
    }
}
