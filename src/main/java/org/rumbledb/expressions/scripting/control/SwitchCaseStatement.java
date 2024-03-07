package org.rumbledb.expressions.scripting.control;

import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that organizes children statements of a switch statement.
 * From a tree perspective, all statements in this class are considered to
 * be direct children of the SwitchStatement.
 */
public class SwitchCaseStatement {
    private final List<Statement> conditionStatements;
    private final Statement returnStatement;

    public SwitchCaseStatement(List<Statement> conditionStatements, Statement returnStatement) {
        this.conditionStatements = conditionStatements;
        this.returnStatement = returnStatement;
    }

    public List<Statement> getAllStatements() {
        List<Statement> result = new ArrayList<>(this.conditionStatements);
        result.add(this.returnStatement);
        return result;
    }

    public List<Statement> getConditionStatements() {
        return this.conditionStatements;
    }

    public Statement getReturnStatement() {
        return this.returnStatement;
    }
}
