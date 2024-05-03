package org.rumbledb.compiler;

import org.rumbledb.compiler.wrapper.DescendentSequentialProperties;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.InvalidSequentialChildInNonSequentialParent;
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
 * It passes an instance of DescendentSequentialProperties to its descendants to keep a reference to the parent node and
 * to its
 * sequential property.
 */
public class SequentialClassificationVisitor extends AbstractNodeVisitor<DescendentSequentialProperties> {
    private final Prolog prolog;

    public SequentialClassificationVisitor(Prolog prolog) {
        this.prolog = prolog;
    }

    protected DescendentSequentialProperties defaultAction(Node node, DescendentSequentialProperties argument) {
        DescendentSequentialProperties result = this.visitDescendants(node, argument);
        if (node instanceof Expression) {
            ((Expression) node).setSequential(result.isSequential());
        } else if (node instanceof Statement) {
            ((Statement) node).setSequential(result.isSequential());
        }
        return result;
    }

    @Override
    public DescendentSequentialProperties visitDescendants(Node node, DescendentSequentialProperties argument) {
        // Visit each child checking if it is sequential with or without exit statement, while also looking for
        // interrupts.
        boolean hasExitStatementDescendant = false;
        boolean hasNonExitStatementDescendant = false;
        boolean hasInterruptStatement = false;
        for (Node child : node.getChildren()) {
            DescendentSequentialProperties childResult = visit(child, argument);
            hasExitStatementDescendant = hasExitStatementDescendant || childResult.hasExitStatement();
            hasNonExitStatementDescendant = hasNonExitStatementDescendant
                || childResult.hasNonExitSequentialStatement();
            hasInterruptStatement = hasInterruptStatement || childResult.hasInterruptStatement();
        }
        return new DescendentSequentialProperties(
                hasNonExitStatementDescendant,
                hasInterruptStatement,
                hasExitStatementDescendant
        );
    }

    @Override
    public DescendentSequentialProperties visitMainModule(MainModule node, DescendentSequentialProperties argument) {
        this.visit(node.getProlog(), argument);
        this.visit(
            node.getProgram(),
            argument
        );
        return argument;
    }

    @Override
    public DescendentSequentialProperties visitProlog(Prolog node, DescendentSequentialProperties argument) {
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
    public DescendentSequentialProperties visitProgram(Program node, DescendentSequentialProperties argument) {
        return visit(node.getStatementsAndOptionalExpr(), argument);
    }

    @Override
    public DescendentSequentialProperties visitFunctionCall(
            FunctionCallExpression expression,
            DescendentSequentialProperties argument
    ) {
        FunctionDeclaration targetFunction = getFunctionDeclarationFromProlog(
            this.prolog,
            expression.getFunctionIdentifier()
        );
        if (targetFunction == null) {
            expression.setSequential(false);
            return new DescendentSequentialProperties(false, false);

        }
        InlineFunctionExpression functionBody = (InlineFunctionExpression) targetFunction.getExpression();
        if (functionBody.isSequential()) {
            expression.setSequential(true);
            return new DescendentSequentialProperties(true, false);
        }
        expression.setSequential(false);
        return new DescendentSequentialProperties(false, false);
    }


    @Override
    public DescendentSequentialProperties visitAssignStatement(
            AssignStatement statement,
            DescendentSequentialProperties argument
    ) {
        StaticContext statementContext = statement.getStaticContext();
        int variableBlockLevel = statementContext.getVariableBlockLevel(statement.getName());
        // Assign statement is sequential in nature.
        statement.setSequential(true);
        if (variableBlockLevel < statementContext.getBlockLevel()) {
            // Variable is defined outside of this block
            return new DescendentSequentialProperties(true, false);
        }
        // The parent should not be sequential as the assign statement affects a variable declared within the parent.
        return new DescendentSequentialProperties(false, false);
    }

    @Override
    public DescendentSequentialProperties visitApplyStatement(
            ApplyStatement statement,
            DescendentSequentialProperties argument
    ) {
        DescendentSequentialProperties descendant = visit(statement.getApplyExpression(), argument);
        statement.setSequential(
            statement.getApplyExpression().isUpdating() || statement.getApplyExpression().isSequential()
        );
        return new DescendentSequentialProperties(
                statement.isSequential(),
                descendant.hasInterruptStatement(),
                descendant.hasExitStatement()
        );
    }

    @Override
    public DescendentSequentialProperties visitBreakStatement(
            BreakStatement statement,
            DescendentSequentialProperties argument
    ) {
        statement.setSequential(true);
        return new DescendentSequentialProperties(true, true);
    }

    @Override
    public DescendentSequentialProperties visitContinueStatement(
            ContinueStatement statement,
            DescendentSequentialProperties argument
    ) {
        statement.setSequential(true);
        return new DescendentSequentialProperties(true, true);
    }

    @Override
    public DescendentSequentialProperties visitExitStatement(
            ExitStatement statement,
            DescendentSequentialProperties argument
    ) {
        statement.setSequential(true);
        return new DescendentSequentialProperties(false, false, true);
    }

    @Override
    public DescendentSequentialProperties visitFunctionDeclaration(
            FunctionDeclaration expression,
            DescendentSequentialProperties argument
    ) {
        InlineFunctionExpression inlineFunctionExpression = (InlineFunctionExpression) expression.getExpression();
        boolean isFunctionBodySequential = false;
        boolean isNonExitSequential = false;
        // Visit each statement to check if it is sequential and non-exit statement.
        for (Node children : inlineFunctionExpression.getBody().getChildren()) {
            DescendentSequentialProperties result = this.visit(
                children,
                new DescendentSequentialProperties(false, false)
            );
            isFunctionBodySequential = isFunctionBodySequential || result.isSequential();
            if (result.hasNonExitSequentialStatement()) {
                isNonExitSequential = true;
            }
        }

        if (!inlineFunctionExpression.hasSequentialPropertyAnnotation()) {
            /*
             * If we don't have an annotation, then we must determine if
             * the function is sequential by its body's property of being
             * sequential
             */
            inlineFunctionExpression.setSequential(isFunctionBodySequential);
        } else if (!inlineFunctionExpression.isSequential() && isNonExitSequential) {
            /*
             * We have a declared non-sequential function.
             * Statements must be non-sequential, and we have a non-exit sequential statement
             */
            throw new InvalidSequentialChildInNonSequentialParent(
                    "The body of a non-sequential function can only contain non-sequential expressions. Only exit statements are allowed in non-sequential functions!",
                    inlineFunctionExpression.getMetadata()
            );
        }
        return new DescendentSequentialProperties(
                inlineFunctionExpression.isSequential(),
                false
        );
    }

    @Override
    public DescendentSequentialProperties visitWhileStatement(
            WhileStatement statement,
            DescendentSequentialProperties argument
    ) {
        DescendentSequentialProperties descendant = visit(
            statement.getStatement(),
            argument
        );
        if (descendant.isSequential() && !descendant.hasInterruptStatement()) {
            statement.setSequential(true);
            return new DescendentSequentialProperties(
                    descendant.hasNonExitSequentialStatement(),
                    false,
                    descendant.hasExitStatement()
            );
        }
        // If we have a descendant that is sequential with interrupt, the while will be affected by the interrupt,
        // therefore it is not sequential.
        // If the descendant is not sequential, it cannot have an interrupt statement as that would have made it
        // sequential.
        statement.setSequential(false);
        return new DescendentSequentialProperties(false, false, descendant.hasExitStatement());
    }

    @Override
    public DescendentSequentialProperties visitFlowrStatement(
            FlowrStatement statement,
            DescendentSequentialProperties argument
    ) {
        Clause clause = statement.getReturnStatementClause().getFirstClause();
        DescendentSequentialProperties result = argument;
        while (clause != null) {
            result = this.visit(clause, result);
            clause = clause.getNextClause();
        }
        if (result.isSequential() && !result.hasInterruptStatement()) {
            statement.setSequential(true);
            return new DescendentSequentialProperties(
                    result.hasNonExitSequentialStatement(),
                    false,
                    result.hasExitStatement()
            );
        }
        statement.setSequential(true);
        return new DescendentSequentialProperties(false, false, result.hasExitStatement());
    }

    @Override
    public DescendentSequentialProperties visitTreatExpression(
            TreatExpression expression,
            DescendentSequentialProperties argument
    ) {
        DescendentSequentialProperties result = visit(expression.getMainExpression(), argument);
        if (expression.isSequential()) {
            expression.setSequential(true);
            return new DescendentSequentialProperties(true, false, result.hasExitStatement());
        }
        expression.setSequential(result.isSequential());
        return result;
    }

    // Variable declaration is always sequential
    @Override
    public DescendentSequentialProperties visitVariableDeclStatement(
            VariableDeclStatement statement,
            DescendentSequentialProperties argument
    ) {
        visitDescendants(statement, argument);
        statement.setSequential(true);
        // The parent should not become sequential if this is sequential. Basically, the expression containing the
        // declaration is not sequential even though the declaration is. This is enforced as the declaration must be
        // executed (it being sequential), but the expression encapsulating it is non side-effecting if it only contains
        // declarations.
        return new DescendentSequentialProperties(false, false);
    }
}
