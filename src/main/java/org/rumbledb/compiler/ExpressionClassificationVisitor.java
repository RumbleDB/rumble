package org.rumbledb.compiler;

import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.InvalidUpdatingExpressionPositionException;
import org.rumbledb.expressions.*;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.control.TypeswitchCase;
import org.rumbledb.expressions.flowr.*;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.update.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionClassificationVisitor extends AbstractNodeVisitor<ExpressionClassification> {

    @Override
    protected ExpressionClassification defaultAction(Node node, ExpressionClassification argument) {
        ExpressionClassification expressionClassification = this.visitDescendants(node, argument);
        if (!(node instanceof Expression)) {
            return expressionClassification;
        }
        Expression expression = (Expression) node;
        expression.setExpressionClassification(expressionClassification);
        return expressionClassification;
    }

    @Override
    public ExpressionClassification visitDescendants(Node node, ExpressionClassification argument) {
        List<ExpressionClassification> expressionClassifications = node.getChildren().stream().map(child -> this.visit(child, argument)).collect(Collectors.toList());
        return expressionClassifications.stream().anyMatch(ExpressionClassification::isUpdating) ?
                ExpressionClassification.UPDATING :
                ExpressionClassification.SIMPLE;
    }

    @Override
    public ExpressionClassification visitCommaExpression(CommaExpression expression, ExpressionClassification argument) {
        List<ExpressionClassification> results = expression.getChildren().stream().map(n -> this.visit(n, argument)).collect(Collectors.toList());
        boolean anyUpdating = results.stream().anyMatch(ExpressionClassification::isUpdating);
        boolean allUpdatingOrVacuous = results.stream().allMatch(e -> e.isVacuous() || e.isUpdating());

        if (anyUpdating && !allUpdatingOrVacuous) {
            throw new InvalidUpdatingExpressionPositionException("All expressions in Comma separated expressions must only be Simple expressions or only be Updating/Vacuous expressions", expression.getMetadata());
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

    // Region FLWOR

    @Override
    public ExpressionClassification visitFlowrExpression(FlworExpression expression, ExpressionClassification argument) {
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
            throw new InvalidUpdatingExpressionPositionException("Expression in For Clause cannot be updating", expression.getMetadata());
        }
        return result;
    }

    @Override
    public ExpressionClassification visitLetClause(LetClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visit(expression.getExpression(), argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException("Expression in Let Clause cannot be updating", expression.getMetadata());
        }
        return result;
    }

    @Override
    public ExpressionClassification visitGroupByClause(GroupByClause expression, ExpressionClassification argument) {
        // TODO: Add check for updating?
        return super.visitGroupByClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitOrderByClause(OrderByClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visitDescendants(expression, argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException("Expressions in Order By Clause cannot be updating", expression.getMetadata());
        }
        return result;
    }

    @Override
    public ExpressionClassification visitWhereClause(WhereClause expression, ExpressionClassification argument) {
        ExpressionClassification result = this.visitDescendants(expression, argument);
        if (result.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException("Expression in Where Clause cannot be updating", expression.getMetadata());
        }
        return result;
    }

    @Override
    public ExpressionClassification visitCountClause(CountClause expression, ExpressionClassification argument) {
        // TODO: Add check for updating?
        return super.visitCountClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitReturnClause(ReturnClause expression, ExpressionClassification argument) {
        return super.visitReturnClause(expression, argument);
    }

    // Endregion

    // Region Control

    @Override
    public ExpressionClassification visitConditionalExpression(ConditionalExpression expression, ExpressionClassification argument) {
        ExpressionClassification condResult = this.visit(expression.getCondition(), argument);
        if (condResult.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException("Condition expression in Conditional expression cannot be updating", expression.getMetadata());
        }

        ExpressionClassification thenResult = this.visit(expression.getBranch(), argument);
        ExpressionClassification elseResult = this.visit(expression.getElseBranch(), argument);

        boolean oneUpdating = thenResult.isUpdating() || elseResult.isUpdating();
        boolean bothUpdatingOrVacuous = (thenResult.isUpdating() || thenResult.isVacuous()) && (elseResult.isVacuous()|| elseResult.isUpdating());

        if (oneUpdating && !bothUpdatingOrVacuous) {
            throw new InvalidUpdatingExpressionPositionException("Both branch expressions in Conditional expression must only be Simple expressions or only be Updating/Vacuous expressions", expression.getMetadata());
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
    public ExpressionClassification visitSwitchExpression(SwitchExpression expression, ExpressionClassification argument) {
        return super.visitSwitchExpression(expression, argument);
    }

    @Override
    public ExpressionClassification visitTypeSwitchExpression(TypeSwitchExpression expression, ExpressionClassification argument) {
        ExpressionClassification condResult = this.visit(expression.getTestCondition(), argument);
        if (condResult.isUpdating()) {
            throw new InvalidUpdatingExpressionPositionException("Condition expression in Type Switch expression cannot be updating", expression.getMetadata());
        }

        List<ExpressionClassification> branchResults = new ArrayList<>();
        branchResults.add(this.visit(expression.getDefaultCase().getReturnExpression(), argument));
        for (TypeswitchCase branch : expression.getCases()) {
            branchResults.add(this.visit(branch.getReturnExpression(), argument));
        }
        boolean anyUpdating = branchResults.stream().anyMatch(ExpressionClassification::isUpdating);
        if (anyUpdating && !branchResults.stream().allMatch(e -> e.isUpdating() || e.isVacuous())) {
            throw new InvalidUpdatingExpressionPositionException("All branch expressions in Type Switch expression must only be Simple expressions or only be Updating/Vacuous expressions", expression.getMetadata());
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

    @Override
    public ExpressionClassification visitFunctionCall(FunctionCallExpression expression, ExpressionClassification argument) {
        // TODO: Make vacuous if call to fn:error?
        return super.visitFunctionCall(expression, argument);
    }

    // Endregion



    // Region Basic Updating

    @Override
    public ExpressionClassification visitDeleteExpression(DeleteExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitRenameExpression(RenameExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitReplaceExpression(ReplaceExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitInsertExpression(InsertExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitAppendExpression(AppendExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    @Override
    public ExpressionClassification visitTransformExpression(TransformExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return ExpressionClassification.BASIC_UPDATING;
    }

    // Endregion
}
