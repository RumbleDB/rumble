package org.rumbledb.expressions.primary;

import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;

import java.util.ArrayList;
import java.util.List;

public class DirElemConstructorExpression extends Expression {
    private final String tagName;
    private final List<Expression> childExpressions;

    public DirElemConstructorExpression(
            String tagName,
            List<Expression> contentExpressions,
            ExceptionMetadata metadata
    ) {
        super(metadata);
        this.tagName = tagName;
        this.childExpressions = contentExpressions;
    }

    public String getTagName() {
        return this.tagName;
    }

    public List<Expression> getChildExpressions() {
        return this.childExpressions;
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitDirElemConstructor(this, argument);
    }

    @Override
    public List<Node> getChildren() {
        List<Node> result = new ArrayList<>();
        if (this.childExpressions != null) {
            result.addAll(this.childExpressions);
        }
        return result;
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append("<" + this.tagName + ">");
        if (this.childExpressions != null && !this.childExpressions.isEmpty()) {
            sb.append("\n");
            for (Expression expr : this.childExpressions) {
                expr.serializeToJSONiq(sb, indent + 1);
            }
            indentIt(sb, indent);
        }
        sb.append("</" + this.tagName + ">\n");
    }

}
