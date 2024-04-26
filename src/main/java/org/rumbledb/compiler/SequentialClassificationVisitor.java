package org.rumbledb.compiler;

import org.rumbledb.compiler.wrapper.SequentialDescendant;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.InvalidSequentialChildInNonSequentialParent;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.typing.TreatExpression;

import static org.rumbledb.expressions.module.Prolog.getFunctionDeclarationFromProlog;

/**
 * This visitor is used to classify expressions into sequential/non-sequential.
 *
 * It passes an instance of SequentialDescendant to its descendants to keep a reference to the parent node and to its
 * sequential property.
 */
public class SequentialClassificationVisitor extends AbstractNodeVisitor<SequentialDescendant> {

    protected SequentialDescendant defaultAction(Node node, SequentialDescendant argument) {
        SequentialDescendant result = this.visitDescendants(node, argument);
        if (node instanceof Expression) {
            ((Expression) node).setSequential(result.isSequential());
        } else if (node instanceof Statement) {
            ((Statement) node).setSequential(result.isSequential());
        }
        return result;
    }

    @Override
    public SequentialDescendant visitDescendants(Node node, SequentialDescendant argument) {
        /*
         * Look for children that are sequential without interrupt statement. If the child is sequential due to
         * a break or continue, it should not propagate the sequential property to the parent.
         */
        SequentialDescendant result = new SequentialDescendant(false, false, argument.getProlog());;
        for (Node child : node.getChildren()) {
            SequentialDescendant childResult = visit(child, argument);
            if (childResult.isSequential() && !childResult.hasInterruptStatement()) {
                result = childResult;
            } else if (childResult.isSequential() && !result.isSequential()) {
                result = childResult;
            }
        }
        return result;
    }

    @Override
    public SequentialDescendant visitMainModule(MainModule node, SequentialDescendant argument) {
        SequentialDescendant visitProlog = this.visit(node.getProlog(), argument);
        SequentialDescendant visitProgram = this.visit(
            node.getProgram(),
            new SequentialDescendant(argument, node.getProlog())
        );
        return new SequentialDescendant(
                visitProlog.isSequential() || visitProgram.isSequential(),
                false,
                null
        );
    }

    @Override
    public SequentialDescendant visitProlog(Prolog node, SequentialDescendant argument) {
        node.getImportedModules()
            .forEach(
                libraryModule -> visit(libraryModule, new SequentialDescendant(argument, node))
            );
        node.getFunctionDeclarations()
            .forEach(
                functionDeclaration -> visit(
                    functionDeclaration,
                    new SequentialDescendant(argument, node)
                )
            );
        node.getVariableDeclarations()
            .forEach(
                variableDeclaration -> visit(
                    variableDeclaration,
                    new SequentialDescendant(argument, node)
                )
            );
        return argument;
    }

    @Override
    public SequentialDescendant visitProgram(Program node, SequentialDescendant argument) {
        return visit(node.getStatementsAndOptionalExpr(), argument);
    }

    @Override
    public SequentialDescendant visitFunctionCall(FunctionCallExpression expression, SequentialDescendant argument) {
        FunctionDeclaration targetFunction = getFunctionDeclarationFromProlog(
            argument.getProlog(),
            expression.getFunctionIdentifier()
        );
        if (targetFunction == null) {
            expression.setSequential(false);
            return new SequentialDescendant(false, false, argument.getProlog());

        }
        InlineFunctionExpression functionBody = (InlineFunctionExpression) targetFunction.getExpression();
        if (functionBody.isSequential()) {
            expression.setSequential(true);
            return new SequentialDescendant(true, false, argument.getProlog());
        }
        expression.setSequential(false);
        return new SequentialDescendant(false, false, argument.getProlog());
    }


    @Override
    public SequentialDescendant visitAssignStatement(AssignStatement statement, SequentialDescendant argument) {
        StaticContext statementContext = statement.getStaticContext();
        int variableBlockLevel = statementContext.getVariableBlockLevel(statement.getName());
        if (variableBlockLevel < statementContext.getBlockLevel()) {
            // Variable is defined outside of this block
            statement.setSequential(true);
            return new SequentialDescendant(true, false, argument.getProlog());
        }
        statement.setSequential(false);
        return new SequentialDescendant(false, false, argument.getProlog());
    }

    @Override
    public SequentialDescendant visitApplyStatement(ApplyStatement statement, SequentialDescendant argument) {
        statement.setSequential(
            statement.getApplyExpression().isUpdating() || statement.getApplyExpression().isSequential()
        );
        return new SequentialDescendant(
                statement.isSequential(),
                false,
                argument.getProlog()
        );
    }

    @Override
    public SequentialDescendant visitBreakStatement(BreakStatement statement, SequentialDescendant argument) {
        statement.setSequential(true);
        return new SequentialDescendant(true, true, argument.getProlog());
    }

    @Override
    public SequentialDescendant visitContinueStatement(ContinueStatement statement, SequentialDescendant argument) {
        statement.setSequential(true);
        return new SequentialDescendant(true, true, argument.getProlog());
    }

    @Override
    public SequentialDescendant visitExitStatement(ExitStatement statement, SequentialDescendant argument) {
        statement.setSequential(true);
        return new SequentialDescendant(true, false, argument.getProlog());
    }

    @Override
    public SequentialDescendant visitFunctionDeclaration(
            FunctionDeclaration expression,
            SequentialDescendant argument
    ) {
        InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) expression.getExpression();
        ExitStatement exitStatementInstance = null;
        boolean isFunctionSequential = false;
        boolean hasNonExitStatementSequentialChild = false;
        // Visit each statement to check if it is sequential and non-exit statement.
        for (Statement statement : inlineFunctionExpression.getBody().getStatements()) {
            SequentialDescendant result = this.visit(
                statement,
                new SequentialDescendant(false, false, argument.getProlog())
            );
            isFunctionSequential = isFunctionSequential || result.isSequential();
            if (statement instanceof ExitStatement) {
                exitStatementInstance = (ExitStatement) statement;
            } else if (result.isSequential()) {
                hasNonExitStatementSequentialChild = true;
            }
        }
        isFunctionSequential = isFunctionSequential
            || this.visit(inlineFunctionExpression.getBody().getExpression(), argument).isSequential();
        if (exitStatementInstance != null) {
            if (exitStatementInstance.getExitExpression().isUpdating()) {
                // TODO: Outside of master's scope.
                throw new OurBadException("Updating expressions in exit statements are unsupported!");
            }

        }
        hasNonExitStatementSequentialChild = hasNonExitStatementSequentialChild
            || inlineFunctionExpression.getBody().getExpression().isSequential();
        if (!inlineFunctionExpression.hasSequentialPropertyAnnotation()) {
            /*
             * If we don't have an annotation, then we must determine if
             * the function is sequential by its body's property of being
             * sequential
             */
            inlineFunctionExpression.setSequential(isFunctionSequential);
        } else if (!inlineFunctionExpression.isSequential() && hasNonExitStatementSequentialChild) {
            /*
             * We have a declared non-sequential function.
             * Statements must be non-sequential, and we have a non-exit sequential statement
             */
            throw new InvalidSequentialChildInNonSequentialParent(
                    "The body of a non-sequential function can only contain non-sequential expressions. Only exit statements are allowed in non-sequential functions!",
                    inlineFunctionExpression.getMetadata()
            );
        }
        return new SequentialDescendant(
                inlineFunctionExpression.isSequential(),
                argument.hasInterruptStatement(),
                argument.getProlog()
        );
    }

    @Override
    public SequentialDescendant visitWhileStatement(WhileStatement statement, SequentialDescendant argument) {
        SequentialDescendant descendantResult = visit(
            statement.getStatement(),
            argument
        );
        if (descendantResult.isSequential() && descendantResult.hasInterruptStatement()) {
            statement.setSequential(false);
            return new SequentialDescendant(false, true, argument.getProlog());
        } else if (descendantResult.isSequential()) {
            statement.setSequential(true);
            return new SequentialDescendant(true, false, argument.getProlog());
        }
        statement.setSequential(false);
        return new SequentialDescendant(false, descendantResult.hasInterruptStatement(), argument.getProlog());
    }

    @Override
    public SequentialDescendant visitFlowrStatement(FlowrStatement statement, SequentialDescendant argument) {
        Clause clause = statement.getReturnStatementClause().getFirstClause();
        SequentialDescendant result = argument;
        while (clause != null) {
            result = this.visit(clause, result);
            clause = clause.getNextClause();
        }
        if (result.isSequential() && result.hasInterruptStatement()) {
            statement.setSequential(false);
            return new SequentialDescendant(false, false, argument.getProlog());
        }
        statement.setSequential(result.isSequential());
        return result;
    }

    @Override
    public SequentialDescendant visitTreatExpression(TreatExpression expression, SequentialDescendant argument) {
        SequentialDescendant result = visit(expression.getMainExpression(), argument);
        if (expression.isSequential()) {
            expression.setSequential(true);
            return new SequentialDescendant(true, false, argument.getProlog());
        }
        expression.setSequential(result.isSequential());
        return result;
    }
}
