package org.rumbledb.expressions.scripting.control;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Node;
import org.rumbledb.expressions.control.CatchPattern;
import org.rumbledb.expressions.scripting.block.BlockStatement;
import org.rumbledb.expressions.scripting.statement.Statement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TryCatchStatement extends Statement {
    private final BlockStatement tryStatement;
    private final Map<CatchPattern, BlockStatement> catchStatements;

    public TryCatchStatement(
            BlockStatement tryStatement,
            Map<CatchPattern, BlockStatement> catchStatements,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.tryStatement = tryStatement;
        this.catchStatements = new LinkedHashMap<>(catchStatements);
    }

    public BlockStatement getTryStatement() {
        return this.tryStatement;
    }

    public Map<CatchPattern, BlockStatement> getCatchStatements() {
        return this.catchStatements;
    }

    public List<CatchPattern> getCatchPatterns() {
        return new ArrayList<>(this.catchStatements.keySet());
    }

    public boolean catchesAll() {
        for (CatchPattern pattern : this.catchStatements.keySet()) {
            if (pattern.isCatchAll()) {
                return true;
            }
        }
        return false;
    }

    public BlockStatement getCatchAllStatement() {
        for (Map.Entry<CatchPattern, BlockStatement> entry : this.catchStatements.entrySet()) {
            if (entry.getKey().isCatchAll()) {
                return entry.getValue();
            }
        }
        return null;
    }

    public BlockStatement getBlockStatementCatching(CatchPattern pattern) {
        return this.catchStatements.get(pattern);
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
            for (Map.Entry<CatchPattern, BlockStatement> entry : this.catchStatements.entrySet()) {
                indentIt(sb, indent);
                sb.append("catch " + entry.getKey() + " {\n");
                entry.getValue().serializeToJSONiq(sb, indent + 1);
                indentIt(sb, indent);
                sb.append("}\n");
            }
        }
    }
}
