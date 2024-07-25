package org.rumbledb.expressions.xml;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.xml.axis.Step;

import java.util.ArrayList;
import java.util.List;

public class StepExpr extends Expression {
    private Step step;
    private List<Expression> predicates;

    public StepExpr(Step step, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.step = step;
        this.predicates = new ArrayList<>();
    }

    public StepExpr(Step step, List<Expression> predicates, ExceptionMetadata exceptionMetadata) {
        super(exceptionMetadata);
        this.step = step;
        this.predicates = predicates;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitStepExpr(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(predicates);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append(step.toString());
        for (Expression predicate : predicates) {
            sb.append("[ ");
            predicate.serializeToJSONiq(sb, indent);
            sb.append(" ], ");
        }
    }

    public List<Expression> getPredicates() {
        return this.predicates;
    }

    public Step getStep() {
        return this.step;
    }
}
