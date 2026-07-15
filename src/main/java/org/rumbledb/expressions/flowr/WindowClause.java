package org.rumbledb.expressions.flowr;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.io.Serializable;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

public class WindowClause extends Clause {

    public enum WindowType {
        TUMBLING,
        SLIDING
    }

    /**
     * Record used to keep track of variables binding of a window condition
     *
     * All of these variables are optional (can be null)
     * 
     * @param currentItem item that starts / ends the window
     * @param position position of the current item in the binding sequence
     * @param previousItem item that precedes the current item in the binding sequence
     * @param nextItem item that follows the current item in the binding sequence
     */
    public record WindowVars(
            Name currentItem,
            Name position,
            Name previousItem,
            Name nextItem) implements Serializable {
        public List<Name> names() {
            return Stream.of(this.currentItem, this.position, this.previousItem, this.nextItem)
                .filter(name -> name != null)
                .toList();
        }
    }

    /**
     * Record to keep the start or end condition of a window clause
     * 
     * @param variables Variables bound to the condition (for example, {@code $start} in
     *        {@code start at $s when fn:true()}
     * @param expression boolean expression that triggers the start / end of window
     * @param only If a start item is identified, but no following item in the binding sequence satisfies the
     *        WindowEndCondition, then the only keyword determines whether a window is generated: if only end is
     *        specified, then no window is generated; otherwise, the end item is set to the last item in the binding
     *        sequence and a window is generated.
     */
    public record WindowCondition(WindowVars variables, Expression expression, boolean only) {
    }

    private final WindowType windowType;
    private final Name windowVariable;
    private final SequenceType sequenceType;
    private final Expression expression;
    private final WindowCondition startCondition;
    private final WindowCondition endCondition;

    public WindowClause(
            WindowType windowType,
            Name windowVariable,
            SequenceType sequenceType,
            Expression expression,
            WindowCondition startCondition,
            WindowCondition endCondition,
            ExceptionMetadata metadata
    ) {
        super(FLWOR_CLAUSES.WINDOW, metadata);
        this.windowType = windowType;
        this.windowVariable = windowVariable;
        this.sequenceType = sequenceType;
        this.expression = expression;
        this.startCondition = startCondition;
        this.endCondition = endCondition;
    }

    public WindowType getWindowType() {
        return this.windowType;
    }

    public Name getWindowVariable() {
        return this.windowVariable;
    }

    public SequenceType getActualSequenceType() {
        return this.sequenceType;
    }

    public SequenceType getSequenceType() {
        return this.sequenceType == null ? SequenceType.createSequenceType("item*") : this.sequenceType;
    }

    public Expression getExpression() {
        return this.expression;
    }

    public WindowCondition getStartCondition() {
        return this.startCondition;
    }

    public WindowCondition getEndCondition() {
        return this.endCondition;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.expression != null) {
            result.add(this.expression);
        }
        if (this.startCondition != null && this.startCondition.expression() != null) {
            result.add(this.startCondition.expression());
        }
        if (this.endCondition != null && this.endCondition.expression() != null) {
            result.add(this.endCondition.expression());
        }
        if (this.getPreviousClause() != null) {
            result.add(this.getPreviousClause());
        }
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitWindowClause(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuilder sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.windowType == WindowType.TUMBLING ? "for tumbling window $" : "for sliding window $");
        sb.append(this.windowVariable);
        if (this.sequenceType != null) {
            sb.append(" as ").append(this.sequenceType);
        }
        sb.append(" in ");
        this.expression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
