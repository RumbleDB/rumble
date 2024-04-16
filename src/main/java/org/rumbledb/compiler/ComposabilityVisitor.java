package org.rumbledb.compiler;

import org.rumbledb.compiler.wrapper.BlockExpressionMetadata;
import org.rumbledb.exceptions.InvalidAssignableVariableComposability;
import org.rumbledb.exceptions.InvalidComposabilityUpdatingAndSequentialExpression;
import org.rumbledb.exceptions.InvalidControlStatementComposability;
import org.rumbledb.exceptions.InvalidUpdatingExpressionCondition;
import org.rumbledb.exceptions.InvalidUpdatingExpressionOperand;
import org.rumbledb.expressions.AbstractNodeVisitor;
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
 * The visitor passes a pair to the descendents:
 * - the first element of the pair is the innermost block expression (function or program).
 * - the second element of the pair is the innermost FLWOR or While statement. Null is expected if there is none.
 */
public class ComposabilityVisitor extends AbstractNodeVisitor<BlockExpressionMetadata> {

    @Override
    public BlockExpressionMetadata visitMainModule(MainModule mainModule, BlockExpressionMetadata argument) {
        visit(mainModule.getProlog(), argument);
        visit(mainModule.getProgram(), new BlockExpressionMetadata(mainModule.getProlog(), null));
        return argument;
    }

    @Override
    public BlockExpressionMetadata visitProlog(Prolog expression, BlockExpressionMetadata argument) {
        expression.getFunctionDeclarations()
            .forEach(
                functionDeclaration -> visit(
                    functionDeclaration,
                    new BlockExpressionMetadata(expression, null)
                )
            );
        expression.getVariableDeclarations()
            .forEach(
                variableDeclaration -> visit(
                    variableDeclaration,
                    new BlockExpressionMetadata(expression, null)
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
        return new BlockExpressionMetadata(expression, null);
    }

    @Override
    public BlockExpressionMetadata visitProgram(Program expression, BlockExpressionMetadata argument) {
        visit(expression.getStatementsAndOptionalExpr(), argument);
        return argument;
    }

    @Override
    public BlockExpressionMetadata visitStatementsAndExpr(
            StatementsAndExpr statementsAndExpr,
            BlockExpressionMetadata argument
    ) {
        if (statementsAndExpr.getStatements() != null) {
            statementsAndExpr.getStatements().forEach(statement -> visit(statement, argument));
        }
        visit(statementsAndExpr.getExpression(), argument);

        return argument;
    }

    @Override
    public BlockExpressionMetadata visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr statementsAndOptionalExpr,
            BlockExpressionMetadata argument
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
    public BlockExpressionMetadata visitAssignStatement(AssignStatement expression, BlockExpressionMetadata argument) {
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
    public BlockExpressionMetadata visitVariableDeclStatement(
            VariableDeclStatement statement,
            BlockExpressionMetadata argument
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
    public BlockExpressionMetadata visitWhileStatement(WhileStatement expression, BlockExpressionMetadata argument) {
        if (expression.getTestCondition().isUpdating()) {
            throw new InvalidUpdatingExpressionCondition(
                    "While statement expects non-updating test condition",
                    expression.getMetadata()
            );
        }
        return new BlockExpressionMetadata(argument.getInnerMostBlock(), expression);
    }

    @Override
    public BlockExpressionMetadata visitFlowrStatement(FlowrStatement expression, BlockExpressionMetadata argument) {
        Clause itClause = expression.getReturnStatementClause().getFirstClause();
        while (itClause != null) {
            this.visit(itClause, new BlockExpressionMetadata(argument.getInnerMostBlock(), expression));
            itClause = itClause.getNextClause();
        }
        return new BlockExpressionMetadata(argument.getInnerMostBlock(), expression);
    }

    @Override
    public BlockExpressionMetadata visitConditionalStatement(
            ConditionalStatement expression,
            BlockExpressionMetadata argument
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
    public BlockExpressionMetadata visitVariableDeclaration(
            VariableDeclaration expression,
            BlockExpressionMetadata argument
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
    public BlockExpressionMetadata visitBreakStatement(BreakStatement expression, BlockExpressionMetadata argument) {
        if (argument.getInnerMostLoopStatement() == null) {
            // There is no enclosing while or flwor statement
            throw new InvalidControlStatementComposability(
                    "Break statements must be enclosed in while or flwor statements!",
                    expression.getMetadata()
            );
        }
        if (argument.getInnerMostLoopStatement() instanceof WhileStatement) {
            expression.setStoppingStatement((WhileStatement) argument.getInnerMostLoopStatement());
        } else if (argument.getInnerMostLoopStatement() instanceof FlowrStatement) {
            expression.setStoppingStatement((FlowrStatement) argument.getInnerMostLoopStatement());
        }
        return argument;
    }

    @Override
    public BlockExpressionMetadata visitContinueStatement(
            ContinueStatement expression,
            BlockExpressionMetadata argument
    ) {
        if (argument.getInnerMostLoopStatement() == null) {
            // There is no enclosing while or flwor statement
            throw new InvalidControlStatementComposability(
                    "Break statements must be enclosed in while or flwor statements!",
                    expression.getMetadata()
            );
        }
        if (argument.getInnerMostLoopStatement() instanceof WhileStatement) {
            expression.setContinueStatement((WhileStatement) argument.getInnerMostLoopStatement());
        } else if (argument.getInnerMostLoopStatement() instanceof FlowrStatement) {
            expression.setContinueStatement((FlowrStatement) argument.getInnerMostLoopStatement());
        }
        return argument;
    }

    // TODO: Switch, typeswitch, trycatch outside of master's thesis.
}
