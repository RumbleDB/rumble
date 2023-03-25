package org.rumbledb.compiler;

import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.*;
import org.rumbledb.expressions.control.ConditionalExpression;
import org.rumbledb.expressions.control.SwitchExpression;
import org.rumbledb.expressions.control.TypeSwitchExpression;
import org.rumbledb.expressions.flowr.*;
import org.rumbledb.expressions.primary.FunctionCallExpression;
import org.rumbledb.expressions.update.*;

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
        return super.visitCommaExpression(expression, argument);
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
        return super.visitForClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitLetClause(LetClause expression, ExpressionClassification argument) {
        return super.visitLetClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitGroupByClause(GroupByClause expression, ExpressionClassification argument) {
        return super.visitGroupByClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitOrderByClause(OrderByClause expression, ExpressionClassification argument) {
        return super.visitOrderByClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitWhereClause(WhereClause expression, ExpressionClassification argument) {
        return super.visitWhereClause(expression, argument);
    }

    @Override
    public ExpressionClassification visitCountClause(CountClause expression, ExpressionClassification argument) {
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
        return super.visitConditionalExpression(expression, argument);
    }

    @Override
    public ExpressionClassification visitSwitchExpression(SwitchExpression expression, ExpressionClassification argument) {
        return super.visitSwitchExpression(expression, argument);
    }

    @Override
    public ExpressionClassification visitTypeSwitchExpression(TypeSwitchExpression expression, ExpressionClassification argument) {
        this.visitDescendants(expression, argument);
//        boolean allVacuous = expression.getChildren().stream().allMatch(e -> e.)
        return super.visitTypeSwitchExpression(expression, argument);
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
