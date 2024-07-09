package org.rumbledb.expressions.scripting.declaration;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class CommaVariableDeclStatement extends Statement {
    private final List<VariableDeclStatement> variables;

    public CommaVariableDeclStatement(List<VariableDeclStatement> variables, ExceptionMetadata metadata) {
        super(metadata);
        this.variables = variables;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitCommaVariableDeclStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>(variables);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        for (VariableDeclStatement variableDeclStatement : variables) {
            variableDeclStatement.serializeToJSONiq(sb, 0);
        }
    }

    public List<VariableDeclStatement> getVariables() {
        return variables;
    }
}
