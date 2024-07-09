package org.rumbledb.expressions.scripting.loops;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FLWOR_CLAUSES;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.Collections;
import java.util.List;

public class FlowrStatement extends Statement {
    private final ReturnStatementClause returnStatementClause;

    public FlowrStatement(
            ReturnStatementClause returnStatementClause,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        Clause startClause = returnStatementClause.getFirstClause();
        if (
            startClause.getClauseType() != FLWOR_CLAUSES.FOR
                &&
                startClause.getClauseType() != FLWOR_CLAUSES.LET
        ) {
            throw new SemanticException("FLOWR clause must starts with a FOR or a LET\n", this.getMetadata());
        }
        this.returnStatementClause = returnStatementClause;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitFlowrStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.returnStatementClause);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        this.returnStatementClause.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }

    public ReturnStatementClause getReturnStatementClause() {
        return returnStatementClause;
    }
}
