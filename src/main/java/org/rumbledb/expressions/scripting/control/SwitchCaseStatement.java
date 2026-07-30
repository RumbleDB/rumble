package org.rumbledb.expressions.scripting.control;

import lombok.Getter;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.List;

/**
 * Helper class that organizes children statements of a switch statement.
 * From a tree perspective, all statements in this class are considered to
 * be direct children of the SwitchStatement.
 */
@Getter
public class SwitchCaseStatement {
    private final List<Expression> conditionExpressions;
    private final Statement returnStatement;

    public SwitchCaseStatement(List<Expression> conditionExpressions, Statement returnStatement) {
        this.conditionExpressions = conditionExpressions;
        this.returnStatement = returnStatement;
    }

}
