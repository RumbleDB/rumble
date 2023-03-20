package org.rumbledb.expressions.update;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransformExpression extends Expression
{

    private List<Node> copyDeclarations;
    private Expression modifyExpression;
    private Expression returnExpression;

    public TransformExpression(List<Node> copyDeclarations,
                               Expression modifyExpression,
                               Expression returnExpression,
                               ExceptionMetadata metadata
    ) {
        super(metadata);
        this.copyDeclarations = copyDeclarations;
        this.modifyExpression = modifyExpression;
        this.returnExpression = returnExpression;
    }

    public List<Node> getCopyDeclarations() {
        return copyDeclarations;
    }

    public Expression getModifyExpression() {
        return modifyExpression;
    }

    public Expression getReturnExpression() {
        return returnExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = this.copyDeclarations.stream().filter(Objects::nonNull).collect(Collectors.toList());
        result.add(this.modifyExpression);
        result.add(this.returnExpression);
        return result;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTransformExpression(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        for (Node copyDecl : this.copyDeclarations) {
            copyDecl.serializeToJSONiq(sb, 0);
        }
        sb.append("\n modify ");
        this.modifyExpression.serializeToJSONiq(sb, 0);
        sb.append("\n return ");
        this.returnExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }
}
