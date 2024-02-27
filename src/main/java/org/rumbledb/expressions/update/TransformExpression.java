package org.rumbledb.expressions.update;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransformExpression extends Expression {

    private List<CopyDeclaration> copyDeclarations;
    private Expression modifyExpression;
    private Expression returnExpression;
    private int mutabilityLevel;

    public TransformExpression(
            List<CopyDeclaration> copyDeclarations,
            Expression modifyExpression,
            Expression returnExpression,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        if (copyDeclarations == null || copyDeclarations.isEmpty()) {
            throw new SemanticException("Transform expression must have at least one variable", metadata);
        }
        this.copyDeclarations = copyDeclarations;
        this.modifyExpression = modifyExpression;
        this.returnExpression = returnExpression;
        this.mutabilityLevel = 0;
    }

    public List<CopyDeclaration> getCopyDeclarations() {
        return copyDeclarations;
    }

    public List<Expression> getCopySourceExpressions() {
        return this.copyDeclarations.stream()
            .filter(Objects::nonNull)
            .map(CopyDeclaration::getSourceExpression)
            .collect(Collectors.toList());
    }

    public Expression getModifyExpression() {
        return modifyExpression;
    }

    public Expression getReturnExpression() {
        return returnExpression;
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = this.copyDeclarations.stream()
            .filter(Objects::nonNull)
            .map(CopyDeclaration::getSourceExpression)
            .collect(Collectors.toList());
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
        for (CopyDeclaration copyDecl : this.copyDeclarations) {
            sb.append("copy $").append(copyDecl.getVariableName().toString());
            sb.append(" := (");
            copyDecl.getSourceExpression().serializeToJSONiq(sb, 0);
            sb.append(")\n");
        }
        sb.append("\n modify ");
        this.modifyExpression.serializeToJSONiq(sb, 0);
        sb.append("\n return ");
        this.returnExpression.serializeToJSONiq(sb, 0);
        sb.append("\n");
    }

    public int getMutabilityLevel() {
        return this.mutabilityLevel;
    }

    public void setMutabilityLevel(int mutabilityLevel) {
        this.mutabilityLevel = mutabilityLevel;
    }
}
