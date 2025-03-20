package org.rumbledb.compiler;

import org.rumbledb.compiler.wrapper.DescendentSequentialProperties;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.InvalidSequentialChildInNonSequentialParent;
import org.rumbledb.exceptions.UndeclaredVariableException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.module.FunctionDeclaration;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.module.Prolog;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.Program;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.control.ConditionalStatement;
import org.rumbledb.expressions.scripting.control.SwitchStatement;
import org.rumbledb.expressions.scripting.control.TryCatchStatement;
import org.rumbledb.expressions.scripting.control.TypeSwitchStatement;
import org.rumbledb.expressions.scripting.declaration.VariableDeclStatement;
import org.rumbledb.expressions.scripting.loops.BreakStatement;
import org.rumbledb.expressions.scripting.loops.ContinueStatement;
import org.rumbledb.expressions.scripting.loops.ExitStatement;
import org.rumbledb.expressions.scripting.loops.FlowrStatement;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.loops.WhileStatement;
import org.rumbledb.expressions.scripting.mutation.ApplyStatement;
import org.rumbledb.expressions.scripting.mutation.AssignStatement;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.types.SequenceType;

import java.util.HashMap;
import java.util.Map;

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
    private int blockLevel;
    private Map<Name, Integer> variableBlockLevel;

    public SequentialClassificationVisitor(Prolog prolog) {
        this.prolog = prolog;
        this.blockLevel = 0;
        this.variableBlockLevel = new HashMap<>();
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
        // Visit variables first as they may be modified in a function.
        node.getVariableDeclarations()
            .forEach(
                variableDeclaration -> visit(
                    variableDeclaration,
                    argument
                )
            );
        node.getFunctionDeclarations()
            .forEach(
                functionDeclaration -> visit(
                    functionDeclaration,
                    argument
                )
            );
        return argument;
    }

    @Override
    public DescendentSequentialProperties visitProgram(Program program, DescendentSequentialProperties argument) {
        return visitDescendantsWithScope(program, argument);
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
        int variableBlockLevel = getVariableBlockLevel(statement.getName(), statement.getMetadata());
        // Assign statement is sequential in nature.
        statement.setSequential(true);
        if (variableBlockLevel < this.blockLevel) {
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
        // Apply Statements are sequential
        statement.setSequential(true);
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
        return new DescendentSequentialProperties(false, true);
    }

    @Override
    public DescendentSequentialProperties visitContinueStatement(
            ContinueStatement statement,
            DescendentSequentialProperties argument
    ) {
        statement.setSequential(true);
        return new DescendentSequentialProperties(false, true);
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
        boolean hasExitStatement = false;
        incrementBlockLevel();
        for (Map.Entry<Name, SequenceType> parameter : inlineFunctionExpression.getParams().entrySet()) {
            addVariableToCurrentBlockLevel(parameter.getKey());
        }
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
            if (result.hasExitStatement()) {
                hasExitStatement = true;
            }
        }
        inlineFunctionExpression.setHasExitStatement(hasExitStatement);
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
        decrementBlockLevel();
        return new DescendentSequentialProperties(
                inlineFunctionExpression.isSequential(),
                false
        );
    }

    @Override
    public DescendentSequentialProperties visitInlineFunctionExpr(
            InlineFunctionExpression expression,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantsWithScope(expression, argument);
    }

    @Override
    public DescendentSequentialProperties visitReturnClause(
            ReturnClause returnClause,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantsWithScope(returnClause, argument);
    }

    @Override
    public DescendentSequentialProperties visitReturnStatementClause(
            ReturnStatementClause returnStatementClause,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantsWithScope(returnStatementClause, argument);
    }


    @Override
    public DescendentSequentialProperties visitWhileStatement(
            WhileStatement statement,
            DescendentSequentialProperties argument
    ) {
        incrementBlockLevel();
        DescendentSequentialProperties descendant = visit(
            statement.getStatement(),
            argument
        );
        if (descendant.isSequential()) {
            statement.setSequential(true);
            decrementBlockLevel();
            return new DescendentSequentialProperties(
                    descendant.hasNonExitSequentialStatement(),
                    false,
                    descendant.hasExitStatement()
            );
        }
        // If we have a descendant that is non-sequential with interrupt, the while will be affected by the interrupt,
        // therefore it is not sequential.
        statement.setSequential(false);
        decrementBlockLevel();
        return new DescendentSequentialProperties(false, false, descendant.hasExitStatement());
    }

    @Override
    public DescendentSequentialProperties visitLetClause(LetClause clause, DescendentSequentialProperties argument) {
        this.visit(clause.getExpression(), argument);
        addVariableToCurrentBlockLevel(clause.getVariableName());
        return argument;
    }

    @Override
    public DescendentSequentialProperties visitForClause(ForClause clause, DescendentSequentialProperties argument) {
        this.visit(clause.getExpression(), argument);
        addVariableToCurrentBlockLevel(clause.getVariableName());
        if (clause.getPositionalVariableName() != null) {
            addVariableToCurrentBlockLevel(clause.getPositionalVariableName());
        }
        return argument;
    }

    @Override
    public DescendentSequentialProperties visitFlowrStatement(
            FlowrStatement statement,
            DescendentSequentialProperties argument
    ) {
        incrementBlockLevel();
        Clause clause = statement.getReturnStatementClause().getFirstClause();
        DescendentSequentialProperties result = argument;
        while (clause != null) {
            result = this.visit(clause, result);
            clause = clause.getNextClause();
        }
        if (result.isSequential()) {
            statement.setSequential(true);
            decrementBlockLevel();
            return new DescendentSequentialProperties(
                    result.hasNonExitSequentialStatement(),
                    false,
                    result.hasExitStatement()
            );
        }
        statement.setSequential(false);
        decrementBlockLevel();
        return new DescendentSequentialProperties(false, false, result.hasExitStatement());
    }

    @Override
    public DescendentSequentialProperties visitTreatExpression(
            TreatExpression expression,
            DescendentSequentialProperties argument
    ) {
        DescendentSequentialProperties result = visit(expression.getMainExpression(), argument);
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
        addVariableToCurrentBlockLevel(statement.getVariableName());
        // The parent should not become sequential if this is sequential. Basically, the expression containing the
        // declaration is not sequential even though the declaration is. This is enforced as the declaration must be
        // executed (it being sequential), but the expression encapsulating it is non side-effecting if it only contains
        // declarations.
        return new DescendentSequentialProperties(false, false);
    }

    @Override
    public DescendentSequentialProperties visitVariableDeclaration(
            VariableDeclaration variableDeclaration,
            DescendentSequentialProperties argument
    ) {
        addVariableToCurrentBlockLevel(variableDeclaration.getVariableName());
        return defaultAction(variableDeclaration, argument);
    }

    @Override
    public DescendentSequentialProperties visitBlockStatement(
            BlockStatement statement,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantWithInterruptPropagation(statement, argument);
    }

    @Override
    public DescendentSequentialProperties visitTypeSwitchStatement(
            TypeSwitchStatement statement,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantWithInterruptPropagation(statement, argument);
    }

    @Override
    public DescendentSequentialProperties visitConditionalStatement(
            ConditionalStatement statement,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantWithInterruptPropagation(statement, argument);
    }

    @Override
    public DescendentSequentialProperties visitTryCatchStatement(
            TryCatchStatement statement,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantWithInterruptPropagation(statement, argument);
    }

    @Override
    public DescendentSequentialProperties visitSwitchStatement(
            SwitchStatement statement,
            DescendentSequentialProperties argument
    ) {
        return visitDescendantWithInterruptPropagation(statement, argument);
    }

    private DescendentSequentialProperties visitDescendantsWithScope(
            Node node,
            DescendentSequentialProperties argument
    ) {
        incrementBlockLevel();
        DescendentSequentialProperties result = defaultAction(node, argument);
        decrementBlockLevel();
        return result;
    }

    private void addVariableToCurrentBlockLevel(Name variableName) {
        this.variableBlockLevel.put(variableName, this.blockLevel);
    }

    private int getVariableBlockLevel(Name variableName, ExceptionMetadata exceptionMetadata) {
        if (!this.variableBlockLevel.containsKey(variableName)) {
            throw new UndeclaredVariableException(
                    "Variable: " + variableName + " not present at block level when expected to",
                    exceptionMetadata
            );
        }
        return this.variableBlockLevel.get(variableName);
    }

    private DescendentSequentialProperties visitDescendantWithInterruptPropagation(
            Statement statement,
            DescendentSequentialProperties argument
    ) {
        DescendentSequentialProperties result = visitDescendantsWithScope(statement, argument);
        if (result.hasInterruptStatement()) {
            // If we have an interrupt, we are sequential
            statement.setSequential(true);
            return new DescendentSequentialProperties(
                    result.hasNonExitSequentialStatement(),
                    true,
                    result.hasExitStatement()
            );
        }
        statement.setSequential(result.isSequential());
        return result;
    }

    private void incrementBlockLevel() {
        this.blockLevel++;
    }

    private void decrementBlockLevel() {
        this.blockLevel--;
    }
}
