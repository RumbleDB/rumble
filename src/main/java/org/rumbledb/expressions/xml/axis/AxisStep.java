package org.rumbledb.expressions.xml.axis;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class AxisStep extends Node {
    private Step step;
    private List<Expression> predicates;

    public AxisStep(Step step) {
        this.step = step;
        this.predicates = new ArrayList<>();
    }

    public AxisStep(Step step, List<Expression> predicates) {
        this.step = step;
        this.predicates = predicates;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitAxisStep(this, argument);
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
