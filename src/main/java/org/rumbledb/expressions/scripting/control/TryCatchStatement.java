package org.rumbledb.expressions.scripting.control;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TryCatchStatement extends Statement {
    private final BlockStatement tryStatement;
    private final Map<String, BlockStatement> catchStatements;
    private final BlockStatement catchAllStatement;

    public TryCatchStatement(
            BlockStatement tryStatement,
            Map<String, BlockStatement> catchStatements,
            BlockStatement catchAllStatement,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.tryStatement = tryStatement;
        this.catchStatements = catchStatements;
        this.catchAllStatement = catchAllStatement;
    }

    public BlockStatement getTryStatement() {
        return this.tryStatement;
    }

    public BlockStatement getCatchAllStatement() {
        return this.catchAllStatement;
    }

    public Map<String, BlockStatement> getCatchStatements() {
        return this.catchStatements;
    }

    public List<String> getErrorsCaught() {
        return new ArrayList<>(this.catchStatements.keySet());
    }

    public BlockStatement getBlockStatementCatching(String error) {
        return this.catchStatements.get(error);
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTryCatchStatement(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        result.add(this.tryStatement);
        result.addAll(this.catchStatements.values());
        if (this.catchAllStatement != null) {
            result.add(this.catchAllStatement);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("try {\n");
        this.tryStatement.serializeToJSONiq(sb, indent + 1);

        indentIt(sb, indent);
        sb.append("}\n");

        if (this.catchStatements != null) {
            for (Map.Entry<String, BlockStatement> entry : this.catchStatements.entrySet()) {
                indentIt(sb, indent);
                sb.append("catch " + entry.getKey() + " {\n");
                entry.getValue().serializeToJSONiq(sb, indent + 1);
                indentIt(sb, indent);
                sb.append("}\n");
            }
        }

        if (this.catchAllStatement != null) {
            indentIt(sb, indent);
            sb.append("catch * {\n");
            this.catchAllStatement.serializeToJSONiq(sb, indent + 1);
            indentIt(sb, indent);
            sb.append("}\n");
        }
    }
}
