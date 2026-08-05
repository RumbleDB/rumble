package org.rumbledb.expressions.scripting;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;

/**
 * A program is the first-class citizen in JSONiq scripting syntax. A program corresponds to the
 * body of a block expression.
 *
 * <p>The final expression may be omitted. In this case, the final expression is considered the
 * empty expression.
 *
 * <p>The result of a program is the result of executing, if present, an expression.
 *
 * <p>A program forms a tree of statements and possibly an expression.
 */
@Getter
public class Program extends Node {
    private final StatementsAndOptionalExpr statementsAndOptionalExpr;

    public Program(
            StatementsAndOptionalExpr statementsAndOptionalExpr, ExceptionMetadata metadata) {
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
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        this.statementsAndOptionalExpr.serializeToJSONiq(sb, indent);
    }

    public boolean isSequential() {
        return this.statementsAndOptionalExpr.isSequential();
    }

    public boolean isUpdating() {
        return this.statementsAndOptionalExpr.isUpdating();
    }
}
