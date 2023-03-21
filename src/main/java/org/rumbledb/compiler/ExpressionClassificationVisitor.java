package org.rumbledb.compiler;

import org.apache.hadoop.fs.Stat;
import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.update.*;

public class ExpressionClassificationVisitor extends AbstractNodeVisitor<StaticContext> {

    @Override
    protected StaticContext defaultAction(Node node, StaticContext argument) {
        StaticContext staticContext = super.defaultAction(node, argument);
        boolean hasUpdatingChild = false;
        for (Node child : node.getChildren()) {
            if (!hasUpdatingChild && child.isUpdating()) {
                hasUpdatingChild = true;
            }
        }
        if (node instanceof Expression && hasUpdatingChild) {
            node.setExpressionClassification(ExpressionClassification.UPDATING);
        }
        return staticContext;
    }

    // Region Basic Updating

    @Override
    public StaticContext visitDeleteExpression(DeleteExpression expression, StaticContext argument) {
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return super.visitDeleteExpression(expression, argument);
    }

    @Override
    public StaticContext visitRenameExpression(RenameExpression expression, StaticContext argument) {
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return super.visitRenameExpression(expression, argument);
    }

    @Override
    public StaticContext visitReplaceExpression(ReplaceExpression expression, StaticContext argument) {
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return super.visitReplaceExpression(expression, argument);
    }

    @Override
    public StaticContext visitInsertExpression(InsertExpression expression, StaticContext argument) {
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return super.visitInsertExpression(expression, argument);
    }

    @Override
    public StaticContext visitAppendExpression(AppendExpression expression, StaticContext argument) {
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        return super.visitAppendExpression(expression, argument);
    }

    @Override
    public StaticContext visitTransformExpression(TransformExpression expression, StaticContext argument) {
        expression.setExpressionClassification(ExpressionClassification.BASIC_UPDATING);
        this.visitDescendants(expression, argument);
        return this.visitDescendants(expression, argument);
    }

    // Endregion
}
