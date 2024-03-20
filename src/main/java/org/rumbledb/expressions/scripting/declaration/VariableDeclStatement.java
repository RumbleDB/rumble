package org.rumbledb.expressions.scripting.declaration;

import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.annotations.Annotation;
import org.rumbledb.expressions.scripting.statement.Statement;
import org.rumbledb.types.SequenceType;

import java.util.ArrayList;
import java.util.List;

import static org.rumbledb.expressions.scripting.annotations.Annotation.checkAssignable;

// TODO: Update specification document to reflect this definition:
// A variable decl statement will be visible to subsequent comma declarations.
public class VariableDeclStatement extends Statement {
    // Default is True for statement variable declaration.
    private final boolean DEFAULT_ASSIGNABLE = true;
    private final List<Annotation> annotations;
    private final Name variableName;
    private final SequenceType variableSequenceType;
    private final Expression variableExpression;
    private final boolean isAssignable;

    public VariableDeclStatement(
            List<Annotation> annotations,
            Name variableName,
            SequenceType variableSequenceType,
            Expression variableExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.annotations = annotations;
        this.variableName = variableName;
        this.variableSequenceType = variableSequenceType;
        this.variableExpression = variableExpression;
        if (this.annotations != null) {
            this.isAssignable = checkAssignable(this.annotations, this.DEFAULT_ASSIGNABLE); // default is true for
                                                                                            // variable statements
        } else {
            this.isAssignable = this.DEFAULT_ASSIGNABLE;
        }
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
    }

    // TODO: VariableStorageMode!

    public SequenceType getSequenceType() {
        if (this.variableSequenceType != null) {
            return this.variableSequenceType;
        }
        if (this.variableExpression != null && this.variableExpression.getStaticSequenceType() != null) {
            return this.variableExpression.getStaticSequenceType();
        }
        return SequenceType.ITEM_STAR;
    }

    public Name getVariableName() {
        return this.variableName;
    }

    public SequenceType getActualSequenceType() {
        return this.variableSequenceType;
    }

    public Expression getVariableExpression() {
        return this.variableExpression;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public boolean isAssignable() {
        return isAssignable;
    }
}
