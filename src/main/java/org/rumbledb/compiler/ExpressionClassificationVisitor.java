package org.rumbledb.compiler;

import org.rumbledb.context.BuiltinFunction;
import org.rumbledb.context.BuiltinFunctionCatalogue;
import org.rumbledb.context.FunctionIdentifier;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.InvalidUpdatingExpressionPositionException;
import org.rumbledb.exceptions.SimpleExpressionMustBeVacuousException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.CommaExpression;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;
import org.rumbledb.exceptions.UnknownFunctionCallException;
import org.rumbledb.exceptions.UpdatingFunctionHasReturnTypeException;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.CountClause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.ForClause;
import org.rumbledb.expressions.flowr.GroupByClause;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.OrderByClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.flowr.WhereClause;
import org.rumbledb.expressions.module.VariableDeclaration;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.primary.InlineFunctionExpression;
import org.rumbledb.expressions.scripting.block.BlockExpression;
import org.rumbledb.expressions.scripting.loops.ReturnStatementClause;
import org.rumbledb.expressions.scripting.statement.StatementsAndExpr;
import org.rumbledb.expressions.scripting.statement.StatementsAndOptionalExpr;
import org.rumbledb.expressions.typing.TreatExpression;
import org.rumbledb.expressions.update.AppendExpression;
import org.rumbledb.expressions.update.CopyDeclaration;
import org.rumbledb.expressions.update.DeleteExpression;
import org.rumbledb.expressions.update.InsertExpression;
import org.rumbledb.expressions.update.RenameExpression;
import org.rumbledb.expressions.update.ReplaceExpression;
import org.rumbledb.expressions.update.TransformExpression;
import org.rumbledb.types.FunctionSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionClassificationVisitor extends AbstractNodeVisitor<ExpressionClassification> {

    @Override
    protected ExpressionClassification defaultAction(Node node, ExpressionClassification argument) {
        ExpressionClassification expressionClassification = this.visitDescendants(node, argument);

        if (
            !(node instanceof Expression)
        ) {
            return expressionClassification;
        }

        if (node instanceof InlineFunctionExpression) {
            ((InlineFunctionExpression) node).setExpressionClassification(expressionClassification);
            return expressionClassification;
        }
        if (expressionClassification.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Operand of expression is Updating when it should be Simple or Vacuous",
                    node.getMetadata()
            );
        }
        Expression expression = (Expression) node;
        expression.setExpressionClassification(expressionClassification);
        return expressionClassification;
    }

    @Override
    public ExpressionClassification visitDescendants(Node node, ExpressionClassification argument) {
        List<ExpressionClassification> expressionClassifications = node.getChildren()
            .stream()
            .map(child -> this.visit(child, argument))
            .collect(Collectors.toList());
        ExpressionClassification result = expressionClassifications.stream()
            .anyMatch(ExpressionClassification::isUpdating)
                ? ExpressionClassification.UPDATING
                : ExpressionClassification.SIMPLE;

        return result;
    }

    @Override
    public ExpressionClassification visitStatementsAndOptionalExpr(
            StatementsAndOptionalExpr expression,
            ExpressionClassification argument
    ) {
        this.visitDescendants(expression, argument);
        Expression e = expression.getExpression();
        expression.setExpressionClassification(e.getExpressionClassification());
        return e.getExpressionClassification();
    }

    @Override
    public ExpressionClassification visitStatementsAndExpr(
            StatementsAndExpr expression,
            ExpressionClassification argument
    ) {
        this.visitDescendants(expression, argument);
        Expression e = expression.getExpression();
        expression.setExpressionClassification(e.getExpressionClassification());
        return e.getExpressionClassification();
    }

    @Override
    public ExpressionClassification visitCommaExpression(
            CommaExpression expression,
            ExpressionClassification argument
    ) {
        List<ExpressionClassification> results = expression.getChildren()
            .stream()
            .map(n -> this.visit(n, argument))
            .collect(Collectors.toList());
        boolean anyUpdating = results.stream().anyMatch(ExpressionClassification::isUpdating);
        boolean allUpdatingOrVacuous = results.stream().allMatch(e -> e.isVacuous() || e.isUpdating());

        if (anyUpdating && !allUpdatingOrVacuous) {
            throw new InvalidUpdatingExpressionPositionException(
                    "All expressions in Comma separated expressions must only be Simple expressions or only be Updating/Vacuous expressions",
                    expression.getMetadata()
            );
        }

        if (anyUpdating) {
            expression.setExpressionClassification(ExpressionClassification.UPDATING);
        } else if (results.stream().allMatch(ExpressionClassification::isVacuous)) {
            expression.setExpressionClassification(ExpressionClassification.VACUOUS);
        } else {
            expression.setExpressionClassification(ExpressionClassification.SIMPLE);
        }

        return expression.getExpressionClassification();
    }

    // Region Typing

    @Override
    public ExpressionClassification visitTreatExpression(TreatExpression expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visit(expression.getMainExpression(), argument);
        expression.setExpressionClassification(result);
        return result;
    }


    // Endregion

    // Region FLWOR

    @Override
    public ExpressionClassification visitFlowrExpression(
            FlworExpression expression,
            ExpressionClassification argument
    ) {
        Clause clause = expression.getReturnClause().getFirstClause();
        ExpressionClassification result = argument;
        while (clause != null) {
            result = this.visit(clause, result);
            clause = clause.getNextClause();
        }
        result = expression.getReturnClause().getReturnExpr().getExpressionClassification();
        result = result.isUpdating() ? ExpressionClassification.UPDATING : result;
        expression.setExpressionClassification(result);
        return result;
    }

    @Override
    public ExpressionClassification visitForClause(ForClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visit(expression.getExpression(), argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Expression in For Clause cannot be updating",
                    expression.getMetadata()
            );
        }
        return result;
    }

    @Override
    public ExpressionClassification visitLetClause(LetClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visit(expression.getExpression(), argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Expression in Let Clause cannot be updating",
                    expression.getMetadata()
            );
        }
        return result;
    }

    @Override
    public ExpressionClassification visitGroupByClause(GroupByClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visitDescendants(expression, argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Expressions in Group By Clause cannot be updating",
                    expression.getMetadata()
            );
        }
        return result;
    }

    @Override
    public ExpressionClassification visitOrderByClause(OrderByClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visitDescendants(expression, argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Expressions in Order By Clause cannot be updating",
                    expression.getMetadata()
            );
        }
        return result;
    }

    @Override
    public ExpressionClassification visitWhereClause(WhereClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visitDescendants(expression, argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Expression in Where Clause cannot be updating",
                    expression.getMetadata()
            );
        }
        return result;
    }

    @Override
    public ExpressionClassification visitCountClause(CountClause expression, ExpressionClassification argument) {
        return super.visitCountClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitReturnClause(ReturnClause expression, ExpressionClassification argument) {
        if (expression.getReturnExpr() == null) {
            return argument;
        }
        return this.visit(expression.getReturnExpr(), argument);
    }

    // Endregion

    // Region Control

    @Override
    public ExpressionClassification visitConditionalExpression(
            ConditionalExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification condResult = this.visit(expression.getCondition(), argument);
        if (condResult.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Condition expression in Conditional expression cannot be updating",
                    expression.getMetadata()
            );
        }

        ExpressionClassification thenResult = this.visit(expression.getBranch(), argument);
        ExpressionClassification elseResult = this.visit(expression.getElseBranch(), argument);

        boolean oneUpdating = thenResult.isUpdating() || elseResult.isUpdating();
        boolean bothUpdatingOrVacuous = (thenResult.isUpdating() || thenResult.isVacuous())
            && (elseResult.isVacuous() || elseResult.isUpdating());

        if (oneUpdating && !bothUpdatingOrVacuous) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Both branch expressions in Conditional expression must only be Simple expressions or only be Updating/Vacuous expressions",
                    expression.getMetadata()
            );
        }

        if (oneUpdating) {
            expression.setExpressionClassification(ExpressionClassification.UPDATING);
        } else if (thenResult.isVacuous() && elseResult.isVacuous()) {
            expression.setExpressionClassification(ExpressionClassification.VACUOUS);
        } else {
            expression.setExpressionClassification(ExpressionClassification.SIMPLE);
        }

        return expression.getExpressionClassification();
    }

    @Override
    public ExpressionClassification visitSwitchExpression(
            SwitchExpression expression,
            ExpressionClassification argument
    ) {
        return super.visitSwitchExpression(expression, argument);
    }

    @Override
    public ExpressionClassification visitTypeSwitchExpression(
            TypeSwitchExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification condResult = this.visit(expression.getTestCondition(), argument);
        if (condResult.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Condition expression in Type Switch expression cannot be updating",
                    expression.getMetadata()
            );
        }

        List<ExpressionClassification> branchResults = new ArrayList<>();
        branchResults.add(this.visit(expression.getDefaultCase().getReturnExpression(), argument));
        for (TypeswitchCase branch : expression.getCases()) {
            branchResults.add(this.visit(branch.getReturnExpression(), argument));
        }
        boolean anyUpdating = branchResults.stream().anyMatch(ExpressionClassification::isUpdating);
        if (anyUpdating && !branchResults.stream().allMatch(e -> e.isUpdating() || e.isVacuous())) {
            throw new InvalidUpdatingExpressionPositionException(
                    "All branch expressions in Type Switch expression must only be Simple expressions or only be Updating/Vacuous expressions",
                    expression.getMetadata()
            );
        }

        if (anyUpdating) {
            expression.setExpressionClassification(ExpressionClassification.UPDATING);
        } else if (branchResults.stream().allMatch(ExpressionClassification::isVacuous)) {
            expression.setExpressionClassification(ExpressionClassification.VACUOUS);
        } else {
            expression.setExpressionClassification(ExpressionClassification.SIMPLE);
        }

        return expression.getExpressionClassification();
    }

    // Endregion

    // Region Primary

    private FunctionSignature getSignature(FunctionIdentifier identifier, StaticContext staticContext) {
        BuiltinFunction function;
        FunctionSignature signature;
        function = BuiltinFunctionCatalogue.getBuiltinFunction(identifier);
        if (function != null) {
            signature = function.getSignature();
        } else {
            signature = staticContext.getFunctionSignature(identifier);
        }
        return signature;
    }

    @Override
    public ExpressionClassification visitFunctionCall(
            FunctionCallExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification currArgResult;
        for (Expression argExpr : expression.getArguments()) {
            if (argExpr == null) {
                continue;
            }
            currArgResult = this.visit(argExpr, argument);
            if (currArgResult.isUpdating()) {
                throw new InvalidUpdatingExpressionPositionException(
                        "Arguments to function calls cannot be updating",
                        argExpr.getMetadata()
                );
            }
        }
        FunctionSignature funcSig;
        try {
            funcSig = getSignature(expression.getFunctionIdentifier(), expression.getStaticContext());
        } catch (UnknownFunctionCallException e) {
            throw new UnknownFunctionCallException(expression.getFunctionIdentifier(), expression.getMetadata());
        }
        ExpressionClassification result = funcSig.isUpdating()
            ? ExpressionClassification.UPDATING
            : ExpressionClassification.SIMPLE;
        expression.setExpressionClassification(result);
        // TODO: Make vacuous if call to fn:error? Not present!
        return result;
    }

    @Override
    public ExpressionClassification visitInlineFunctionExpr(
            InlineFunctionExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification bodyResult = this.visit(expression.getBody(), argument);

        if (expression.isUpdating()) {
            if (expression.getActualReturnType() != null) {
                System.err.println(expression);
                throw new UpdatingFunctionHasReturnTypeException(
                        "An updating function cannot have a return type. Return type "
                            + expression.getActualReturnType()
                            + " is specified.",
                        expression.getMetadata()
                );
            }
            if (!expression.isExternal() && !(bodyResult.isUpdating() || bodyResult.isVacuous())) {
                throw new SimpleExpressionMustBeVacuousException(
                        "Top level expression of updating function must be updating or vacuous",
                        expression.getMetadata()
                );
            }
            // TODO CHECK IF EXTERNAL ERROR XUDY0019
            expression.setExpressionClassification(ExpressionClassification.UPDATING);
        } else {
            if (!expression.isExternal() && !bodyResult.isSimple()) {
                throw new InvalidUpdatingExpressionPositionException(
                        "Body of simple inline function must be simple",
                        expression.getMetadata()
                );
            }
            // TODO CHECK IF EXTERNAL ERROR XUDY0018
            expression.setExpressionClassification(ExpressionClassification.SIMPLE);
        }


        return expression.getExpressionClassification();
    }

    // Endregion



    // Region Basic Updating

    @Override
    public ExpressionClassification visitDeleteExpression(
            DeleteExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification mainResult = this.visit(expression.getMainExpression(), argument);
        if (!mainResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Main expression in Delete expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification locatorResult = this.visit(expression.getLocatorExpression(), argument);
        if (!locatorResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Locator expression in Delete expression must be Simple",
                    expression.getMetadata()
            );
        }

        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitRenameExpression(
            RenameExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification mainResult = this.visit(expression.getMainExpression(), argument);
        if (!mainResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Main expression in Rename expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification locatorResult = this.visit(expression.getLocatorExpression(), argument);
        if (!locatorResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Locator expression in Rename expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification replacerResult = this.visit(expression.getNameExpression(), argument);
        if (!replacerResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Replacer expression in Rename expression must be Simple",
                    expression.getMetadata()
            );
        }

        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitReplaceExpression(
            ReplaceExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification mainResult = this.visit(expression.getMainExpression(), argument);
        if (!mainResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Main expression in Replace expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification locatorResult = this.visit(expression.getLocatorExpression(), argument);
        if (!locatorResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Locator expression in Replace expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification replacerResult = this.visit(expression.getReplacerExpression(), argument);
        if (!replacerResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Replacer expression in Replace expression must be Simple",
                    expression.getMetadata()
            );
        }

        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitInsertExpression(
            InsertExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification mainResult = this.visit(expression.getMainExpression(), argument);
        if (!mainResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Main expression in Insert expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification toInsertResult = this.visit(expression.getToInsertExpression(), argument);
        if (!toInsertResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "toInsert expression in Insert expression must be Simple",
                    expression.getMetadata()
            );
        }

        if (expression.hasPositionExpression()) {
            ExpressionClassification positionResult = this.visit(expression.getPositionExpression(), argument);
            if (!positionResult.isSimple()) {
                throw new InvalidUpdatingExpressionPositionException(
                        "Position expression in Insert expression must be Simple",
                        expression.getMetadata()
                );
            }
        }

        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitAppendExpression(
            AppendExpression expression,
            ExpressionClassification argument
    ) {
        ExpressionClassification arrayResult = this.visit(expression.getArrayExpression(), argument);
        if (!arrayResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Array expression in Append expression must be Simple",
                    expression.getMetadata()
            );
        }

        ExpressionClassification toAppendResult = this.visit(expression.getToAppendExpression(), argument);
        if (!toAppendResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "toAppend expression in Append expression must be Simple",
                    expression.getMetadata()
            );
        }

        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitTransformExpression(
            TransformExpression expression,
            ExpressionClassification argument
    ) {

        for (CopyDeclaration copyDecl : expression.getCopyDeclarations()) {
            ExpressionClassification copyDeclResult = this.visit(copyDecl.getSourceExpression(), argument);
            if (!copyDeclResult.isSimple()) {
                throw new InvalidUpdatingExpressionPositionException(
                        "Source expression in Copy Declaration must be Simple",
                        expression.getMetadata()
                );
            }
        }

        ExpressionClassification modifyResult = this.visit(expression.getModifyExpression(), argument);
        if (!(modifyResult.isUpdating() || modifyResult.isVacuous())) {
            throw new SimpleExpressionMustBeVacuousException(
                    "Modify expression must be Updating or Vacuous",
                    expression.getMetadata()
            );
        }

        ExpressionClassification returnResult = this.visit(expression.getReturnExpression(), argument);
        if (!returnResult.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Return expression of Transform expression must be Simple",
                    expression.getMetadata()
            );
        }

        expression.setExpressionClassification(ExpressionClassification.SIMPLE);
        return ExpressionClassification.SIMPLE;
    }

    // Endregion


    @Override
    public ExpressionClassification visitVariableDeclaration(
            VariableDeclaration expression,
            ExpressionClassification argument
    ) {
        if (expression.getExpression() == null) {
            return argument;
        }

        ExpressionClassification result = this.visit(expression.getExpression(), argument);
        if (!result.isSimple()) {
            throw new InvalidUpdatingExpressionPositionException(
                    "Initialising expression in variable declaration must be Simple",
                    expression.getMetadata()
            );
        }
        return ExpressionClassification.SIMPLE;
    }

    @Override
    public ExpressionClassification visitBlockExpr(BlockExpression expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visit(expression.getStatementsAndExpr().getExpression(), argument);
        if (result.isUpdating()) {
            return ExpressionClassification.UPDATING;
        }
        return ExpressionClassification.SIMPLE;
    }

    @Override
    public ExpressionClassification visitReturnStatementClause(
            ReturnStatementClause statementClause,
            ExpressionClassification argument
    ) {
        if (statementClause.getReturnStatement() == null) {
            return argument;
        }
        return this.visitDescendants(statementClause, argument);
    }
}
