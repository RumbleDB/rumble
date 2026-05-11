package org.rumbledb.expressions.scripting.block;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement extends Statement {
    private final List<Statement> blockStatements;

    public BlockStatement(
            List<Statement> blockStatements,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.blockStatements = blockStatements;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitBlockStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        this.blockStatements.forEach(statement -> {
            if (statement != null) {
                result.add(statement);
            }
        });
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        for (int i = 0; i < this.blockStatements.size(); i++) {
            this.blockStatements.get(i).serializeToJSONiq(sb, 0);
            if (i == this.blockStatements.size() - 1) {
                sb.append("\n");
            } else {
                sb.append(", ");
            }
        }
    }

    public List<Statement> getBlockStatements() {
        return this.blockStatements;
    }
}
