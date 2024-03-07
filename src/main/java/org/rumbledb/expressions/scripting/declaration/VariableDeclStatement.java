package org.rumbledb.expressions.scripting.declaration;

import org.antlr.v4.runtime.misc.Pair;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariableDeclStatement extends Statement {
    private final Name variableName;
    private final SequenceType variableSequenceType;
    private final Expression variableExpression;
    private final Map<Name, Pair<SequenceType, Expression>> otherVariables;

    public VariableDeclStatement(
            Name variableName,
            SequenceType variableSequenceType,
            Expression variableExpression,
            Map<Name, Pair<SequenceType, Expression>> otherVariables,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.variableName = variableName;
        this.variableSequenceType = variableSequenceType;
        this.variableExpression = variableExpression;
        this.otherVariables = otherVariables;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitVariableDeclStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.variableExpression != null) {
            result.add(this.variableExpression);
        }
        if (this.otherVariables != null) {
            this.otherVariables.values().forEach(otherExpressions -> {
                if (otherExpressions.b != null) {
                    result.add(otherExpressions.b);
                }
            });
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("declare variable $").append(this.variableName);
        if (this.variableSequenceType != null) {
            sb.append(" as ").append(this.variableSequenceType);
        }
        sb.append(" ");
        this.variableExpression.serializeToJSONiq(sb, 0);
        sb.append(", ");
        if (this.otherVariables != null) {
            this.otherVariables.forEach((key, value) -> {
                sb.append("$ ").append(key).append(" ");
                if (value.a != null) {
                    sb.append("as ").append(value.a);
                }
                sb.append(" ");
                value.b.serializeToJSONiq(sb, 0);
                sb.append(", ");
            });
        }
    }

    // TODO: VariableStorageMode!

    public Name getVariableName() {
        return this.variableName;
    }

    public SequenceType getVariableSequenceType() {
        if (this.variableSequenceType != null) {
            return this.variableSequenceType;
        }
        if (this.variableExpression != null && this.variableExpression.getStaticSequenceType() != null) {
            return this.variableExpression.getStaticSequenceType();
        }
        return SequenceType.ITEM_STAR;
    }

    public SequenceType getActualSequenceType() {
        return this.variableSequenceType;
    }

    public Expression getVariableExpression() {
        return this.variableExpression;
    }

    public Map<Name, Pair<SequenceType, Expression>> getOtherVariables() {
        return otherVariables;
    }
}
