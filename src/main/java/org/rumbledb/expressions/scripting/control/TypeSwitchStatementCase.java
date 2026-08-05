package org.rumbledb.expressions.scripting.control;

import java.util.List;

import lombok.Getter;

import org.rumbledb.context.Name;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.types.SequenceType;

/**
 * This is a helper class that organizes the children statements of a TypeSwitchStatement. From a
 * tree perspective, all statements in there are considered to be direct children of the
 * TypeSwitchStatement.
 */
@Getter
public class TypeSwitchStatementCase {
    private final Name variableName;
    private final List<SequenceType> union;
    private final Statement returnStatement;

    public TypeSwitchStatementCase(
            Name variableName, List<SequenceType> union, Statement returnStatement) {
        this.variableName = variableName;
        this.union = union;
        this.returnStatement = returnStatement;
    }

    public TypeSwitchStatementCase(Name variableName, Statement returnStatement) {
        this.variableName = variableName;
        this.union = null;
        this.returnStatement = returnStatement;
    }
}
