package org.rumbledb.compiler;

import org.rumbledb.exceptions.InvalidAssignableVariableComposability;
import org.rumbledb.exceptions.InvalidComposabilityUpdatingAndSequentialExpression;
import org.rumbledb.exceptions.InvalidControlStatementComposability;
import org.rumbledb.exceptions.InvalidUpdatingExpressionCondition;
import org.rumbledb.exceptions.InvalidUpdatingExpressionOperand;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;

/**
 * Visitor checks rules for composing statements based on sequential properties
 * or nesting (e.g., break statements).
 * The visitor passes a node representing the innermost control statement (while or FLWOR), and for some of the rules
 * applies checks to verify if loops enclose the visited node type.
 */
public class ComposabilityVisitor extends AbstractNodeVisitor<Node> {

    @Override
    public Node visitMainModule(MainModule mainModule, Node argument) {
        visit(mainModule.getProlog(), null);
        visit(mainModule.getProgram(), null);
        return argument;
    }

    @Override
    public Node visitProlog(Prolog expression, Node argument) {
        expression.getFunctionDeclarations()
            .forEach(
                functionDeclaration -> visit(
                    functionDeclaration,
                    null
                )
            );
        expression.getVariableDeclarations()
            .forEach(
                variableDeclaration -> visit(
                    variableDeclaration,
                    null
                )
            );
        boolean containsOtherThanNonUpdatingNonSeq = expression.getVariableDeclarations()
            .stream()
            .anyMatch(varDecl -> {
                if (varDecl.getExpression() != null) {
                    return varDecl.getExpression().isUpdating() || varDecl.getExpression().isSequential();
                }
                return false;
            });
        if (containsOtherThanNonUpdatingNonSeq) {
            throw new InvalidComposabilityUpdatingAndSequentialExpression(
                    "We cannot support variable declaration which does not contain non-updating, non-sequential expressions.",
                    expression.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public Node visitProgram(Program expression, Node argument) {
        visit(expression.getStatementsAndOptionalExpr(), argument);
        return argument;
    }

    @Override
    public Node visitStatementsAndExpr(
            StatementsAndExpr statementsAndExpr,
            Node argument
    ) {
        if (statementsAndExpr.getStatements() != null) {
            statementsAndExpr.getStatements().forEach(statement -> visit(statement, argument));
        }
        visit(statementsAndExpr.getExpression(), argument);

        return argument;
    }

    @Override
    public Node visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr,
            Node argument
    ) {
        if (statementsAndOptionalExpr.getStatements() != null) {
            statementsAndOptionalExpr.getStatements().forEach(statement -> visit(statement, argument));
        }
        if (statementsAndOptionalExpr.getExpression() != null) {
            visit(statementsAndOptionalExpr.getExpression(), argument);
        }
        return argument;
    }

    @Override
    public Node visitAssignStatement(AssignStatement expression, Node argument) {
        if (expression.getAssignExpression().isUpdating()) {
            throw new InvalidUpdatingExpressionOperand(
                    "Assign statement expects a non-updating operand.",
                    expression.getMetadata()
            );
        }
        if (!expression.getStaticContext().getIsAssignable(expression.getName())) {
            throw new InvalidAssignableVariableComposability(
                    "Assign statement expects an assignable variable",
                    expression.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public Node visitVariableDeclStatement(
            VariableDeclStatement statement,
            Node argument
    ) {
        if (statement.getVariableExpression() != null && statement.getVariableExpression().isUpdating()) {
            throw new InvalidUpdatingExpressionOperand(
                    "Variable declaration statements expects non-updating operands",
                    statement.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public Node visitWhileStatement(WhileStatement expression, Node argument) {
        if (expression.getTestCondition().isUpdating()) {
            throw new InvalidUpdatingExpressionCondition(
                    "While statement expects non-updating test condition",
                    expression.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public Node visitFlowrStatement(FlowrStatement expression, Node argument) {
        Clause itClause = expression.getReturnStatementClause().getFirstClause();
        while (itClause != null) {
            this.visit(itClause, expression);
            itClause = itClause.getNextClause();
        }
        return argument;
    }

    @Override
    public Node visitConditionalStatement(
            ConditionalStatement expression,
            Node argument
    ) {
        visit(expression.getCondition(), argument);
        if (expression.getCondition().isUpdating()) {
            throw new InvalidUpdatingExpressionCondition(
                    "If statement expects non-updating conditional expression",
                    expression.getMetadata()
            );
        }
        visit(expression.getBranch(), argument);
        visit(expression.getElseBranch(), argument);
        return argument;
    }

    @Override
    public Node visitVariableDeclaration(
            VariableDeclaration expression,
            Node argument
    ) {
        if (expression.external()) {
            // TODO: see if external should have different behavior.
            return argument;
        }
        if (expression.getExpression().isUpdating() || expression.getExpression().isSequential()) {
            throw new InvalidComposabilityUpdatingAndSequentialExpression(
                    "Variable declaration must contain non-updating and non-sequential expression in its assignment!",
                    expression.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public Node visitBreakStatement(BreakStatement expression, Node argument) {
        if (argument == null) {
            // There is no enclosing while or flwor statement
            throw new InvalidControlStatementComposability(
                    "Break statements must be enclosed in while or flwor statements!",
                    expression.getMetadata()
            );
        }
        return argument;
    }

    @Override
    public Node visitContinueStatement(
            ContinueStatement expression,
            Node argument
    ) {
        if (argument == null) {
            // There is no enclosing while or flwor statement
            throw new InvalidControlStatementComposability(
                    "Break statements must be enclosed in while or flwor statements!",
                    expression.getMetadata()
            );
        }
        return argument;
    }

    // TODO: Switch, typeswitch, trycatch outside of master's thesis.
}
