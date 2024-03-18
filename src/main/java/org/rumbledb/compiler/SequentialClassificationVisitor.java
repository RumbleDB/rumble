package org.rumbledb.compiler;

import org.rumbledb.compiler.wrapper.SequentialDescendant;
import org.rumbledb.exceptions.InvalidComposability;
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
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.stream.Collectors;

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
        }
        return result;
    }

    @Override
    public SequentialDescendant visitDescendants(Node node, SequentialDescendant argument) {
        return node.getChildren()
            .stream()
            .map(child -> this.visit(child, argument))
            .collect(Collectors.toList())
            .stream()
            .anyMatch(SequentialDescendant::isSequential)
                ? new SequentialDescendant(true, node, argument.getProlog())
                : new SequentialDescendant(false, node, argument.getProlog());
    }

    @Override
    public SequentialDescendant visitMainModule(MainModule node, SequentialDescendant argument) {
        SequentialDescendant visitProlog = this.visit(node.getProlog(), new SequentialDescendant(false, node, null));
        SequentialDescendant visitProgram = this.visit(
            node.getProgram(),
            new SequentialDescendant(false, node, node.getProlog())
        );
        return new SequentialDescendant(
                visitProlog.isSequential() || visitProgram.isSequential(),
                argument.getParent(),
                null
        );
    }

    @Override
    public SequentialDescendant visitProlog(Prolog node, SequentialDescendant argument) {
        node.getImportedModules()
            .forEach(
                libraryModule -> visit(libraryModule, new SequentialDescendant(argument.isSequential(), node, node))
            );
        node.getFunctionDeclarations()
            .forEach(
                functionDeclaration -> visit(
                    functionDeclaration,
                    new SequentialDescendant(argument.isSequential(), node, node)
                )
            );
        node.getVariableDeclarations()
            .forEach(
                variableDeclaration -> visit(
                    variableDeclaration,
                    new SequentialDescendant(argument.isSequential(), node, node)
                )
            );
        return argument;
    }

    @Override
    public SequentialDescendant visitProgram(Program node, SequentialDescendant argument) {
        /* Expect prolog to be passed as argument.getParent(). Thus we can find function definitions in the program. */
        visit(node.getStatementsAndOptionalExpr(), new SequentialDescendant(false, node, argument.getProlog()));
        return argument;
    }

    @Override
    public SequentialDescendant visitFunctionCall(FunctionCallExpression expression, SequentialDescendant argument) {
        FunctionDeclaration targetFunction = getFunctionDeclarationFromProlog(
            argument.getProlog(),
            expression.getFunctionIdentifier()
        );
        if (targetFunction == null) {
            expression.setSequential(false);
            return new SequentialDescendant(false, argument.getParent(), argument.getProlog());

        }
        InlineFunctionExpression functionBody = (InlineFunctionExpression) targetFunction.getExpression();
        if (functionBody.isSequential()) {
            expression.setSequential(true);
            return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
        }
        expression.setSequential(false);
        return new SequentialDescendant(false, argument.getParent(), argument.getProlog());
    }


    @Override
    public SequentialDescendant visitAssignStatement(AssignStatement statement, SequentialDescendant argument) {
        // TODO: Currently always true. Should check expression's variable scope.
        return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
    }

    @Override
    public SequentialDescendant visitApplyStatement(ApplyStatement statement, SequentialDescendant argument) {
        return new SequentialDescendant(
                statement.getApplyExpression().isUpdating(),
                argument.getParent(),
                argument.getProlog()
        );
    }

    // TODO: Must check expression tree to figure out if while or flwor is present.
    @Override
    public SequentialDescendant visitBreakStatement(BreakStatement statement, SequentialDescendant argument) {
        return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
    }

    // TODO: Must check expression tree to figure out if while or flwor is present.
    @Override
    public SequentialDescendant visitContinueStatement(ContinueStatement statement, SequentialDescendant argument) {
        return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
    }

    @Override
    public SequentialDescendant visitExitStatement(ExitStatement statement, SequentialDescendant argument) {
        return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
    }

    @Override
    public SequentialDescendant visitBlockStatement(BlockStatement blockStatement, SequentialDescendant argument) {
        for (Statement statement : blockStatement.getBlockStatements()) {
            SequentialDescendant result = visit(
                statement,
                new SequentialDescendant(false, blockStatement, argument.getProlog())
            );
            if (result.isSequential()) {
                return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
            }
        }
        return new SequentialDescendant(false, argument.getParent(), argument.getProlog());
    }

    @Override
    public SequentialDescendant visitFunctionDeclaration(
            FunctionDeclaration expression,
            SequentialDescendant argument
    ) {
        InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) expression.getExpression();
        if (!inlineFunctionExpression.isSequential()) {
            /*
             * We have a declared non-sequential function.
             * Statements must be non-sequential.
             * However, we allow exit statement!
             */
            inlineFunctionExpression.getBody().getStatements().forEach(statement -> {
                SequentialDescendant result = this.visit(
                    statement,
                    new SequentialDescendant(false, expression, argument.getProlog())
                );
                if (result.isSequential()) {
                    // Sequential statement. We only accept ExitStatement
                    if (!(statement instanceof ExitStatement)) {
                        throw new InvalidComposability(
                                "The body of a non-sequential function can only contain non-sequential expressions. Only exit statements are allowed in non-sequential functions!",
                                expression.getMetadata()
                        );
                    }
                }
            });
            this.visit(inlineFunctionExpression.getBody().getExpression(), argument);
            if (inlineFunctionExpression.getBody().getExpression().isSequential()) {
                throw new InvalidComposability(
                        "The body of a non-sequential function can only contain non-sequential expressions. Only exit statements are allowed in non-sequential functions!",
                        expression.getMetadata()
                );
            }
        }
        return new SequentialDescendant(
                inlineFunctionExpression.isSequential(),
                argument.getParent(),
                argument.getProlog()
        );
    }

    @Override
    public SequentialDescendant visitWhileStatement(WhileStatement statement, SequentialDescendant argument) {
        SequentialDescendant descendantResult = visit(
            statement.getStatement(),
            new SequentialDescendant(false, statement, argument.getProlog())
        );
        if (descendantResult.isSequential()) {
            return new SequentialDescendant(true, argument.getParent(), argument.getProlog());
        }
        return new SequentialDescendant(false, argument.getParent(), argument.getProlog());
    }

    @Override
    public SequentialDescendant visitFlowrStatement(FlowrStatement statement, SequentialDescendant argument) {
        Clause clause = statement.getReturnStatementClause().getFirstClause();
        SequentialDescendant result = new SequentialDescendant(false, statement, argument.getProlog());
        while (clause != null) {
            result = this.visit(clause, result);
            clause = clause.getNextClause();
        }
        boolean isReturnSequential = visit(
            statement.getReturnStatementClause().getReturnStatement(),
            new SequentialDescendant(false, statement, argument.getProlog())
        ).isSequential();
        boolean isStatementSequential = isReturnSequential || result.isSequential();
        return new SequentialDescendant(isStatementSequential, argument.getParent(), argument.getProlog());
    }
}
