package org.rumbledb.compiler;

import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.flowr.Clause;
import org.rumbledb.expressions.flowr.FlworExpression;
import org.rumbledb.expressions.flowr.LetClause;
import org.rumbledb.expressions.flowr.ReturnClause;
import org.rumbledb.expressions.module.MainModule;
import org.rumbledb.expressions.primary.ObjectConstructorExpression;
import org.rumbledb.expressions.scripting.Program;

import java.util.ArrayList;
import java.util.List;

public class ProjectionPushdownVisitor extends CloneVisitor {

    private final ProjectionPushdownDetectionVisitor projectionPushdownDetectionVisitor;

    public ProjectionPushdownVisitor() {
        this.projectionPushdownDetectionVisitor = new ProjectionPushdownDetectionVisitor();
    }

    @Override
    protected Node defaultAction(Node node, Node argument) {
        return node;
    }

    @Override
    public Node visitMainModule(MainModule mainModule, Node argument) {
        this.projectionPushdownDetectionVisitor.visit(mainModule, null);
        MainModule result = new MainModule(
                mainModule.getProlog(),
                (Program) visit(mainModule.getProgram(), mainModule.getProlog()),
                mainModule.getMetadata()
        );
        result.setStaticContext(mainModule.getStaticContext());
        return result;
    }

    // region flwor
    @Override
    public Node visitFlowrExpression(FlworExpression expression, Node argument) {
        Clause clause = expression.getReturnClause().getFirstClause();
        Clause result = null;
        while (clause != null) {
            Clause temp = (Clause) this.visit(clause, argument);
            if (temp != null) {
                if (result != null) {
                    result.chainWith(temp);
                }
                result = temp;
            }
            clause = clause.getNextClause();
        }
        return new FlworExpression((ReturnClause) result, expression.getMetadata());
    }

    @Override
    public Node visitLetClause(LetClause clause, Node argument) {
        // return new LetClause(
        // clause.getVariableName(),
        // clause.getActualSequenceType(),
        // (Expression) visit(clause.getExpression(), argument),
        // clause.getMetadata()
        // );

        // code below from dominik
        // problem is that it removes the last let clause (returns null for that)
        if (clause.getReferenced()) {
            return new LetClause(
                    clause.getVariableName(),
                    clause.getActualSequenceType(),
                    (Expression) visit(clause.getExpression(), argument),
                    clause.getMetadata()
            );
        }
        return null;
    }

    @Override
    public Node visitObjectConstructor(ObjectConstructorExpression expression, Node argument) {
        if (expression.isMergedConstructor()) {
            return new ObjectConstructorExpression(
                    (Expression) visit(expression.getChildren().get(0), argument),
                    expression.getMetadata()
            );
        } else {
            List<Expression> keys = new ArrayList<>();
            List<Expression> values = new ArrayList<>();
            for (int i = 0; i < expression.getKeys().size(); i++) {
                if (expression.getReferenced(i)) {
                    keys.add((Expression) visit(expression.getKeys().get(i), argument));
                    values.add((Expression) visit(expression.getValues().get(i), argument));
                }
            }
            return new ObjectConstructorExpression(keys, values, expression.getMetadata());
        }
    }
}
