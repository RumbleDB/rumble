package org.rumbledb.expressions.xml;

import java.util.ArrayList;
import java.util.List;

import org.rumbledb.expressions.AbstractNodeVisitor;
import org.rumbledb.expressions.Expression;
import org.rumbledb.expressions.Node;
import org.rumbledb.exceptions.ExceptionMetadata;

public class TextNodeExpression extends Expression {

    private String content;

    public TextNodeExpression(String content, ExceptionMetadata metadata) {
        super(metadata);
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public List<Node> getChildren() {
        return new ArrayList<>();
    }

    @Override
    public <T> T accept(AbstractNodeVisitor<T> visitor, T argument) {
        return visitor.visitTextNode(this, argument);
    }

    @Override
    public void serializeToJSONiq(StringBuffer sb, int indent) {
        indentIt(sb, indent);
        sb.append(this.content);
    }
}
