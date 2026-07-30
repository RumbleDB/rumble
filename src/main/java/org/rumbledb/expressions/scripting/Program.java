package org.rumbledb.expressions.scripting;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;

import java.util.Collections;
import java.util.List;

/**
 * A program is the first-class citizen in JSONiq scripting syntax. A program
 * corresponds to the body of a block expression.
 *
 * The final expression may be omitted. In this case, the final expression is
 * considered the empty expression.
 *
 * The result of a program is the result of executing, if present, an
 * expression.
 *
 * A program forms a tree of statements and possibly an expression.
 */
public class Program extends Node {
    private final StatementsAndOptionalExpr statementsAndOptionalExpr;

    public Program(StatementsAndOptionalExpr statementsAndOptionalExpr, ExceptionMetadata metadata) {
        super(metadata);
        this.statementsAndOptionalExpr = statementsAndOptionalExpr;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitProgram(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.statementsAndOptionalExpr);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        this.statementsAndOptionalExpr.serializeToJSONiq(sb, indent);
    }

    public StatementsAndOptionalExpr getStatementsAndOptionalExpr() {
        return this.statementsAndOptionalExpr;
    }
}
