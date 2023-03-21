package org.rumbledb.expressions.update;

import org.rumbledb.compiler.VisitorConfig;
import org.rumbledb.context.Name;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.exceptions.SemanticException;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.types.SequenceType;

import java.util.Collections;
import java.util.List;

public class CopyDeclaration extends Node {

    private Name variableName;
    private Expression sourceExpression;

    protected ExecutionMode variableHighestStorageMode = ExecutionMode.UNSET;


    public CopyDeclaration(Name variableName,
                           Expression sourceExpression,
                           ExceptionMetadata metadata
    ) {
        super(metadata);
        if (variableName == null) {
            throw new SemanticException("Copy clause must have at least one variable", metadata);
        }
        this.variableName = variableName;
        this.sourceExpression = sourceExpression;
    }

    public Name getVariableName() {
        return variableName;
    }

    public Expression getSourceExpression() {
        return sourceExpression;
    }

    public SequenceType getSourceSequenceType() {
        return sourceExpression.getStaticSequenceType();
    }

    @Override
    public void initHighestExecutionMode(VisitorConfig visitorConfig) {
        this.highestExecutionMode = ExecutionMode.LOCAL;
        this.variableHighestStorageMode = ExecutionMode.LOCAL;
    }

    public ExecutionMode getVariableHighestStorageMode(VisitorConfig visitorConfig) {
        if (
                !visitorConfig.suppressErrorsForAccessingUnsetExecutionModes()
                        && this.variableHighestStorageMode == ExecutionMode.UNSET
        ) {
            throw new OurBadException("A copy variable storage mode is accessed without being set.");
        }
        return this.variableHighestStorageMode;
    }


    @Override
    public List<Node> getChildren() {
        return Collections.singletonList(this.sourceExpression);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCopyDeclaration(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("copy $").append(this.variableName.toString());
        sb.append(" := (");
        this.sourceExpression.serializeToJSONiq(sb, 0);
        sb.append(")\n");
    }
}
