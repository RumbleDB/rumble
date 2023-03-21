package org.rumbledb.compiler;

import org.rumbledb.context.StaticContext;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.ExpressionClassification;
import org.rumbledb.expressions.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExpressionClassificationVisitor extends AbstractNodeVisitor<ExpressionClassification> {

    public ExpressionClassificationVisitor() {
    }

    @Override
    protected ExpressionClassification defaultAction(Node node, ExpressionClassification argument) {
        ExpressionClassification exprClassification = super.defaultAction(node, argument);
        if (node instanceof Expression) {
            ((Expression) node).setExpressionClassification(exprClassification);
        }
        return exprClassification;
    }

    @Override
    public ExpressionClassification visitDescendants(Node node, ExpressionClassification argument) {
        ExpressionClassification result = null;
        for (Node child : node.getChildren()) {
            ExpressionClassification temp = visit(child, result);
            if (result == null && temp.isUpdating()) {
                result = temp;
            }
        }
        return result == null ? argument : result;
    }
}
