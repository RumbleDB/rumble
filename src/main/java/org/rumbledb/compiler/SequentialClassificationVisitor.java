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
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
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
    private final Prolog prolog;

    public SequentialClassificationVisitor(Prolog prolog) {
        this.prolog = prolog;
    }

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
        SequentialDescendant result = new SequentialDescendant(false, false);
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
            argument
        );
        return new SequentialDescendant(
                visitProlog.isSequential() || visitProgram.isSequential(),
                false
        );
    }

    @Override
    public SequentialDescendant visitProlog(Prolog node, SequentialDescendant argument) {
        node.getImportedModules()
            .forEach(
                libraryModule -> visit(libraryModule, argument)
            );
        node.getFunctionDeclarations()
            .forEach(
                functionDeclaration -> visit(
                    functionDeclaration,
                    argument
                )
            );
        node.getVariableDeclarations()
            .forEach(
                variableDeclaration -> visit(
                    variableDeclaration,
                    argument
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
            this.prolog,
            expression.getFunctionIdentifier()
        );
        if (targetFunction == null) {
            expression.setSequential(false);
            return new SequentialDescendant(false, false);

        }
        InlineFunctionExpression functionBody = (InlineFunctionExpression) targetFunction.getExpression();
        if (functionBody.isSequential()) {
            expression.setSequential(true);
            return new SequentialDescendant(true, false);
        }
        expression.setSequential(false);
        return new SequentialDescendant(false, false);
    }


    @Override
    public SequentialDescendant visitAssignStatement(AssignStatement statement, SequentialDescendant argument) {
        StaticContext statementContext = statement.getStaticContext();
        int variableBlockLevel = statementContext.getVariableBlockLevel(statement.getName());
        // Assign statement is sequential in nature.
        statement.setSequential(true);
        if (variableBlockLevel < statementContext.getBlockLevel()) {
            // Variable is defined outside of this block
            return new SequentialDescendant(true, false);
        }
        // The parent should not be sequential as the assign statement affects a variable declared within the parent.
        return new SequentialDescendant(false, false);
    }

    @Override
    public SequentialDescendant visitApplyStatement(ApplyStatement statement, SequentialDescendant argument) {
        visit(statement.getApplyExpression(), argument);
        statement.setSequential(
            statement.getApplyExpression().isUpdating() || statement.getApplyExpression().isSequential()
        );
        return new SequentialDescendant(
                statement.isSequential(),
                false
        );
    }

    @Override
    public SequentialDescendant visitBreakStatement(BreakStatement statement, SequentialDescendant argument) {
        statement.setSequential(true);
        return new SequentialDescendant(true, true);
    }

    @Override
    public SequentialDescendant visitContinueStatement(ContinueStatement statement, SequentialDescendant argument) {
        statement.setSequential(true);
        return new SequentialDescendant(true, true);
    }

    @Override
    public SequentialDescendant visitExitStatement(ExitStatement statement, SequentialDescendant argument) {
        statement.setSequential(true);
        return new SequentialDescendant(true, false);
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
                new SequentialDescendant(false, false)
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
                argument.hasInterruptStatement()
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
            return new SequentialDescendant(false, true);
        } else if (descendantResult.isSequential()) {
            statement.setSequential(true);
            return new SequentialDescendant(true, false);
        }
        statement.setSequential(false);
        return new SequentialDescendant(false, descendantResult.hasInterruptStatement());
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
            return new SequentialDescendant(false, false);
        }
        statement.setSequential(result.isSequential());
        return result;
    }

    @Override
    public SequentialDescendant visitTreatExpression(TreatExpression expression, SequentialDescendant argument) {
        SequentialDescendant result = visit(expression.getMainExpression(), argument);
        if (expression.isSequential()) {
            expression.setSequential(true);
            return new SequentialDescendant(true, false);
        }
        expression.setSequential(result.isSequential());
        return result;
    }

    // Variable declaration is always sequential
    @Override
    public SequentialDescendant visitVariableDeclStatement(
            VariableDeclStatement statement,
            SequentialDescendant argument
    ) {
        visitDescendants(statement, argument);
        statement.setSequential(true);
        // The parent should not become sequential if this is sequential. Basically, the expression containing the
        // declaration is not sequential even though the declaration is. This is enforced as the declaration must be
        // executed (it being sequential), but the expression encapsulating it is non side-effecting if it only contains
        // declarations.
        return new SequentialDescendant(false, false);
    }
}
